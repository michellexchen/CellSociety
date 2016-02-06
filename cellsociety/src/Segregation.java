//TODOS: 
//Error - All cells are being updated to empty

import java.util.*;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Segregation extends Simulation {
	private static final String TITLE = "Segregation";
	private static final String GROUP1 = "GROUP1";
	private static final String GROUP2 = "GROUP2";
	private static final String EMPTY = "EMPTY";
	private static final Color GROUP1COLOR = Color.RED;
	private static final Color GROUP2COLOR = Color.BLUE;
	private static final Color BACKGROUND = Color.GAINSBORO;
	private int myPopulation;
	private double percentGroup1;
	private double myThreshold;
	private GridCell[][] myCells;
	private int gridSize;
	private ArrayList<GridCell> emptyCells;
	private ArrayList<GridCell> nextEmpty = new ArrayList<GridCell>();
	private ArrayList<GridCell> allCells = new ArrayList<GridCell>();
	
	public Segregation(int size, int numCells, int population, double group1population, double threshold) {
		super(TITLE,size,numCells);
		myPopulation = population;
		percentGroup1 = group1population;
		myThreshold = threshold;
		gridSize = super.getGridSize();
	}
	
	@Override
	public Scene init(){
		super.init();
		myCells = super.getCells();
		randomInit(myPopulation, percentGroup1, GROUP1, GROUP2, EMPTY, GROUP1COLOR, GROUP2COLOR, BACKGROUND); 
		emptyCells = getEmptyCells();
		initGridCells();
		
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				GridCell cell = myCells[x][y];
				allCells.add(cell);
			}
		}
		
		return super.getMyScene();
	}
	
	@Override
	public void update(){
		for(GridCell e: emptyCells){
			e.setNextState(EMPTY);
		}
			Collections.shuffle(allCells);
			for(GridCell cell: allCells){
				String currState = cell.getState();
				if(currState != EMPTY)
				{
					if(isDissatisfied(cell)){ //if not satisfied and can move, move
						GridCell empty = getRandEmpty();
						if(empty != null){
							empty.setNextState(currState);
							cell.setNextState(EMPTY);
							nextEmpty.add(cell);
						}	
					}
					
					if(cell.getNextState()==null){ //if satisfied or can't move, do nothing
						cell.setNextState(currState);
					}
				}
			}
		
		emptyCells.addAll(nextEmpty);
		nextEmpty.clear();
		//setting current state to next state and clearing next state
		updateStates();
	}
	
	private boolean isDissatisfied(GridCell cell){
		ArrayList<GridCell> neighbors = getAllNeighbors(cell.getX(),cell.getY());
		double numSame = 0;
		double numDiff = 0;
		for(GridCell n: neighbors){
			if(cell.getState()==n.getState()){
				numSame++;
			}
			else if(n.getState() != EMPTY){
				numDiff++;
			}
		}
		
		return (numSame/(numSame+numDiff)<myThreshold);
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
				if(cell.getState()==GROUP1){
					cell.setMyColor(GROUP2COLOR);
				}
				else if(cell.getState()==GROUP2){
					cell.setMyColor(GROUP1COLOR);
				}
				else if(cell.getState() == EMPTY){
					cell.setMyColor(BACKGROUND);
				}
			}
		}
	}	
}		
	
