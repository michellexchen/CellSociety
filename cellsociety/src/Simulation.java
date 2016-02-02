import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

public abstract class Simulation {

	private GridCell[][] myGridCells;  //Every array in myGridCells is one COLUMN of GridCells
	private Group root = new Group();
	private Scene myScene;
	
	private int GridCellSize;
	private int gridSize;
	
	public abstract void update(); //Calculates the NEW state for every GridCell, then sets current state to new state and new state to null, 
	public abstract void updateColors(); //Changes the colors of GridCells based on their new state
	
	public Scene init(int size, int numGridCells ){
		
		GridCellSize = size/numGridCells;
		gridSize = numGridCells;
		myGridCells = new GridCell[gridSize][gridSize];
		
		myScene = new Scene(root);
		
		return myScene; 
	}
	
	public int getGridSize(){
		return gridSize;
	}
	
	public void initGridCells(){
		int top = 0;
		int left = 0;
		
		for(GridCell[] c: myGridCells){
			for(GridCell d: c){
				Rectangle temp = d.getMySquare();
				temp.setX(left);
				temp.setY(top);
				temp.setWidth(GridCellSize);
				temp.setHeight(GridCellSize);
				temp.setFill(d.getMyColor());
				root.getChildren().add(temp);
				top += GridCellSize;
			}
			
			left += GridCellSize;
		}
		
	}
	
	public GridCell[][] getGridCells(){
		return myGridCells;
	}
	
	public void step(){
		update();
		updateColors();
		
		for(GridCell[] c: myGridCells){
			for(GridCell d: c){
				Rectangle temp = d.getMySquare();
				temp.setFill(d.getMyColor());	
			}			
		}	
	}
	
	public ArrayList<GridCell> getCardinalNeighbors(int x, int y){
		
		ArrayList<GridCell> result = new ArrayList<GridCell>();
		if(x > 0){
			result.add(myGridCells[x-1][y]); //LEFT
		}
		if(x < gridSize -1){
			result.add(myGridCells[x+1][y]);//RIGHT
		}
		if(y > 0){
			result.add(myGridCells[x][y-1]);//TOP
		}
		if(y < gridSize -1){
			result.add(myGridCells[x][y+1]);
		}
		
		return result;
	}
	
	public ArrayList<GridCell> getAllNeighbors(int x, int y){
		ArrayList<GridCell> result = getCardinalNeighbors(x,y);
		
		if(x > 0 && y > 0){
			result.add(myGridCells[x-1][y-1]); //top left
		}
		if(x < gridSize-1 && y > 0){
			result.add(myGridCells[x+1][y-1]); //top right
		}
		if(x < gridSize-1 && y < gridSize-1){
			result.add(myGridCells[x+1][y+1]); //bottom left
		}
		if(x>0 && y < gridSize-1){
			result.add(myGridCells[x-1][y+1]); //bottom right
		}
		
		return result;
	}
}
