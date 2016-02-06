import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Life extends Simulation {
	private static final String TITLE = "Game of Life";
	private final String DEAD = "DEAD";
	private final String ALIVE = "ALIVE";
	private final Color DEADCOLOR = Color.GRAY;
	private final Color ALIVECOLOR = Color.BLUE;
	private int[][] liveCells = {{3,1}, {3,2},{3,3}, {2,3}, {1,2}};
	
	public Life(int size, int numCells, int numAlive) {
		super(TITLE, size, numCells);
	}
	
	@Override
	public Scene init(){
		
		super.init();
		for(int[] c: liveCells){
			int x = c[0];
			int y = c[1];
			GridCell temp = new GridCell(ALIVE, ALIVECOLOR, x, y);
			getCells()[x][y] = temp;
		}
		
		super.initEmpty(DEAD, DEADCOLOR);
		super.initGridCells();
		return super.getMyScene();
	}
	
	@Override
	public void update() {
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
	public void updateColors() {
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
