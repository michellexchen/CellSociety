import java.util.*;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;

public class Ants extends Simulation {
	ArrayList<int[]> obstacleCoords;
	ArrayList<int[]> nestCoords;
	ArrayList<int[]> foodCoords;
	ArrayList<GridCell> nestCells = new ArrayList<GridCell>();
	ArrayList<GridCell> foodCells = new ArrayList<GridCell>();
	ArrayList<GridCell> obstacleCells = new ArrayList<GridCell>();
	ArrayList<GridCell> cellList = new ArrayList<GridCell>();
	ArrayList<Ant> ants = new ArrayList<Ant>();
	private GridCell[][] myCells;
	private int gridSize;
	private int startAnts;
	private int dieThresh;
	private int antCap;
	private double pherCap;
	private double pherMin;
	private double evapRate;
	private double diffRate;
	private DataChart dataChart;
	private Chart myChart;
	private static final int MAX_X_POINTS = 20;
	

	public Ants(String title, int size, int numCells, boolean tor) { //ArrayList<int[]> nest, ArrayList<int[]> food, int maxAnts, int antLife, int antBreed, int numNest, double minPher, double maxPher, double evaporation, double diffusion, ArrayList<int[]> obstacles, int k, int n
		super(title,size,numCells,tor);
//		obstacleCoords = obstacles;
//		nestCoords = nest;
//		foodCoords = food;
//		startAnts = numNest;
//		dieThresh = antLife;
//		antCap = maxAnts;
//		pherCap = maxPher; 
//		pherMin = minPher;
//		evapRate = evaporation;
//		diffRate = diffusion;
		obstacleCoords = new ArrayList<int[]>();
		obstacleCoords.add(new int[]{1,2});
		obstacleCoords.add(new int[]{4,4});
		obstacleCoords.add(new int[]{3,4});
		obstacleCoords.add(new int[]{2,2});
		nestCoords = new ArrayList<int[]>();
		nestCoords.add(new int[]{1,1});
		foodCoords = new ArrayList<int[]>();
		foodCoords.add(new int[]{3,3});
		startAnts = 2;
		dieThresh = 500;
		antCap = 10;
		pherCap = 1000; 
		pherMin = 0;
		evapRate = 0.0001;
		diffRate = 0.0001;
	}
	
	public Scene init(){
		super.init();
		myCells = super.getCells();
		gridSize = super.getGridSize();
		populateCells("OBSTACLE",Color.YELLOW ,obstacleCoords, obstacleCells);
		populateCells("NEST",Color.BEIGE,nestCoords,nestCells);
		populateCells("FOOD",Color.BLUE,foodCoords,foodCells);
		populateAnts(nestCells,startAnts);
		populateEmpty();
		displayGrid();
		cellList = super.getCellList();
		for(GridCell cell: cellList){
			cell.initForwardNeighbors();
			cell.initBackwardNeighbors();
		}
		
		ArrayList<Integer> dataVals = new ArrayList<Integer>();
		dataVals.add(ants.size());
		
		ArrayList<String> dataNames = new ArrayList<String>();
		dataNames.add("Ants");
		
		dataChart = new DataChart(dataVals,dataNames,this);
		myChart = dataChart.init();
		
		Group root = getRoot();
		root.getChildren().add(myChart);
		
		return super.getMyScene();
	}
	
	private void updateChart(){
		ArrayList<Integer> newData = new ArrayList<Integer>();
		newData.add(ants.size());
		dataChart.update(newData);
	}
	
	private void populateCells(String state, Color color, ArrayList<int[]> coordinates, ArrayList<GridCell> cellDest){
		for(int[] coord: coordinates){
			myCells[coord[0]][coord[1]] = new AntCell(state,color,coord[0],coord[1],antCap,pherCap,pherMin,evapRate,diffRate);
			cellDest.add((AntCell) myCells[coord[0]][coord[1]]);
		}
	}
	
	private void populateEmpty(){
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myCells[x][y]==null){
					myCells[x][y] = new AntCell("GROUND",Color.GREEN,x,y,antCap,pherCap,pherMin,evapRate,diffRate);
				}
			}
		}
	}
	
	private void populateAnts(ArrayList<GridCell> selections, int amt){
		Random rnd = new Random();
		int i = amt;
		while(i>0){
			GridCell chosen = selections.get(rnd.nextInt(selections.size()));
			if(!((AntCell)chosen).atCapacity()){
				Ant ant = new Ant(dieThresh,chosen.getX(),chosen.getY());
				((AntCell)chosen).addAnimal(ant);
				ants.add(ant);
				i--;
			}
			else{
				break;
			}
		}
	}
	
	
	@Override
	public void update() {
		updateChart();
		for(Ant ant: ants){
			if(ant.hasFoodItem()){
				returnToNest(ant);
			}
			else{
				findFoodSource(ant);
			}
		}
		for(GridCell cell: cellList){
			AntCell antCell = (AntCell) cell;
			antCell.diffuse();
			antCell.evaporate();
		}
		populateAnts(nestCells,2);
	}
	
	private void returnToNest(Ant ant){
		GridCell cell = myCells[ant.getX()][ant.getY()];
		ArrayList<GridCell> neighbors = (ArrayList<GridCell>) cell.getAllNeighbors();
		ArrayList<GridCell> forwardNeighbors = (ArrayList<GridCell>) cell.getForwardNeighbors(ant.getOrientation());
		ArrayList<GridCell> backwardNeighbors = (ArrayList<GridCell>) cell.getBackwardNeighbors(ant.getOrientation());
		if(myCells[ant.getX()][ant.getY()].getState()=="FOOD"){
			GridCell maxHomePher = getMaxNeighbor(neighbors,"NEST");
			ant.setOrientation(cell.getOrientationTo(maxHomePher));
		}
		AnimalCell nextLoc = getMaxNeighbor(forwardNeighbors,"NEST");
		if(nextLoc==null){
			nextLoc = getMaxNeighbor(backwardNeighbors,"NEST");
		}
		if(nextLoc!=null){
			dropPheromones(ant,"FOOD");
			ant.setOrientation(cell.getOrientationTo(nextLoc));
			nextLoc.addAnimal(ant);
			((AntCell)myCells[ant.getX()][ant.getY()]).removeAnimal(ant);
			if(nextLoc.getState()=="NEST"){
				ant.dropFood();
			}
		}
		
	}
	
	private void findFoodSource(Ant ant){
		GridCell cell = myCells[ant.getX()][ant.getY()];
		List<GridCell> neighbors = cell.getAllNeighbors();
		ArrayList<GridCell> forwardNeighbors = (ArrayList<GridCell>) cell.getForwardNeighbors(ant.getOrientation());
		ArrayList<GridCell> backwardNeighbors = (ArrayList<GridCell>) cell.getBackwardNeighbors(ant.getOrientation());
		if(myCells[ant.getX()][ant.getY()].getState()=="NEST"){
			GridCell maxFoodPher = getMaxNeighbor(neighbors,"FOOD");
			ant.setOrientation(cell.getOrientationTo(maxFoodPher));
		}
		AntCell nextLoc = selectLocation(forwardNeighbors);
		if(nextLoc==null){
			nextLoc = selectLocation(backwardNeighbors);
		}
		if(nextLoc!=null){
			dropPheromones(ant, "NEST");
			ant.setOrientation(cell.getOrientationTo(nextLoc));
			((AntCell)myCells[ant.getX()][ant.getY()]).removeAnimal(ant);
			nextLoc.addAnimal(ant);
			if(nextLoc.getState()=="FOOD"){
				ant.pickUpFood();
			}
		}
		
	}
	
	private AntCell selectLocation(ArrayList<GridCell> neighbors){
		if(neighbors==null){
			return null;
		}
		ArrayList<GridCell> refinedNeighbors = new ArrayList<GridCell>();
		for(GridCell cell: neighbors){
			if(cell.getState()!="OBSTACLE" && !((AntCell)cell).atCapacity()){
				refinedNeighbors.add(cell);
			}
		}
		if(refinedNeighbors.size()==0){
			return null;
		}
		else{
			return (AntCell) pickByProbability(refinedNeighbors);
		}
	}
	
	private GridCell pickByProbability(ArrayList<GridCell> neighbors){
		Random rnd = new Random();
	    double total = 0;

	    for(GridCell neighbor : neighbors) {
	    	AntCell antNeighbor = (AntCell) neighbor;
            total = total + antNeighbor.getProbability();
        }
	    
	    double index = 0 + (total - 0) * rnd.nextDouble();
        double sum = 0;
        int i=0;
        while(sum < index ) {
             sum = sum + ((AntCell) neighbors.get(i++)).getProbability();
        }
        return neighbors.get(Math.max(0,i-1));
	}
	
	private void dropPheromones(Ant ant, String pherKind){
		GridCell cell = myCells[ant.getX()][ant.getY()];
		AntCell antCell = (AntCell) cell;
		if(antCell.getState()==pherKind){
			antCell.setPher(pherCap, pherKind);
		}
		else{
			AntCell maxCell = getMaxNeighbor(cell.getAllNeighbors(),pherKind);
			double maxPher = maxCell.getPher(pherKind);
			double result = maxPher - 2 - antCell.getPher(pherKind); //constant
			if(result>0){
				antCell.addPher(result, pherKind);
			}
		}
	}
	
	private AntCell getMaxNeighbor(List<GridCell> list,String pherKind){
		if(list==null){
			return null;
		}
		double maxNum=0;
		AntCell maxCell = null;
		for(GridCell cell: list){
			AntCell antCell = (AntCell) cell;
			if (antCell.getPher(pherKind)>=maxNum && antCell.getState()!="OBSTACLE" && !antCell.atCapacity()){
				maxNum = antCell.getPher(pherKind);
				maxCell = antCell;
			}
		}
		return maxCell;
	}


	//edit this
	@Override
	public void updateColors() {

	}

}