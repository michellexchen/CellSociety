import java.util.*;
import java.util.Random;

import javafx.scene.Scene;
import javafx.scene.paint.Color;


public class Segregation extends Simulation {
	private final static double THRESHOLD = 0.30;
	private final static int MY_POPULATION = 4;
	private final static double PERCENT_GROUP1 = 0.25;
	private final static double PERCENT_GROUP2 = 0.75;
	
	private int gridSize;
	private Scene myScene;
	private GridCell[][] myGrid;
	private ArrayList<GridCell> emptyCells;
	
	public Segregation() {
		// TODO Auto-generated constructor stub
		gridSize = 4;//getGridSize();
		myGrid = getCells();
	}
	
	public Scene init(){
		//the variables below need to be implemented w parameters from XML
		int population = MY_POPULATION; 
		int popGroup1 = (int)(population*PERCENT_GROUP1);
		int popGroup2 = (int)(population*PERCENT_GROUP2);
		
		myScene = super.init(gridSize,gridSize);
		
		//choosing random coordinates for however many cells need to be populated (as determined by user)
		HashMap<Integer,ArrayList<Integer>> randCoords = new HashMap<Integer,ArrayList<Integer>>(); 
		Random rnd = new Random();
		while(population>0){
			int x = rnd.nextInt(gridSize);
			int y = rnd.nextInt(gridSize);
			if(randCoords.keySet().contains(x)){
				if(!randCoords.get(x).contains(y)){
					randCoords.get(x).add(y);
					population--;
				}
			}
			ArrayList<Integer> ycoords = new ArrayList<Integer>();
			ycoords.add(y);
			randCoords.put(x, ycoords);
		}
		
		//creating list of coordinate pairs to select from below (because too complicated to just get pair when the y coordinates are in a list)
		ArrayList<int[]> coordList = new ArrayList<int[]>();
		for(int x: randCoords.keySet()){
			for(int y: randCoords.get(x)){
				int[] coord = {x,y};
				coordList.add(coord);
			}
		}

		//distributing random coordinate pairs among both groups 
		for(int i=0; i<coordList.size(); i++){ 
			int[] coord = coordList.get(i);
			int x = coord[0];
			int y = coord[1];
			
			if(popGroup1>0){  
				myGrid[x][y] = new GridCell("GROUP1");
				popGroup1--;
				continue;
			}
			myGrid[x][y] = new GridCell("GROUP2");
			
		}
		
		//populating the rest of the grid with empty cells
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myGrid[x][y]==null){
					GridCell c = new GridCell("EMPTY");
					myGrid[x][y] = c;
					emptyCells.add(c);
				}
			}
		}

		initCells();
		
		return myScene;
	}
	
	@Override
	public void update(){
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				GridCell cell = myGrid[x][y];
				String currState = cell.getState();
				if(currState=="EMPTY"){
					emptyCells.add(cell);
				}
				else{
					ArrayList<GridCell> neighbors = getAllNeighbors(x,y);
					int num = 0;
					for(GridCell n: neighbors){
						if(currState==n.getState()){
							num++;
						}
					}
					if(num/neighbors.size()<THRESHOLD){ 
						GridCell empty = getRandEmpty();
						empty.setNextState(currState);
					}
				}
				if(cell.getNextState()!=null){
					cell.setNextState(currState);
				}
			}
		}
		
		//setting current state to next state and clearing next state
		updateStates();
	}
	
	private GridCell getRandEmpty(){
		Random rnd = new Random();
		GridCell empty = emptyCells.get(rnd.nextInt(emptyCells.size()));
		emptyCells.remove(empty);
		return empty;
	}

	@Override
	public void updateColors() {
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				GridCell cell = myGrid[x][y];
				if(cell.getState()=="GROUP1"){
					cell.setMyColor(Color.BLUE);
				}
				else if(cell.getState()=="GROUP2"){
					cell.setMyColor(Color.RED);
				}
				else{
					cell.setMyColor(Color.WHITE);
				}
			}
		}
		
	}
	

}
