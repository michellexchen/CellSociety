import java.util.*;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class DataChart {
	private static final int MAX_X_POINTS = 20;
	private List<Integer> variables;
	private List<String> varNames;
	private List<XYChart.Series<Number,Number>> seriesList = new ArrayList<XYChart.Series<Number,Number>>();
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	private Simulation mySimulation;
	
	public DataChart(List<Integer> data, List<String> labels, Simulation simulation, int maxY, int height) {
		variables = data;
		varNames = labels;
		mySimulation = simulation;
		xAxis = new NumberAxis(0,MAX_X_POINTS+1,1);
		yAxis = new NumberAxis(0,maxY+1,maxY/height);
	}
	
	public Chart init(){
		
		LineChart<Number,Number> chart = new LineChart<Number,Number>(xAxis,yAxis);
		
		setChartProperties(chart);
		
		for(int i=0; i<variables.size(); i++){
			XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
			series.setName(varNames.get(i));
			series.getData().add(new LineChart.Data<Number,Number>(0,variables.get(i)));
			chart.getData().add(series);
			seriesList.add(series);
		}
		
		return chart;
	}

	private void setChartProperties(LineChart<Number, Number> chart) {
		chart.getXAxis().setTickLabelsVisible(false);
		chart.getYAxis().setTickLabelsVisible(false);
		chart.getXAxis().setOpacity(0);
		chart.getYAxis().setOpacity(0);
		chart.setCreateSymbols(false);	
		chart.setAnimated(false);
	}
	
	public void update(List<Integer> newVals){
		updateBounds();
		
		for(int i=0; i<seriesList.size(); i++){
			XYChart.Series<Number,Number> series = seriesList.get(i);
			series.getData().add(new LineChart.Data<Number,Number>(mySimulation.getStepCount(),newVals.get(i)));
		}
	}

	private void updateBounds() {
		if(mySimulation.getStepCount()>MAX_X_POINTS){
			for(XYChart.Series<Number,Number> series: seriesList){
				series.getData().remove(0);
				
			}
		}
		if(mySimulation.getStepCount() > MAX_X_POINTS-1){
			xAxis.setLowerBound(xAxis.getLowerBound()+1);
			xAxis.setUpperBound(xAxis.getUpperBound()+1);
		}
	}
	
	
	

}
