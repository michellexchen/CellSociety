//TODOS: 
//Error - All cells are being updated to empty

import java.util.*;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Segregation extends Simulation {
	
	private final static double THRESHOLD = 0.7;
	private final static int MY_POPULATION = 5000;
	private final static double PERCENT_GROUP1 = 0.25;
	private final static double PERCENT_GROUP2 = 0.75;

	private ArrayList<GridCell> emptyCells = new ArrayList<GridCell>();
	private ArrayList<GridCell> nextEmpty = new ArrayList<GridCell>();
	
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
					population--;
				}
				continue;
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

		Collections.shuffle(coordList);
		//distributing random coordinate pairs among both groups 
		for(int i=0; i<coordList.size(); i++){ 
			int[] coord = coordList.get(i);
			int x = coord[0];
			int y = coord[1];
			
			if(popGroup1>0){  
				GridCell temp = new GridCell("GROUP1", Color.BLUE);
				super.getCells()[x][y] = temp;
				popGroup1--;
				continue;
			}
			 GridCell temp= new GridCell("GROUP2", Color.RED);
			 getCells()[x][y] = temp;
		}
		
		//populating the rest of the grid with empty cells
		for(int x=0; x<super.getGridSize(); x++){
			for(int y=0; y<super.getGridSize(); y++){
				if(getCells()[x][y] == null){	
					GridCell c = new GridCell("EMPTY", Color.GRAY);
					getCells()[x][y] = c;
					emptyCells.add(c);
				}	
			}
		}
		super.initGridCells();
		
		return super.getMyScene();
	}
	
	@Override
	public void update(){
		for(GridCell e: emptyCells){
			e.setNextState("EMPTY");
		}
		for(int x=0; x<super.getGridSize(); x++){
			for(int y=0; y<super.getGridSize(); y++){
				GridCell cell = getCells()[x][y];
				String currState = cell.getState();
				
				if(currState != "EMPTY")
				{
					ArrayList<GridCell> neighbors = getAllNeighbors(x,y);
					double numSame = 0;
					double numDiff = 0;
					for(GridCell n: neighbors){
						if(currState==n.getState()){
							numSame++;
						}
						else if(n.getState() != "EMPTY"){
							numDiff++;
						}
					}
					if(numSame/(numSame+numDiff)<THRESHOLD){ //if not satisfied and can move, move
						GridCell empty = getRandEmpty();
						if(empty != null){
							empty.setNextState(currState);
							cell.setNextState("EMPTY");
							nextEmpty.add(cell);
						}	
					}
					
					if(cell.getNextState()==null){ //if satisfied or can't move, do nothing
						cell.setNextState(currState);
					}
				}
			}
		}
		
		emptyCells.addAll(nextEmpty);
		nextEmpty.clear();
		//setting current state to next state and clearing next state
		updateStates();
	}
	
	private GridCell getRandEmpty(){
		Random rnd = new Random();
		if(emptyCells.size() == 0){
			return null;
		}
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
				else if(cell.getState() == "EMPTY"){
					cell.setMyColor(Color.GRAY);
				}
				
				else{
					System.out.println(cell.getState());
					cell.setMyColor(Color.WHITE);
				}
			}
		}
	}	
}		
	
	
