import java.util.*;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Simulation {
	//made gridcell public
	private GridCell[][] myCells;  //Every array in myCells is one COLUMN of cells
	private Group root = new Group();
	private Scene myScene;
	private String myTitle;

	private int sceneSize;
	private int gridCellSize;
	private int gridSize;
	
	private ArrayList<GridCell> emptyCells = new ArrayList<GridCell>();
	
	public abstract void update(); //Calculates the NEW state for every cell, then sets current state to new state and new state to null, 
	public abstract void updateColors(); //Changes the colors of cells based on their new state
	
	public Simulation(String title, int size, int numGridCells){
		sceneSize = size;
		gridSize = numGridCells;
		gridCellSize = size/numGridCells;
		myTitle = title;
	}
	
	public String getTitle(){
		return myTitle;
	}
	
	public int getSceneSize(){
		return sceneSize;
	}
	
	public Scene init(){
		myCells = new GridCell[gridSize][gridSize];
		myScene = new Scene(root,sceneSize,sceneSize);
		
		return myScene; 
	}
	
	private ArrayList<int[]> getRandomCoordinates(int population){
		HashMap<Integer,ArrayList<Integer>> randCoords = new HashMap<Integer,ArrayList<Integer>>();  //Maps a column integer (x) to a list of rows (y)
		Random rnd = new Random();
		while(population>0){
			int x = rnd.nextInt(gridSize);
			int y = rnd.nextInt(gridSize);
			
			if(randCoords.keySet().contains(x)){
				if(!randCoords.get(x).contains(y)){
					randCoords.get(x).add(y);
					population--;
				}
			}
			else{			
				ArrayList<Integer> ycoords = new ArrayList<Integer>();
				ycoords.add(y);
				randCoords.put(x, ycoords);
				population--;
			}	
		}
		
		//creating list of coordinate pairs to select from below (because too complicated to just get pair when the y coordinates are in a list)
		ArrayList<int[]> coordList = new ArrayList<int[]>();
		for(int x: randCoords.keySet()){
			for(int y: randCoords.get(x)){
				int[] coord = {x,y};
				coordList.add(coord);
			}
		}

		Collections.shuffle(coordList);
		return coordList;
	}
	
	private void populateGrid(ArrayList<int[]> coordinates, int populationType1, String type1, String type2, Color color1, Color color2){
		for(int i=0; i<coordinates.size(); i++){ 
			int[] coord = coordinates.get(i);
			int x = coord[0];
			int y = coord[1];
			
			if(populationType1>0){  
				GridCell temp = new GridCell(type1, color1, x, y);
				myCells[x][y] = temp;
				populationType1--;
				continue;
			}
			
			 GridCell temp= new GridCell(type2, color2, x, y);

			 myCells[x][y] = temp;
		}
	}
	
	public void initEmpty(String defaultState, Color defaultColor){		//Any cells that haven't been initialized are set to some default state and colr eg. empty/gray
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(getCells()[x][y] == null){	
					GridCell c = new GridCell(defaultState, defaultColor, x, y);
					myCells[x][y] = c;
					emptyCells.add(c);
				}	
			}
		}
	}
	
	public void randomInit(int population, double percent1, String type1, String type2, String defaultType, Color color1, Color color2, Color defaultColor){
		ArrayList<int[]> coordinates = getRandomCoordinates(population);
		populateGrid(coordinates,(int)(population*percent1),type1,type2,color1,color2);
		initEmpty(defaultType, defaultColor);
	}
	
	public int getGridSize(){
		return gridSize;
	}
	
	public Group getRoot(){
		return root;
	}
	
	public ArrayList<GridCell> getEmptyCells(){
		return emptyCells;
	}
	
	public void initGridCells(){
		int top = 0;
		int left = 0;
		
		for(int i = 0; i < this.gridSize; i++){
			for(int j = 0; j < this.gridSize; j++){
				GridCell d = myCells[i][j];
				Rectangle temp = new Rectangle(left, top, gridCellSize, gridCellSize);
				temp.setFill(d.getMyColor());
				d.setMySquare(temp);
				root.getChildren().add(temp);
				top += gridCellSize;
			}
			top = 0;
			left += gridCellSize;
		}	
	}

	public void updateStates(){
		for (int m = 0; m<gridSize; m++) {
			for (int n = 0; n<gridSize; n++) {
				GridCell curr = myCells[m][n];
				curr.setState(curr.getNextState());
				curr.setNextState(null);
			}
		}
	}
	
	public GridCell[][] getCells(){
		return myCells;
	}
	
	public void step(){
		update();
		updateColors();

		for(GridCell[] c: myCells){
			for(GridCell d: c){
				Rectangle temp = d.getMySquare();
				temp.setFill(d.getMyColor());	
			}			
		}	
	}
	
	public ArrayList<GridCell> getCardinalNeighbors(int x, int y){
		ArrayList<GridCell> result = new ArrayList<GridCell>();
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
	
	public ArrayList<GridCell> getAllNeighbors(int x, int y){
		ArrayList<GridCell> result = getCardinalNeighbors(x,y);

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
	public Scene getMyScene() {
		return myScene;
	}
	public void setMyScene(Scene myScene) {
		this.myScene = myScene;
	}
}
