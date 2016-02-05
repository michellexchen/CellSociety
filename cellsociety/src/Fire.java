import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Fire extends Simulation {
	private final static double probCatch = .6;
	private int gridSize;
	private Scene myScene;
	
	public Fire(int size, int numCells) {

	}
	
	public Scene init(int size, int numCells){
		super.init(size,numCells);
		myCells = super.getCells();
		gridSize = super.getGridSize();
		//fill
		for (int a = 0; a<gridSize; a++) { //row
			for (int b = 0; b<gridSize; b++) {
				if (a == (gridSize-1)/2&& b == (gridSize-1)/2) {
					myCells[a][b] = new GridCell("BURNING", Color.RED,a,b);
				}
				else if (a == 0 || b == 0 || a == gridSize-1 || b == gridSize-1){
					myCells[a][b] = new GridCell("EMPTY", Color.YELLOW,a,b);
				}
				else {
					myCells[a][b] = new GridCell("TREE", Color.GREEN,a,b);
				}
			}
		}
		
		initGridCells();		
		return super.getMyScene();
	}
		
	@Override
	public void update() {
		//go through entire grid 
			//if empty: next state = empty
			//if burning: next state = burning
			//if tree: if neighbor burning: if random number between 0 and 1 < probCatch
						//return burning, else tree
					//if neighbor tree: next state = tree
		for (int x = 0; x<gridSize; x++) { //assigning next state
			for (int y = 0; y<gridSize; y++) {
				GridCell curr = myCells[x][y];
				String currState = curr.getState();
				if (currState == "EMPTY"){
					curr.setNextState(currState);
				}
				if (currState == "BURNING") {
					curr.setNextState("EMPTY");
				}
				if (currState == "TREE") {
					ArrayList<GridCell> neighbors = getCardinalNeighbors(x,y);
					ArrayList<String> blah = new ArrayList<String>();
					for (GridCell cell: neighbors){
						blah.add(cell.getState());
					}
					if (blah.contains("BURNING")) {
						if (Math.random()<probCatch) {
							curr.setNextState("BURNING");
						}
						else {
							curr.setNextState(currState);
						}
					}
					else {
						curr.setNextState(currState);
					}						
					
				}
			}
		}		
		updateStates();		
	}

	@Override
	public void updateColors() {
		for(int c = 0; c<super.getGridSize(); c++) {
			for(int d = 0; d<super.getGridSize(); d++) {
				GridCell current = myCells[c][d]; 
				if (current.getState().equals("EMPTY")) { //is it getnextState??? or getState?? confused bc of update call
					current.setMyColor(Color.YELLOW);
				}
				else if (current.getState().equals("TREE")){
					current.setMyColor(Color.GREEN);
				}
				else if (current.getState().equals("BURNING")){
					current.setMyColor(Color.RED);
				}
			}
		}
		
	}


}