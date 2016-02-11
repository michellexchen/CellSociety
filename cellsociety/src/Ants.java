import java.util.*;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Ants extends Simulation {
	ArrayList<int[]> obstacleCoords;
	ArrayList<int[]> nestCoords;
	ArrayList<int[]> foodCoords;
	ArrayList<AntCell> nestCells;
	ArrayList<AntCell> foodCells;
	ArrayList<AntCell> obstacleCells;
	ArrayList<GridCell> cellList = new ArrayList<GridCell>();
	ArrayList<Ant> ants = new ArrayList<Ant>();
	private GridCell[][] myCells;
	private int gridSize;
	private int startAnts;
	private int dieThresh;
	private int antCap;
	private double pherCap;
	private double evapRate;
	private double diffRate;
	

	public Ants(String title, int size, int numCells, ArrayList<int[]> nest, ArrayList<int[]> food, int maxAnts, int antLife, int antBreed, int numNest, double minPher, double maxPher, double evaporation, double diffusion, ArrayList<int[]> obstacles, int k, int n) {
		super(title,size,numCells);
		obstacleCoords = obstacles;
		nestCoords = nest;
		foodCoords = food;
		startAnts = numNest;
		dieThresh = antLife;
		antCap = maxAnts;
		pherCap = maxPher; 
		evapRate = evaporation;
		diffRate = diffusion;
	}
	
	public Scene init(){
		super.init();
		myCells = super.getCells();
		gridSize = super.getGridSize();
		populateAntCells("OBSTACLE",Color.BLACK ,obstacleCoords, obstacleCells);
		populateAntCells("NEST",Color.BROWN,nestCoords,nestCells);
		populateAnts(nestCells,startAnts);
		populateAntCells("FOOD",Color.BLUE,foodCoords,foodCells);
		populateEmpty();
		cellList = super.getCellList();
		return super.getMyScene();
	}
	
	private void populateAntCells(String state, Color color, ArrayList<int[]> coordinates, ArrayList<AntCell> cellList){
		for(int[] coord: coordinates){
			myCells[coord[0]][coord[1]] = new AntCell(state,color,coord[0],coord[1],antCap);
			cellList.add((AntCell) myCells[coord[0]][coord[1]]);
		}
	}
	
	private void populateEmpty(){
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				GridCell cell = myCells[x][y];
				if(cell==null){
					cell = new AntCell("GROUND",Color.GREEN,x,y,antCap);
				}
			}
		}
	}
	
	private void populateAnts(ArrayList<AntCell> selections, int amt){
		Random rnd = new Random();
		int i = amt;
		while(i>0){
			AntCell chosen = selections.get(rnd.nextInt(selections.size()));
			if(!chosen.atCapacity()){
				Ant ant = new Ant(dieThresh,chosen.getX(),chosen.getY());
				chosen.addAnimal(ant);
				ants.add(ant);
				i--;
			}
		}
	}
	
	
	@Override
	public void update() {
		for(Ant ant: ants){
			String cellState = myCells[ant.getX()][ant.getY()].getState();
			if(ant.hasFoodItem()){
				returnToNest(ant);
			}
			else if (cellState=="NEST"){
				findFoodSource(ant);
			}
		}
		
		
	}
	
	private void returnToNest(Ant ant){
		ArrayList<GridCell> forwardNeighbors = getForwardNeighbors(ant.getX(),ant.getY());
		ArrayList<GridCell> backwardNeighbors = getBackwardNeighbors(ant.getX(),ant.getY());
		if(myCells[ant.getX()][ant.getY()].getState()=="FOOD"){
			//ant.setOrientation(NEWORIENTATION);
		}
		AnimalCell nextLoc = getMaxNeighbor(forwardNeighbors,"NEST");
		if(nextLoc==null){
			nextLoc = getMaxNeighbor(backwardNeighbors,"NEST");
		}
		if(nextLoc!=null){
			dropPheromones(ant,"FOOD");
			//ant.setOrientation(NEWORIENTATION);
			nextLoc.addAnimal(ant);
			((AntCell)myCells[ant.getX()][ant.getY()]).removeAnimal(ant);
			if(nextLoc.getState()=="NEST"){
				ant.dropFood();
			}
		}
		
	}
	
	private void findFoodSource(Ant ant){
		ArrayList<GridCell> forwardNeighbors = getForwardNeighbors(ant.getX(),ant.getY());
		ArrayList<GridCell> backwardNeighbors = getBackwardNeighbors(ant.getX(),ant.getY());
		if(myCells[ant.getX()][ant.getY()].getState()=="NEST"){
			//ant.setOrientation(NEWORIENTATION);
		}
		AntCell nextLoc = selectLocation(forwardNeighbors);
		if(nextLoc==null){
			nextLoc = selectLocation(backwardNeighbors);
		}
		if(nextLoc!=null){
			dropPheromones(ant, "NEST");
			//ant.setOrientation(NEWORIENTATION);
			nextLoc.addAnimal(ant);
			((AntCell)myCells[ant.getX()][ant.getY()]).removeAnimal(ant);
			if(nextLoc.getState()=="NEST"){
				ant.dropFood();
			}
		}
		
	}
	
	private AntCell selectLocation(ArrayList<GridCell> neighbors){
		ArrayList<GridCell> refinedNeighbors = neighbors;
		for(GridCell cell: neighbors){
			AntCell antCell = (AntCell) cell;
			if(antCell.getState()=="OBSTACLE" || antCell.atCapacity()){
				refinedNeighbors.remove(cell);
			}
		}
		if(refinedNeighbors==null){
			return null;
		}
		else{
			//choose location with probability 
		}
	}
	
	private void dropPheromones(Ant ant, String pherKind){
		AntCell currCell = (AntCell) myCells[ant.getX()][ant.getY()];
		if(currCell.getState()==pherKind){
			currCell.setPher(pherCap, pherKind);
		}
		else{
			AntCell maxCell = getMaxNeighbor(getAllNeighbors(ant.getX(),ant.getY()),"FOOD");
			double maxPher = maxCell.getPher(pherKind);
			double result = maxPher - 2 - currCell.getPher(pherKind); //constant
			if(result>0){
				currCell.setPher(currCell.getPher(pherKind)+result, pherKind);
			}
		}
	}
	
	private AntCell getMaxNeighbor(ArrayList<GridCell> selection,String pherKind){
		double maxNum=0;
		AntCell maxCell = null;
		for(GridCell cell: selection){
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
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				GridCell cell = myCells[x][y];
				if(cell.getState()=="NEST"){
					cell.setMyColor(Color.BROWN);
				}
				else if(cell.getState()=="FOOD"){
					cell.setMyColor(Color.BLUE);
				}
				else{
					cell.setMyColor(cell.getMyColor());
				}
			}
		}
	}

}
