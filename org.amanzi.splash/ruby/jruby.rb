require 'java'
require $jrubyPath + "/lib/ruby/1.8/erb"
require 'ruby/cell'

awe_console_plugin = Java::org.eclipse.core.runtime.Platform.getBundle("org.amanzi.awe.script.jirb").getEntry("ruby")
awe_console_path = Java::org.eclipse.core.runtime.FileLocator.resolve(awe_console_plugin).getFile
require awe_console_path + 'neoSetup.rb'

include_class org.amanzi.splash.chart.Charts

def method_missing(method_id, *args)  
  if Cell.respond_to?(method_id)
    Cell.send(method_id, *args)    
  elsif method_id.to_s =~ /([a-z]{1,3})([0-9]+)/      
    #if method_missing was called with ID of Cell than put this ID to array
    $idArray << method_id
    find_cell(method_id)  
  else
    super.method_missing(method_id.to_s, *args)
  end
end

#
# Returns value of Cell by given ID
#
def find_cell(cell_id)    
  $tableModel.getCellByID(cell_id.to_s)  
end

def update(currentCellId, formula)      
  begin
    #idArray contains IDs of referenced Cells
    $idArray = []
    $cell = find_cell(currentCellId)
    if formula[0] == '='[0]
      formula = formula[1..formula.length]
      display = ERB.new("<%= #{formula} %>").result  
    else        
      display = ERB.new(formula).result
    end
  rescue Exception => e
    display = e.message
  end 
  
  #if the formula was interpreted than update References of Cell
  $tableModel.updateCellReferences(currentCellId.to_s, $idArray)
  display  
end

class Charts
	def initialize
	
	end
	
	def self.create(*args)
        editor=getActiveEditor()
        hash=args[0]
        if editor.respond_to?('plotChart') 
          chartType=Java::org.amanzi.splash.chart.ChartType.value_of(hash[:type].to_s.upcase)
          editor_input=editor.plotChart( hash[:categories].to_a.to_java(Cell), hash[:values].to_a.to_java(Cell),chartType)
          if hash[:type]==:bar
            openChartEditor(editor_input,Java::org.amanzi.splash.utilities.NeoSplashUtil::AMANZI_NEO4J_SPLASH_CHART_EDITOR)
          else
            openChartEditor(editor_input,Java::org.amanzi.splash.utilities.NeoSplashUtil::AMANZI_NEO4J_SPLASH_PIE_CHART_EDITOR)
          end
          
        puts "chart has been created..."
        "CHART"
        else
          puts "Editor #{editor.class} doesn't respond to 'plotChart'"
          "ERROR"
        end
#		puts "chart has been created..."
#		frame = javax.swing.JFrame.new("Chart");
#		frame.setDefaultCloseOperation javax.swing.JFrame::EXIT_ON_CLOSE
#		frame.content_pane.add(panel=javax.swing.JPanel.new)
#		panel.setPreferredSize(java.awt.Dimension.new(700,450))
#		
#		series = "series"
#		dataset = org.jfree.data.category.DefaulyCategory.new
#		(0..32).each{|i| dataset.setValue(0,series,i)}
#		chart = org.jfree.chart.ChartFactory.createBarChart3D(nil, "neighbors", "servers", dataset, org.jfree.chart.plot.PlotOrientation::VERTICAL, true, true, false)
#		
#		panel.add org.jfree.chart.ChartPanel.new(chart)
#		frame.pack
#		frame.setVisible true
	end
end

