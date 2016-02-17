import java.util.*;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class DataChart {
	private static final int MAX_X_POINTS = 20;
	private static final int BOUNDS_OFFSET = 1;
	private static final int X_INCREMENT = 1;
	private List<Integer> variables;
	private List<String> varNames;
	private List<XYChart.Series<Number,Number>> seriesList = new ArrayList<XYChart.Series<Number,Number>>();
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	private Simulation mySimulation;
	
	public DataChart(List<Integer> data, List<String> labels, Simulation simulation, int maxYPoints, int height) {
		variables = data;
		varNames = labels;
		mySimulation = simulation;
		xAxis = new NumberAxis(0,MAX_X_POINTS+BOUNDS_OFFSET,X_INCREMENT);
		yAxis = new NumberAxis(0,maxYPoints+BOUNDS_OFFSET,maxYPoints/height);
	}
	
	public Chart init(){
		LineChart<Number,Number> chart = new LineChart<Number,Number>(xAxis,yAxis);
		
		setChartProperties(chart);
		createChartVariables(chart);
		
		return chart;
	}

	private void createChartVariables(LineChart<Number, Number> chart) {
		for(int i=0; i<variables.size(); i++){
			XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
			series.setName(varNames.get(i));
			series.getData().add(new LineChart.Data<Number,Number>(0,variables.get(i)));
			chart.getData().add(series);
			seriesList.add(series);
		}
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
		updateValues(newVals);
	}

	private void updateValues(List<Integer> newVals) {
		for(int i=0; i<seriesList.size(); i++){
			XYChart.Series<Number,Number> series = seriesList.get(i);
			int x = mySimulation.getStepCount();
			int y = newVals.get(i);
			series.getData().add(new LineChart.Data<Number,Number>(x,y));
		}
	}

	private void updateBounds() {
		if(mySimulation.getStepCount()>MAX_X_POINTS){
			for(XYChart.Series<Number,Number> series: seriesList){
				series.getData().remove(0);
			}
		}
		if(mySimulation.getStepCount() > MAX_X_POINTS-BOUNDS_OFFSET){
			xAxis.setLowerBound(xAxis.getLowerBound()+BOUNDS_OFFSET);
			xAxis.setUpperBound(xAxis.getUpperBound()+BOUNDS_OFFSET);
		}
	}
	
	
	

}
