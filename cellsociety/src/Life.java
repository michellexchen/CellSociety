
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.paint.Color;
/**
 * This class represents the Game of Life simulation
 * The class populates a grid with Live cells by randomly allocating a user-determined number of live cells
 * It updates the state of cells based on the rules of the simulation
 * Passes the states of cells to the Simulation superclass, which displays the states visually
 * @author Saumya Jain, Collette Torres, Michelle Chen
 *
 */

public class Life extends Simulation {
	
	private static ResourceBundle myResources = ResourceBundle.getBundle("Resources/English");
	private static final String TITLE = myResources.getString("Life");
	private final String DEAD = "DEAD";
	private final String ALIVE = "ALIVE";
	private final Color DEADCOLOR = Color.GRAY;
	private final Color ALIVECOLOR = Color.BLUE;
	private int cellsAlive;
	private int numAlive;
	
	/**
	 * Randomly allocates alive cells through the grid
	 * Initializes non-alive cells to dead
	 * @param Size of Scene
	 * @param Number of cells
	 * @param Number of cells with state alive at beginning
	 */

	public Life(int size, int numCells, int alive, boolean tor, boolean tri) {
		super(TITLE, size, numCells, tor, tri);
		cellsAlive = alive;
		numAlive = alive;
		initialize();
		initChart();
	}
	
	public Life(List<String> columns, int size, boolean tor, boolean tri){
		super(columns, TITLE, size, tor, tri);
		initChart();
	}
	
	/**
	 * Initializes grid and scene. Currently assigns live cells to random locations.
	 */
	public void initialize(){  
		
		super.init();
		super.randomInit(cellsAlive, 1, ALIVE, DEAD, DEAD, ALIVECOLOR, DEADCOLOR, DEADCOLOR); 
		super.initEmpty(DEAD, DEADCOLOR);
		super.displayGrid();
	}
	
	@Override
	public void initExplicit(char current, int col, int row) {
		
		if(current == '1'){
			super.getCells()[col][row] = new GridCell(ALIVE, Color.BLUE, col, row);
			numAlive++;
		}
		else if(current == '0'){
			super.getCells()[col][row] = new GridCell(DEAD, Color.GRAY, col, row);
		}
	}	
	
	/**
	 * Updates state of grid cells
	 * Sets live cells to dead and dead cells to alive based on states of their neighbors
	 */
	@Override
	public void update() { 
		for(GridCell[] c: getCells()){
			for(GridCell d: c){
				
				List<GridCell> neighbors = d.getAllNeighbors();
				int liveNeighbors = 0;
				for(GridCell temp: neighbors){
					if(temp.getState() == ALIVE){
						liveNeighbors++;
					}
				}
				if(d.getState() == ALIVE){
					if(liveNeighbors == 2 || liveNeighbors == 3){
						d.setNextState(ALIVE);
						d.setNextColor(ALIVECOLOR);
					}
					else{
						d.setNextState(DEAD);
						d.setNextColor(DEADCOLOR);
						numAlive--;

					}
				}
				else{
					if(liveNeighbors == 3){
						d.setNextState(ALIVE);
						d.setNextColor(ALIVECOLOR);
						numAlive++;
					}
					else{
						d.setNextState(DEAD);
						d.setNextColor(DEADCOLOR);
					}
				}
								
			}
		}
		updateStates();
	}
	@Override
	public List<Integer> getDataVals() {
		ArrayList<Integer> dataVals = new ArrayList<Integer>();
		dataVals.add(numAlive);
		return dataVals;
	}
	@Override
	public List<String> getDataLabels() {
		ArrayList<String> dataLabels = new ArrayList<String>();
		dataLabels.add("Alive");
		return dataLabels;
	}
	
}
