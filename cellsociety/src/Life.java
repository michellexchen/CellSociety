
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Life extends Simulation {
	private static final String TITLE = "Game of Life";
	private final String DEAD = "DEAD";
	private final String ALIVE = "ALIVE";
	private final Color DEADCOLOR = Color.GRAY;
	private final Color ALIVECOLOR = Color.BLUE;
	private int cellsAlive;
	
	public Life(int size, int numCells, int numAlive) {
		super(TITLE, size, numCells);
		cellsAlive = numAlive;
	}
	
	@Override
	public Scene init(){ //Initializes grid and scene. Currently assigns live cells to random locations. 
		
		super.init();
		super.randomInit(cellsAlive, 1, ALIVE, DEAD, DEAD, ALIVECOLOR, DEADCOLOR, DEADCOLOR); 
		super.initEmpty(DEAD, DEADCOLOR);
		super.initGridCells();
		return super.getMyScene();
	}
	
	@Override
	public void update() { //Updates state of grid cells
		for(GridCell[] c: getCells()){
			for(GridCell d: c){
				
				ArrayList<GridCell> neighbors = this.getAllNeighbors(d.getX(), d.getY());
				int liveNeighbors = 0;
				for(GridCell temp: neighbors){
					if(temp.getState() == ALIVE){
						liveNeighbors++;
					}
				}
				if(d.getState() == ALIVE){
					if(liveNeighbors == 2 || liveNeighbors == 3){
						d.setNextState(ALIVE);
					}
					else{
						d.setNextState(DEAD);
					}
				}
				else{
					if(liveNeighbors == 3){
						d.setNextState(ALIVE);
					}
					else{
						d.setNextState(DEAD);
					}
				}
								
			}
		}
		updateStates();
	}

	@Override
	public void updateColors() { //updates colors in grid to reflect changes in state
		for(GridCell[] c: getCells()){
			for(GridCell d: c){
				if(d.getState() == ALIVE){
					d.setMyColor(ALIVECOLOR);
				}
				else{
					d.setMyColor(DEADCOLOR);
				}
			}
		}
	}
}
