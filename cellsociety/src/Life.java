
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
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
	private static final String TITLE = "Game of Life";
	private final String DEAD = "DEAD";
	private final String ALIVE = "ALIVE";
	private final Color DEADCOLOR = Color.GRAY;
	private final Color ALIVECOLOR = Color.BLUE;
	private int cellsAlive;
	
	/**
	 * Randomly allocates alive cells through the grid
	 * Initializes non-alive cells to dead
	 * @param Size of Scene
	 * @param Number of cells
	 * @param Number of cells with state alive at beginning
	 */
	public Life(int size, int numCells, int numAlive, boolean toroidal, boolean triangular) {
		super(TITLE, size, numCells, toroidal, triangular);
		cellsAlive = numAlive;
	}
	/**
	 * Initializes grid and scene. Currently assigns live cells to random locations.
	 */
	@Override
	public Scene init(){  
		
		super.init();
		super.randomInit(cellsAlive, 1, ALIVE, DEAD, DEAD, ALIVECOLOR, DEADCOLOR, DEADCOLOR); 
		super.initEmpty(DEAD, DEADCOLOR);
		super.displayGrid();
		return super.getMyScene();
	}
	
	@Override
	public Scene init(String[] cols){
		super.init();
		for(int i = 0; i < cols.length; i++){
			for(int j = 0; j < cols.length; j++){
				if(cols[i].charAt(j) == '1'){
					super.getCells()[i][j] = new GridCell(ALIVE, ALIVECOLOR, i, j);
				}
				else{
					super.getCells()[i][j] = new GridCell(DEAD, DEADCOLOR, i, j);

				}
			}
		}
		
		super.displayGrid();
		return super.getMyScene();
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
					}
				}
				else{
					if(liveNeighbors == 3){
						d.setNextState(ALIVE);
						d.setNextColor(ALIVECOLOR);
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
	
}
