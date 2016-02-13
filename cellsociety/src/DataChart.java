import java.util.*;
import javafx.animation.Timeline;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class DataChart {
	private static final int MAX_X_POINTS = 20;
	private static final int MAX_Y_POINTS = 100;
	private List<Integer> variables;
	private List<String> varNames;
	private XYChart.Series<Number,Number> dataSeries;
	private List<XYChart.Series<Number,Number>> seriesList = new ArrayList<XYChart.Series<Number,Number>>();
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	private Simulation mySimulation;
	
	public DataChart(List<Integer> data, List<String> labels, Simulation simulation) {
		variables = data;
		varNames = labels;
		mySimulation = simulation;
	}
	
	public Chart init(){
		xAxis = new NumberAxis(0,MAX_X_POINTS+1,1);
		yAxis = new NumberAxis(0,MAX_Y_POINTS+1,10);
		xAxis.setAnimated(false);
		yAxis.setAnimated(false);
		
		LineChart<Number,Number> chart = new LineChart<Number,Number>(xAxis,yAxis);
		
		chart.getXAxis().setTickLabelsVisible(false);
		chart.getYAxis().setTickLabelsVisible(false);
		chart.getXAxis().setOpacity(0);
		chart.getYAxis().setOpacity(0);
		
		chart.setAnimated(false);
		
		for(int i=0; i<variables.size(); i++){
			XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
			series.setName(varNames.get(i));
			series.getData().add(new LineChart.Data<Number,Number>(0,variables.get(i)));
			chart.getData().add(series);
			seriesList.add(series);
		}
		
		return chart;
	}
	
	public List<XYChart.Series<Number,Number>> getSeries(){
		return seriesList;
	}
	
	public void update(ArrayList<Integer> newVals){
		if(seriesList.get(0).getData().size()>MAX_X_POINTS){
			for(XYChart.Series<Number,Number> series: seriesList){
				series.getData().remove(0);
				xAxis.setLowerBound(xAxis.getLowerBound()+1);
				xAxis.setUpperBound(xAxis.getUpperBound()+1);
				
			}
		}
		for(int i=0; i<seriesList.size(); i++){
			XYChart.Series<Number,Number> series = seriesList.get(i);
			series.getData().add(new LineChart.Data<Number,Number>(mySimulation.getStepCount(),newVals.get(i)));
		}
	}
	
	
	

}
