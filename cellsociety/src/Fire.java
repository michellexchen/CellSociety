import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Fire extends Simulation {
	private final static double probCatch = .2;
	private int gridSize;
	private Scene myScene;
	private GridCell[][] myGrid;
	
	public Fire() {
		// TODO Auto-generated constructor stub
		gridSize = 10;
		myGrid = getCells();
	}
	
	public Scene init(){
		myScene = super.init(gridSize,gridSize);
		//fill
		for (int a = 0; a<gridSize; a++) { //row
			for (int b = 0; b<gridSize; b++) {
				if (a == (gridSize-1)/2&& b == (gridSize-1)/2) {
					myGrid[a][b] = new GridCell("BURNING");
				}
				if (a == 0 || b == 0 || a == gridSize-1 || b == gridSize-1){
					myGrid[a][b] = new GridCell("EMPTY");
				}
				myGrid[a][b] = new GridCell("TREE");
			}
		}
		
		initCells();
		
		return myScene;
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
				GridCell curr = myGrid[x][y];
				String currState = curr.getState();
				if (currState == "EMPTY"){
					curr.setNextState(currState);
				}
				if (currState == "BURNING") {
					curr.setNextState("EMPTY");
				}
				if (currState == "TREE") {
					ArrayList<GridCell> neighbors = getCardinalNeighbors(x,y);
					if (!neighbors.contains("BURNING")) {
						curr.setNextState(currState);
					}
					if (Math.random() < probCatch) {
						curr.setNextState("BURNING");
					}
					else {curr.setNextState(currState);}
				}
			}
		}		
		updateStates();		
	}

	@Override
	public void updateColors() {
		for(int c = 0; c<gridSize; c++) {
			for(int d = 0; d<gridSize; d++) {
				GridCell current = myGrid[c][d]; 
				if (current.getState().equals("EMPTY")) { //is it getnextState??? or getState?? confused bc of update call
					current.setMyColor(Color.YELLOW);
				}
				else if (current.getState().equals("TREE")){
					current.setMyColor(Color.GREEN);
				}
				current.setMyColor(Color.RED);
			}
		}
		
	}


}
