//TODOS: 
//Error - All cells are being updated to empty

import java.util.*;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Segregation extends Simulation {
	private static final String TITLE = "Segregation";
	private int myPopulation;
	private double percentGroup1;
	private double percentGroup2;
	private double myThreshold;
	private GridCell[][] myGrid;
	private ArrayList<GridCell> emptyCells;
	private ArrayList<GridCell> nextEmpty = new ArrayList<GridCell>();
	
	public Segregation(int size, int numCells, int population, double group1, double group2, double threshold) {
		super(TITLE,size,numCells);
		myGrid = getCells();
		myPopulation = population;
		percentGroup1 = group1;
		percentGroup2 = group2;
		myThreshold = threshold;
		emptyCells = getEmptyCells();
	}
	
	@Override
	public Scene init(){
		super.init();

		randomInit(myGrid, myPopulation, percentGroup1, percentGroup2, "GROUP1", "GROUP2",Color.RED, Color.BLUE, Color.GRAY); //use constants, not these magic strings
		initGridCells();
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
					if(numSame/(numSame+numDiff)<myThreshold){ //if not satisfied and can move, move
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
	
