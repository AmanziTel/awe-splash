require 'java'
require 'date'
require 'neo4j'

#require 'neo4j/auto_tx'
#require 'ruby/cell'
require 'ruby/amanzi_neo4j'
require 'ruby/gis'
require 'ruby/filters'
require "ruby/style"
require 'ruby/search_utils'
require "initKpi"

include_class org.amanzi.neo.core.database.nodes.CellID
include_class org.amanzi.neo.core.service.NeoServiceProvider

include_class org.amanzi.awe.report.model.Report
include_class org.amanzi.awe.report.model.Chart
include_class org.amanzi.awe.report.model.ReportText
include_class org.amanzi.awe.report.model.ReportImage
include_class org.amanzi.awe.report.model.ReportTable
include_class org.amanzi.awe.report.model.ReportMap
include_class org.amanzi.awe.report.charts.ChartType
include_class org.amanzi.awe.report.charts.EventDataset
include_class org.amanzi.awe.report.charts.Charts
include_class org.amanzi.awe.report.util.ReportUtils
include_class org.amanzi.awe.report.pdf.PDFPrintingEngine

include_class org.jfree.data.category.DefaultCategoryDataset;
include_class org.jfree.data.xy.XYBarDataset;
include_class org.jfree.chart.plot.PlotOrientation
include_class org.jfree.chart.plot.Plot
include_class org.jfree.data.time.Millisecond
include_class org.jfree.data.time.Day
include_class org.jfree.data.time.Hour
include_class org.jfree.data.time.Week
include_class org.jfree.data.time.Month
include_class org.jfree.data.time.TimeSeries
include_class org.jfree.data.time.TimeSeriesCollection

puts "Starting service...."
neo_service = NeoServiceProvider.getProvider.getService
database_location = NeoServiceProvider.getProvider.getDefaultDatabaseLocation
Neo4j::Config[:storage_path] = database_location
Neo4j::start(neo_service)
puts "Service started"

module NodeUtils
  def children_of(parent_id)
    begin
      Neo4j::Transaction.run {
        Neo4j.load(parent_id).relationships.outgoing(:CHILD).nodes
      }
    rescue Exception =>exc
      puts "children_of: an exception occured #{exc}"
      nil
    end
  end

  def find_dataset(dataset_name)
    traverser=Neo4j.ref_node.outgoing(:CHILD).depth(1).filter      do
      get_property(:name.to_s)== dataset_name
    end
    traverser.first
  end

  def find_aggr_node(gis_node,property, distribute, select)
    dataset_node=gis_node.rel(:NEXT).end_node
    traverser=dataset_node.outgoing(:AGGREGATION).depth(1).filter do
      prop_name=get_property('name')
      prop_distr=get_property('distribute')
      prop_select=get_property('select')
      prop_name==property and prop_distr==distribute and prop_select==select
    end
    traverser.first
  end

  def create_chart_dataset_aggr(aggr_node,type=:bar)
    ds=DefaultCategoryDataset.new()
    aggr_node.outgoing(:CHILD).depth(:all).each do |node|
      #      puts "count_node: #{node[:name]} -  #{node[:name]} - #{node[:value]}"
      ds.addValue(java.lang.Double.parseDouble(node[:value].to_s), "name", node[:name]);
    end
    ds
  end

  def create_chart_dataset(nodes,category,values,type=:bar)
    if type==:bar
      ds=DefaultCategoryDataset.new()
      nodes.each do |node|
        puts node.props
        values.each do |value|
          puts node[value]
          puts node[category]
          ds.addValue(java.lang.Double.parseDouble(node[value].to_s), value, node[category].to_s);
        end
      end
    elsif type==:pie
      ds=DefaultPieDataset.new()
      nodes.each do |node|
        values.each do |value|
          ds.setValue(node[category].to_s,java.lang.Double.parseDouble(node[value].to_s));#TODO may be value?
        end
      end
    end
    ds
  end

  def update_chart_dataset(ds,average,p)
    average.each_with_index do |row,j|
      if j!=0
        val=row.last
        ds.addValue(java.lang.Double.parseDouble(val.to_s), p.to_s,row[3].to_s);
      end
    end
    ds
  end

  def create_time_chart_dataset(nodes,name,time_period,time_property,value)
    ds=TimeSeriesCollection.new()
    series=TimeSeries.new(name)
    puts "create_time_chart_dataset: #{name} : #{value}"
    nodes.each do |node|
      if time_period==:millisecond
        if node.property? value
          puts "#{node.neo_id} - #{node[time_property]} - #{node[value]}"
          series.addOrUpdate(Millisecond.new(java.util.Date.new(node[time_property])), java.lang.Double.parseDouble(node[value].to_s));
        end
      end
    end
    ds.addSeries(series)
    ds
  end

  def create_event_chart_dataset(nodes,name,time_period,time_property,event_property,event_value)
    ds=EventDataset.new(name,event_value)
    nodes.each do |node|
      if time_period==:millisecond
        if node.property? event_property
          puts "event found: #{node.neo_id} - #{node[time_property]} - #{node[event_property]}"
          ds.addEvent(node[event_property], Millisecond.new(java.util.Date.new(node[time_property])))
        end
      end
    end
    ds
  end

  def  get_sub_nodes_with_props(parent, node_type, relation, time)
    time_format = '%H:%M:%S'
    parent.outgoing(relation).depth(:all).stop_on do
      prop_time=get_property(Java::org.amanzi.neo.core.INeoConstants::PROPERTY_TIME_NAME)
      #      puts "#{node_type}.props #{props}"
      if prop_time.nil?
        false
      else
        t=DateTime.strptime(prop_time,time_format)
        Time.gm(t.year,t.mon,t.day,t.hour,t.min,t.sec)>=time
      end
    end
  end
end

class Search
  attr_accessor :name
  def initialize(name,params)
    @name=name
    @traversers=[Neo4j.ref_node.outgoing(:CHILD).depth(1)]
    @params=params
  end

  def traverse(direction, depth, *relation)
    new_traversers=[]
    if direction==:outgoing
      @traversers.each do |traverser|
        new_traversers<<traverser.outgoing(*relation).depth(depth)
      end
    elsif direction==:incoming
      @traversers.each do |traverser|
        new_traversers<<traverser.incoming(*relation).depth(depth)
      end
    else #both
      @traversers.each do |traverser|
        new_traversers<<traverser.both(*relation).depth(depth)
      end
    end
    @traversers=new_traversers
  end

  def filter(&block)
    new_traversers=[]
    @traversers.each do |traverser|
      new_traversers<<traverser.filter(&block)
    end
    @traversers=new_traversers
  end

  def stop(&block)
    new_traversers=[]
    @traversers.each do |traverser|
      new_traversers<<traverser.stop_on(&block)
    end
    @traversers=new_traversers
  end

  def parent(&block)
    self.instance_eval &block
    new_traversers=[]
    @traversers.each do |traverser|
      traverser.each do |node|
        new_traversers<<node
      end
    end
    @traversers=new_traversers
  end

  def where(&block)
    filter(&block)
  end

  def from(&block)
    parent(&block)
  end

  def each
    @traversers.each do |traverser|
      traverser.each do |node|
        yield node
      end
    end
  end
end

class CellID
  attr_writer :beginRange
  attr_writer :endRange
  def initialize(name)
    @fullID = name
  end

  #
  # Returns a succesor of a CellID
  #
  def succ
    if self.rowIndex<@endRange.rowIndex
      cellId=CellID.new(rowIndex+1,columnIndex)
      cellId.beginRange=@beginRange
      cellId.endRange=@endRange
      cellId
    else
      cellId=CellID.new(@beginRange.rowIndex,columnIndex+1)
      cellId.beginRange=@beginRange
      cellId.endRange=@endRange
      cellId
    end
  end

  #
  # Compares two cellIDs
  #
  def <=>(value)
    if @beginRange.nil?
      @beginRange=self
    end
    if @endRange.nil?
      @endRange=value
    end
    if self.rowIndex!=value.rowIndex
      self.rowIndex<=>value.rowIndex
    else
      self.columnIndex<=>value.columnIndex
    end
  end
end

class Report
  include NodeUtils
  attr_reader :name
  attr_accessor :date,:author
  def initialize(name)
    @name = name
  end

  def setup(&block)
    Neo4j::Transaction.run{
      self.instance_eval &block
      self
    }
  end

  def author (new_author)
    setAuthor(new_author)
  end

  def date (new_date)
    setDate(new_date)
  end

  def text (new_text)
    begin
      addPart(ReportText.new(new_text))
    rescue =>e
      comment="#{getErrors().size+1}) the text '#{new_text}' was not added because of the error: #{e}\n"
      addError(comment+e.backtrace.join("\n"))
    end
  end

  def file (file_name)
    setFile(file_name.to_s)
  end

  def image (image_file_name)
    begin
      addPart(ReportImage.new(image_file_name))
    rescue =>e
      comment="#{getErrors().size+1}) the image '#{image_file_name}' was not added because of the error: #{e}\n"
      addError(comment+e.backtrace.join("\n"))
    end
  end

  def table (title,parameters=nil,&block)
    begin
      currTable=ReportTable.new(title)
      currTable.setup(&block)
      currTable.height=parameters[:height] if !parameters.nil?
      currTable.width=parameters[:width]  if !parameters.nil?
      addPart(currTable)
    rescue =>e
      comment="#{getErrors().size+1}) the table '#{title}' was not added because of the error: #{e}\n"
      addError(comment+e.backtrace.join("\n"))
    end
  end

  def chart(name,parameters=nil,&block)
    begin
      currChart=Chart.new(name)
      currChart.setup(&block)
      currChart.height=parameters[:height]||400 if !parameters.nil?
      currChart.width=parameters[:width]||400 if !parameters.nil?
      addPart(currChart)
    rescue =>e
      comment="#{getErrors().size+1}) the chart '#{name}' was not added because of the error: #{e}\n"
      addError(comment+e.backtrace.join("\n"))
    end
  end

  def map(title,parameters,&block)
    begin
      udig_map=parameters[:map]
      puts udig_map.nil?
      #      puts "map: #{udig_map.getID()}"
      currMap=ReportMap.new(title,udig_map)
      currMap.height=parameters[:height]
      currMap.width=parameters[:width]
      currMap.setup(&block)
      addPart(currMap)
    rescue =>e
      comment="#{getErrors().size+1}) the map '#{title}' was not added because of the error: #{e}\n"
      addError(comment+e.backtrace.join("\n"))
    end
  end

  def save
    engine=PDFPrintingEngine.new
    engine.printReport(self)
    puts "Report '#{@name}' saved"
  end
end

class ReportMap
  def setup(&block)
    self.instance_eval &block
    self
  end

  def layers
    map.layers
  end
end

class ReportTable
  include NodeUtils

  attr_accessor :title, :properties, :nodes, :sheet, :range, :kpi
  def initialize(title)
    self.title = title
  end

  def setup(&block)
    self.instance_eval &block if block_given?
    get_data
  end

  def  get_data
    Neo4j::Transaction.run {
      if !@nodes.nil?
        #      puts @nodes.class
        @nodes.each do |n|
          n=Neo4j.load_node(n) if n.is_a? Fixnum
          @properties=n.props.keys if @properties.nil?
          row=Array.new
          @properties.each do |p|
            if p!="id"
              row<<if n.property? p then n.get_property(p).to_s else "" end
            else
              row<<n.neo_id.to_s
            end
          end
          addRow(row.to_java(java.lang.String))
        end
        setHeaders(@properties.to_java(java.lang.String)) if @properties.is_a? Array
      elsif !@sheet.nil?
        sheetName=@sheet
        sheetNodes=Neo4j.load_node($RUBY_PROJECT_NODE_ID).outgoing(:SPREADSHEET).depth(:all).filter      do
          get_property('name')== sheetName
        end
        range=@range
        columnNodes=sheetNodes.first.outgoing(:COLUMN).depth(1).filter do
          #          puts "sheetNode.traverse.outgoing(:COLUMN) #{get_property(:name)}"
          puts get_property(:name)
          index=CellID.getColumnIndexFromCellID(get_property(:name))
          puts "index #{index}"
          index>=range.begin.getColumnIndex() and index<=range.end.getColumnIndex()
        end
        columnNodes.each do |col|
          puts "---> col #{col}"
          cells=col.traverse.outgoing(:COLUMN_CELL).depth(1).filter do
            name=relationship(:ROW_CELL,:incoming).start_node[:name]
            puts "cells row name #{name}"
            rowIndex=name.to_i
            rowIndex>=range.begin.getRowName().to_i and rowIndex<=range.end.getRowName().to_i
            true
          end
          cells.each {|cell| puts "cell #{cell}"}
        end
        #      TODO traverse rows and columns
      elsif !@kpi.nil?
        @kpi.each_with_index do |row,i|
          if i==0
            setHeaders(row.to_java(java.lang.String))
          else
            addRow(row.to_java(java.lang.String))
          end
        end
      end
    }
  end

  def select(name,params=nil,&block)
    Neo4j::Transaction.run {
      #          puts "======> [ReportTable.select]"
      nodes=Search.new(name,params)
      nodes.instance_eval &block
      @properties=params[:properties]if !params.nil?  #TODO
      nodes.each do |n|
        properties=n.props.keys if @properties.nil?
        row=Array.new
        @properties.each do |p|
          if p!="id"
            row<< if n.property? p then n.get_property(p).to_s else "" end
          else
            row<<n.neo_id.to_s
          end
        end
        addRow(row.to_java(java.lang.String))
      end
      setHeaders(@properties.to_java(java.lang.String)) if @properties.is_a? Array
    }
  end

end

class Chart
  include NodeUtils

  attr_accessor :title, :type, :orientation, :domain_axis, :range_axis,:kpi
  attr_writer :categories,:values, :nodes, :statistics
  attr_writer:property, :distribute, :select
  attr_writer :drive, :event, :property1, :property2, :start_time, :length
  attr_accessor :dataset, :properties, :aggregation
  attr_writer :data, :time, :threshold
  def initialize(title)
    @datasets=[]
    self.title = title
  end

  def sheet=(sheet_name)
    @sheet=sheet_name
  end

  def select(name,params,&block)
    Neo4j::Transaction.run {
      nodes=Search.new(name,params)
      nodes.instance_eval &block
      if @type==:time
        event=params[:event]
        if !event.nil?
          #assume that we have one value
          @datasets<< create_event_chart_dataset(nodes,name,params[:time_period],params[:categories],params[:values],event)
        else
          params[:values].each do |value|
            @datasets<< create_time_chart_dataset(nodes,value,params[:time_period],params[:categories],value)
          end
        end
      else
        puts params[:values]
        puts params[:categories]
        if params[:values].is_a? String
          #            setDataset(create_chart_dataset(nodes,params[:categories],[params[:values]]))
          @datasets<<create_chart_dataset(nodes,params[:categories],[params[:values]])
        elsif params[:values].is_a? Array
          @datasets<<create_chart_dataset(nodes,params[:categories],params[:values])
          #            setDataset(create_chart_dataset(nodes,params[:categories],params[:values]))
        else
          puts "Error: Only Strings or Arrays of Strings are supported for chart values!"
        end
      end
    }
  end

  def setup(&block)
    self.instance_eval(&block) #if block_given?
    Neo4j::Transaction.run {
      #JFreeChart specific settings
      if !@orientation.nil?
        if @orientation==:vertical
          setOrientation(PlotOrientation::VERTICAL)
        elsif @orientation==:horizontal
          setOrientation(Java::org.jfree.chart.plot.PlotOrientation::HORIZONTAL)
        end
      end
      setDomainAxisLabel(@domain_axis) if !@domain_axis.nil?
      setRangeAxisLabel(@range_axis) if !@range_axis.nil?
      @type=:bar if @type.nil?
      setChartType(ChartType.value_of(@type.to_s.upcase))
      #
      if !@sheet.nil?
        setCategories(@categories.begin,@categories.end)if !@categories.nil?
        setValues(@values.begin,@values.end) if !@values.nil?
        setSheet(@sheet)
      elsif !@nodes.nil?
        setNodeIds(@nodes.to_java(java.lang.Long)) if @nodes.is_a? Array
        setCategoriesProperty(@categories) if !@categories.nil? and  @categories.is_a? String
        setValuesProperties(@values.to_java(java.lang.String)) if !@values.nil? and   @values.is_a? Array
        #            setDataset(create_chart_dataset(@nodes,@categories,@values))
        @datasets<<create_chart_dataset(@nodes,@categories,@values)
      elsif !@statistics.nil?
        dataset_node=find_dataset(@statistics)
        puts "dataset_node #{dataset_node}"
        if !@property.nil? and !@distribute.nil? and !@select.nil?
          aggr_node=find_aggr_node(dataset_node,@property,@distribute,@select)
          puts "aggr_node #{aggr_node}"
          #                            setDataset(create_chart_dataset_aggr(aggr_node))
          @datasets<<create_chart_dataset_aggr(aggr_node)
        end
      elsif !@dataset.nil?
        puts "chart setup #{Time.now}"
        props=[]
        @properties.each do |p|
          props<<p
        end
        props<<"site_name"<<"cell_name"<<"time"<<"date"
        props.flatten
        puts "collecting data: #{Time.now}"
        aggr=@dataset.collect(props).aggregate("site_name")
        puts "collecting data finished: #{Time.now}"
        result=[]
        @properties.each do |p|
          result<<Hash.new
        end
        puts "aggregating data: #{Time.now}"
        aggr.each do |obj,rows|
          rows.each do |row|
            site=obj
            cell=row['cell_name']
            date=row['date']
            time=row['time']
            @properties.each_with_index do |p,i|
              value=row[p]
              aggregate_sites(result[i],site,cell,date,time,value)
            end
          end
        end
        puts "aggregating data finished: #{Time.now}"
        averages=[]
        ds=DefaultCategoryDataset.new()
        time_label=get_time_label(aggregation)
        puts "calculating averages: #{Time.now}"
        @properties.each_with_index do |p,i|
          average=calculate_average(result[i], @aggregation)
          update_chart_dataset(ds,average,p)
        end
        puts "calculating averages finished: #{Time.now}"
        @datasets<<ds
      elsif !@kpi.nil?
        ds=DefaultCategoryDataset.new()
        @datasets<<update_chart_dataset(ds,@kpi,"value")
      elsif !@data.nil?
        #            ds=DefaultCategoryDataset.new()
        ds=TimeSeriesCollection.new()
        ds_series=TimeSeries.new("Values")
        if !@threshold.nil?
          ds_time=TimeSeriesCollection.new()
          ds_time_series=TimeSeries.new("Threshold")
        end
        @data.each do |row|
          puts row
          if !row[@time].nil?
            time=java.util.Date.new(row[@time])
            if @aggregation==:hourly
              puts "@aggregation==:hourly"
              date=Hour.new(time)
            elsif @aggregation==:daily
              puts "@aggregation==:daily"
              date=Day.new(time)
            elsif @aggregation==:weekly
              puts "@aggregation==:weekly"
              date=Week.new(time)
            elsif @aggregation==:monthly
              puts "@aggregation==:monthly"
              date=Month.new(time)
            end
            puts "date: #{date} "
          end
          ds_series.add(date, java.lang.Double.parseDouble(row[@values].to_s)) if !row[@time].nil? and !row[@values].nil?
          ds_time_series.add(date, @threshold) if !@threshold.nil? and !row[@time].nil? 
        end
        ds.addSeries(ds_series)
        if !@threshold.nil?
          ds_time.addSeries(ds_time_series)
          @datasets<<ds_time
        end
        @datasets<<XYBarDataset.new(ds,0.1)
      end


        puts "@type=#{@type}"
      if @type==:time
        plot=Java::org.jfree.chart.plot.XYPlot.new
        for i in 0..@datasets.size-1
          puts "i=#{i} #{@datasets[i]}" #TODO delete debug info
          Charts.applyDefaultSettings(plot,@datasets[i],i)
        end
        Charts.applyMainVisualSettings(plot, getRangeAxisLabel(),getDomainAxisLabel(),getOrientation())
      elsif @type==:bar
        puts "@type==:bar"
        plot=Java::org.jfree.chart.plot.CategoryPlot.new
        plot.setDataset(@datasets[0])
      elsif @type==:combined
        plot=Java::org.jfree.chart.plot.XYPlot.new
        for i in 0..@datasets.size-1
          Charts.applyDefaultSettings(plot,@datasets[i],i)
        end
        Charts.applyMainVisualSettings(plot, getRangeAxisLabel(),getDomainAxisLabel(),getOrientation())
      end
      setPlot(plot)
    }
    self
  end
end

def method_missing(method_id, *args)
  if method_id.to_s =~ /([a-z]{1,3})([0-9]+)/
    CellID.new(method_id.to_s)
  else
    #      $missing_methods=$missing_methods||Hash.new
    #      if !$missing_methods.has_key method_id
    #        $missing_methods[method_id]=[]
    #      end
    puts "report.rb: Unknown method: #{method_id}"
    super.method_missing(method_id.to_s, *args)
  end
end

def report (name, &block)
  report=Report.new(name)
  begin
    report.setup(&block)
  rescue =>e
    comment="#{report.getErrors().size+1}) #{e}:\n"
    report.addError(comment+e.backtrace.join("\n"))
    ReportUtils::showErrorDlg("A ruby exception occurred during the report creation: #{e}",e.backtrace.join("\n"))
    puts e
  ensure
    report
  end
end

def chart(name,&block)
  begin
    currChart=Chart.new(name)
    currChart.setup(&block)
    $report_model.createPart(currChart)
  rescue =>e
    ReportUtils::showErrorDlg("A ruby exception occurred: #{e}",e.backtrace.join("\n"))
  end
end

def text (new_text)
  begin
    $report_model.createPart(ReportText.new(new_text))
  rescue =>e
    ReportUtils::showErrorDlg("A ruby exception occurred: #{e}",e.backtrace.join("\n"))
  end
end

def image (image_file_name)
  begin
    $report_model.createPart(ReportImage.new(image_file_name))
  rescue =>e
    ReportUtils::showErrorDlg("A ruby exception occurred: #{e}",e.backtrace.join("\n"))
  end
end

