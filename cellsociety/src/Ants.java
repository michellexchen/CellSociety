import java.util.*;

import javafx.scene.paint.Color;

public class Ants extends Simulation {
	public static final String NEST = "NEST";
	public static final String FOOD = "FOOD";
	public static final String GROUND = "GROUND";
	public static final String OBSTACLE = "OBSTACLE";
	private static final Color OBSTACLECOLOR = Color.DARKGRAY;
	private static final Color NESTCOLOR = Color.BEIGE;
	private static final Color FOODCOLOR = Color.BLUE;
	private static final Color GROUNDCOLOR = Color.GREEN;
	private static final String ANT_LABEL = "Ants";
	private static final int ANTCAP = 10; //had to hardcode ant cell capacity because of bug
	
	ArrayList<int[]> obstacleCoords;
	ArrayList<int[]> nestCoords;
	ArrayList<int[]> foodCoords;
	ArrayList<GridCell> nestCells;
	ArrayList<GridCell> cellList = new ArrayList<GridCell>();
	ArrayList<Ant> ants = new ArrayList<Ant>();
	private int startAnts;
	private int breedPerStep;
	private int dieThresh;
	private int antCap;
	private double pherCap;
	private double pherMin;
	private double evapRate;
	private double diffRate;
	private double KVal;
	private double NVal;
	private final static String TITLE = "Ant Simulation";
	

	
	public Ants(List<String> columns, int size, boolean tor, boolean tri, int maxAnts, int antLife, int antBreed, int numNest, double minPher, 
		double maxPher, double evaporation, double diffusion, double k, double n){
		
		super(columns, TITLE, size, tor, tri);
		
		startAnts = numNest;
		breedPerStep = antBreed;
		dieThresh = antLife;
		antCap = maxAnts;
		pherCap = maxPher; 
		pherMin = minPher;
		evapRate = evaporation;
		diffRate = diffusion;
		KVal = k;
		NVal = n;
		
		initChart();
				
		initOrientedNeighbors();
		
		super.displayGrid();
		
	}

	private void initOrientedNeighbors() {
		for(GridCell cell: super.getCellList()){
			cell.initForwardNeighbors();
			cell.initBackwardNeighbors();
		}
	}
	
	@Override
	public void initExplicit(char current, int col, int row) {
		if(nestCells == null){
			nestCells = new ArrayList<GridCell>();
		}
		if(ants == null){
			ants = new ArrayList<Ant>();
		}

		if(current == '0'){
			AntCell temp = new AntCell(NEST, NESTCOLOR, col, row, ANTCAP, pherCap, pherMin, evapRate, diffRate, KVal, NVal);
			nestCells.add(temp);
			getCells()[col][row] = temp;
			populateAnts(nestCells, startAnts);
		}
		else if(current == '1'){
			AntCell temp = new AntCell(FOOD, FOODCOLOR, col, row, ANTCAP, pherCap, pherMin, evapRate, diffRate, KVal, NVal);
			getCells()[col][row] = temp;
		}
		else if(current == '2'){
			AntCell temp = new AntCell(OBSTACLE, OBSTACLECOLOR, col, row, ANTCAP, pherCap, pherMin, evapRate, diffRate, KVal, NVal);
			getCells()[col][row] = temp;
		}
		else if(current == '3'){
			AntCell ground = new AntCell(GROUND, GROUNDCOLOR, col, row,ANTCAP, pherCap, pherMin, evapRate, diffRate, KVal, NVal);
			getCells()[col][row] = ground;

		}
	}	
	
	@Override
	public List<Integer> getDataVals(){
		ArrayList<Integer> dataVals = new ArrayList<Integer>();
		dataVals.add(ants.size());
		return dataVals;
	}
	
	@Override
	public List<String> getDataLabels(){
		ArrayList<String> dataNames = new ArrayList<String>();
		dataNames.add(ANT_LABEL);
		return dataNames;
	}
	
	private void populateAnts(ArrayList<GridCell> selections, int amt){
		Random rnd = new Random();
		int i = amt;
		while(i>0){
			ArrayList<GridCell> refined = (ArrayList<GridCell>) refineLocations(selections);
			if(refined.size()==0){
				return;
			}
			GridCell chosen = refined.get(rnd.nextInt(refined.size()));
			Ant ant = new Ant(dieThresh,chosen.getX(),chosen.getY());
			((AntCell)chosen).addAnimal(ant);
			ants.add(ant);
			i--;
		}
	}
	
	@Override
	public void update() {
		updateAnts();
		updatePheromones();
		populateAnts(nestCells,breedPerStep);
	}

	private void updatePheromones() {
		for(GridCell cell: super.getCellList()){
			AntCell antCell = (AntCell) cell;
			antCell.diffuse();
			antCell.evaporate();
		}
	}

	private void updateAnts() {
		ArrayList<Ant> toRemove = new ArrayList<Ant>();
		for(Ant ant: ants){
			if(ant.timeToDie()){
				((AntCell)super.getCells()[ant.getX()][ant.getY()]).removeAnimal(ant);
				toRemove.add(ant);
				continue;
			}
			if(ant.hasFoodItem()){
				returnToNest(ant);
			}
			else{
				findFoodSource(ant);
			}
		}
		ants.removeAll(toRemove);
	}
	
	private void returnToNest(Ant ant){
		GridCell cell = super.getCells()[ant.getX()][ant.getY()];
		ArrayList<GridCell> neighbors = (ArrayList<GridCell>) cell.getAllNeighbors();
		ArrayList<GridCell> forwardNeighbors = (ArrayList<GridCell>) cell.getForwardNeighbors(ant.getOrientation());
		ArrayList<GridCell> backwardNeighbors = (ArrayList<GridCell>) cell.getBackwardNeighbors(ant.getOrientation());
		if(super.getCells()[ant.getX()][ant.getY()].getState()==FOOD){
			GridCell maxHomePher = getMaxNeighbor(neighbors,NEST);
			ant.setOrientation(cell.getOrientationTo(maxHomePher));
		}
		AnimalCell nextLoc = getMaxNeighbor(forwardNeighbors,NEST);
		if(nextLoc==null){
			nextLoc = getMaxNeighbor(backwardNeighbors,NEST);
		}
		if(nextLoc!=null){
			dropPheromones(ant,FOOD);
			ant.setOrientation(cell.getOrientationTo(nextLoc));
			nextLoc.addAnimal(ant);
			((AntCell)super.getCells()[ant.getX()][ant.getY()]).removeAnimal(ant);
			if(nextLoc.getState()==NEST){
				ant.dropFood();
			}
		}
		
	}
	
	private void findFoodSource(Ant ant){
		GridCell cell = super.getCells()[ant.getX()][ant.getY()];
		List<GridCell> neighbors = cell.getAllNeighbors();
		ArrayList<GridCell> forwardNeighbors = (ArrayList<GridCell>) cell.getForwardNeighbors(ant.getOrientation());
		ArrayList<GridCell> backwardNeighbors = (ArrayList<GridCell>) cell.getBackwardNeighbors(ant.getOrientation());
		if(super.getCells()[ant.getX()][ant.getY()].getState()==NEST){
			GridCell maxFoodPher = getMaxNeighbor(neighbors,FOOD);
			ant.setOrientation(cell.getOrientationTo(maxFoodPher));
		}
		AntCell nextLoc = selectLocation(forwardNeighbors);
		if(nextLoc==null){
			nextLoc = selectLocation(backwardNeighbors);
		}
		if(nextLoc!=null){
			dropPheromones(ant, NEST);
			ant.setOrientation(cell.getOrientationTo(nextLoc));
			((AntCell)super.getCells()[ant.getX()][ant.getY()]).removeAnimal(ant);
			nextLoc.addAnimal(ant);
			if(nextLoc.getState()==FOOD){
				ant.pickUpFood();
			}
		}
		
	}
	
	private AntCell selectLocation(ArrayList<GridCell> neighbors){
		if(neighbors==null){
			return null;
		}
		ArrayList<GridCell> refinedNeighbors = (ArrayList<GridCell>) refineLocations(neighbors);
		if(refinedNeighbors.size()==0){
			return null;
		}
		else{
			return (AntCell) pickByProbability(refinedNeighbors);
		}
	}
	
	private List<GridCell> refineLocations(ArrayList<GridCell> locations){
		ArrayList<GridCell> refined = new ArrayList<GridCell>();
		for(GridCell cell: locations){
			if(cell.getState()!=OBSTACLE && !((AntCell)cell).atCapacity()){
				refined.add(cell);
			}
		}
		return refined;
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
		GridCell cell = super.getCells()[ant.getX()][ant.getY()];
		AntCell antCell = (AntCell) cell;
		if(antCell.getState()==pherKind){
			antCell.topPheromones(pherKind);
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
			if (antCell.getPher(pherKind)>=maxNum && antCell.getState()!=OBSTACLE && !antCell.atCapacity()){
				maxNum = antCell.getPher(pherKind);
				maxCell = antCell;
			}
		}
		return maxCell;
	}


	@Override
	public int getChartY() {
		return super.getGridSize()*super.getGridSize()*antCap;
	}

}