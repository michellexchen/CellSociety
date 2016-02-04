//TODOS: 
//Error - All cells are being updated to empty

import java.util.*;
import java.util.Random;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Segregation extends Simulation {
	
	private final static double THRESHOLD = 0.30;
	private final static int MY_POPULATION = 4;
	private final static double PERCENT_GROUP1 = 0.25;
	private final static double PERCENT_GROUP2 = 0.75;

	private ArrayList<GridCell> emptyCells = new ArrayList<GridCell>();
	
	@Override
	public Scene init(int size, int numGridCells){

		//the variables below need to be implemented w parameters from XML
		int population = MY_POPULATION; 
		int popGroup1 = (int)(population*PERCENT_GROUP1);
		int popGroup2 = (int)(population*PERCENT_GROUP2);
		
		super.init(size,numGridCells);
		
		//choosing random coordinates for however many cells need to be populated (as determined by user)
		HashMap<Integer,ArrayList<Integer>> randCoords = new HashMap<Integer,ArrayList<Integer>>();  //Maps a column integer (x) to a list of rows (y)
		Random rnd = new Random();
		while(population>0){
			int x = rnd.nextInt(super.getGridSize());
			int y = rnd.nextInt(super.getGridSize());
			if(randCoords.keySet().contains(x)){
				if(!randCoords.get(x).contains(y)){
					randCoords.get(x).add(y);
				}
			}
						
			ArrayList<Integer> ycoords = new ArrayList<Integer>();
			ycoords.add(y);
			randCoords.put(x, ycoords);
			population--;

		}
		
		//creating list of coordinate pairs to select from below (because too complicated to just get pair when the y coordinates are in a list)
		ArrayList<int[]> coordList = new ArrayList<int[]>();
		for(int x: randCoords.keySet()){
			for(int y: randCoords.get(x)){
				int[] coord = {x,y};
				coordList.add(coord);
			}
		}
		
		//populating the entire grid with empty cells at first
		for(int x=0; x<super.getGridSize(); x++){
			for(int y=0; y<super.getGridSize(); y++){
					GridCell c = new GridCell("EMPTY", Color.GRAY);
					getCells()[x][y] = c;
					emptyCells.add(c);
			}
		}

		//distributing random coordinate pairs among both groups 
		for(int i=0; i<coordList.size(); i++){ 
			int[] coord = coordList.get(i);
			int x = coord[0];
			int y = coord[1];
			
			if(popGroup1>0){  
				super.getCells()[x][y] = new GridCell("GROUP1", Color.BLUE);
				popGroup1--;
				continue;
			}
			getCells()[x][y] = new GridCell("GROUP2", Color.RED);
		}
		
		super.initGridCells();
		
		return super.getMyScene();
	}
	
	@Override
	public void update(){
		
		for(int x=0; x<super.getGridSize(); x++){
			for(int y=0; y<super.getGridSize(); y++){
				GridCell cell = getCells()[x][y];
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
						cell.setNextState("EMPTY");
					}
				}
				if(cell.getNextState()==null){
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

		for(int x=0; x<super.getGridSize(); x++){
			for(int y=0; y<super.getGridSize(); y++){
				GridCell cell = getCells()[x][y];
				if(cell.getState()=="GROUP1"){
					cell.setMyColor(Color.BLUE);
				}
				else if(cell.getState()=="GROUP2"){
					cell.setMyColor(Color.RED);
				}
				else{
					cell.setMyColor(Color.GRAY);
				}
			}
		}
	}	
}		
	
	
