package cellsociety;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

public abstract class Simulation {

	private Cell[][] myCells;  //Every array in myCells is one COLUMN of cells
	private Group root = new Group();
	private Scene myScene;
	
	private int cellSize;
	private int gridSize;
	
	public abstract void update(); //Calculates the NEW state for every cell, then sets current state to new state and new state to null, 
	public abstract void updateColors(); //Changes the colors of cells based on their new state
	
	public Scene init(int size, int numCells ){
		
		cellSize = size/numCells;
		gridSize = numCells;
		myCells = new Cell[gridSize][gridSize];
		
		myScene = new Scene(root);
		initCells();
		
		return myScene; 
	}
	
	public void initCells(){
		int top = 0;
		int left = 0;
		
		for(Cell[] c: myCells){
			for(Cell d: c){
				Rectangle temp = d.getMySquare();
				temp.setX(left);
				temp.setY(top);
				temp.setWidth(cellSize);
				temp.setHeight(cellSize);
				temp.setFill(d.getMyColor());
				root.getChildren().add(temp);
				top += cellSize;
			}
			
			left += cellSize;
		}
		
	}
	
	public void step(){
		update();
		updateColors();
		
		for(Cell[] c: myCells){
			for(Cell d: c){
				Rectangle temp = d.getMySquare();
				temp.setFill(d.getMyColor());	
			}			
		}	
	}
	
	public ArrayList<Cell> getCardinalNeighbors(int x, int y){
		
		ArrayList<Cell> result = new ArrayList<Cell>();
		if(x > 0){
			result.add(myCells[x-1][y]); //LEFT
		}
		if(x < gridSize -1){
			result.add(myCells[x+1][y]);//RIGHT
		}
		if(y > 0){
			result.add(myCells[x][y-1]);//TOP
		}
		if(y < gridSize -1){
			result.add(myCells[x][y+1]);
		}
		
		return result;
	}
	
	public ArrayList<Cell> getAllNeighbors(int x, int y){
		ArrayList<Cell> result = getCardinalNeighbors(x,y);
		
		if(x > 0 && y > 0){
			result.add(myCells[x-1][y-1]); //top left
		}
		if(x < gridSize-1 && y > 0){
			result.add(myCells[x+1][y-1]); //top right
		}
		if(x < gridSize-1 && y < gridSize-1){
			result.add(myCells[x+1][y+1]); //bottom left
		}
		if(x>0 && y < gridSize-1){
			result.add(myCells[x-1][y+1]); //bottom right
		}
		
		return result;
	}
}
