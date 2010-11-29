include NodeUtils
unit="#"
ticks={:hourly=>[:hour,1,"HH:00, dd"],
  :three_hourly=>[:hour,3,"HH:00, dd"],
  :six_hourly=>[:hour,6,"HH:00, dd"],
  :half_day=>[:hour,12,"HH:00, dd"],
  :daily=>[:day,1,"MM.dd"],
  :weekly=>[:day,7,"w, yyyy"],
  :monthly=>[:month,1,"MMMMM"]}

def get_statistics(dataset_name,statistics,kpi_name)
  ds_root=dataset(dataset_name)
  analysis_root=find_first(ds_root,{"type"=>"statistics_root"},:ANALYSIS)
  level=find_first(analysis_root,{"type"=>"statistics","name"=>"#{statistics}"},:CHILD)
  data=select_properties :key=>"type",:value=>"name" do
    from do
      root level
      traverse :CHILD, :NEXT
      depth :all
      where {get_property("type")=="s_group"}
      select_properties ["time","name"] do
        from do
          traverse :CHILD, :NEXT
          depth :all
          stop_on {get_property("type")=="s_group"}
          where {get_property("type")=="s_row"}
          select_properties "value" do
            from do
              traverse :CHILD, :NEXT
              depth :all
              stop_on {get_property("type")=="s_row" or get_property("name")==kpi_name}
              where {get_property("type")=="s_cell" and get_property("name")==kpi_name}
            end
          end
        end
      end
    end
  end
  group_data={}
  averages={}
  sum=0.0
  count=0
  max=10

  data.each do |row|
    group=row["s_group"]
    period=row["name"]
    if period!="total"
      if group_data.has_key? group
        group_data[group]<<row
      else
        group_data[group]=[row]
      end
    else
      val=row["value"]
      sum+=val
      count+=1
      max=val if val>max
      averages[group]=val
    end
  end
  [group_data,averages]
end

gis_nodes=[]
map=GIS.maps.first.copy
map.layers.each do |layer|
  resource = layer.findGeoResource(GeoNeo.java_class)
  if !resource.nil?
    geoNeo=resource.resolve(GeoNeo.java_class,nil)
    gisNode=geoNeo.getMainGisNode
    if gisNode[:gis_type]=='drive'
      gis_nodes<<gisNode
    end
  end
end

report 'KPI report' do |r|
  r.map "Drive map", :map=>map, :width=>600, :height=>400 do |m|
  end
  gis_nodes.each do |gisNode|
    drive=gisNode[:name]
    gisNode.rel(:NEXT).end_node.outgoing(:AGGREGATION).depth(1).each do |aggr|
      property=aggr[:name]
      r.chart property do |chart|
        chart.domain_axis='Value'
        chart.range_axis='Count'
        chart.statistics=drive.to_s
        chart.property=property
        chart.distribute=aggr[:distribute]
        chart.select=aggr[:select]
      end

    end
  end
  kpis.each do |kpi|
    kpi_name=kpi[0]
    threshold=kpi[1]
    stats=get_statistics(dataset_name,statistics,kpi_name)
    averages=stats[1]
    stats[0].each do |group,data|
      chart kpi_name do |chart|
        chart.data=data
        chart.type=data.size<10 ? :combined : :time
        chart.subtitle=group
        chart.aggregation=aggregation
        chart.values="value"
        chart.time="time"
        chart.threshold=averages[group] if threshold
        chart.threshold_label="average (#{(averages[group]*1000).round/1000})" if threshold
        chart.range_axis_ticks=ticks
        chart.range_axis="#"
      end
    end
  end

  file("Report for #{statistics}.pdf")
  save

end

