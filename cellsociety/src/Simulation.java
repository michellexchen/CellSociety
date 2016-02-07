
import java.util.*;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class is responsible for all of the functionality and properties of a simulation.
 * It houses the 2-D array containing all of the cells that are displayed for the simulation
 * and is this responsible for big-picture handling of simulations as manifested through this matrix, 
 * such as getting them to display on a screen. 
 * The class contains methods useful to simulations of varying types, such as getting the neighbors
 * of a particular cell in the simulation grid, updating the states of all cells on the grid, etc.  
 * @author colettetorres
 * @author saumyajain
 * @author michellechen
 *
 */
public abstract class Simulation {
	private GridCell[][] myCells;  
	private Group root = new Group();
	private Scene myScene;
	private String myTitle;

	private int sceneSize;
	private int gridCellSize;
	private int gridSize;
	
	private ArrayList<GridCell> emptyCells = new ArrayList<GridCell>();
	
	/**
	 * This method is generally responsible for determining the next state for each cell based on certain parameters, as defined by each type of simulation 
	 */
	public abstract void update(); //Calculates the NEW state for every cell, then sets current state to new state and new state to null, 
	
	/** 
	 * This method is generally responsible for setting the color that is to be displayed to reflect the current state of a cell
	 */
	public abstract void updateColors(); //Changes the colors of cells based on their new state
	
	/**
	 * This method is responsible for creating a simulation with a title, specific grid and cell dimension
	 * @param title the title of the simulation
	 * @param size the size of the grid
	 * @param numGridCells the cell dimension of the grid (assuming a square grid)
	 */
	public Simulation(String title, int size, int numGridCells){
		sceneSize = size;
		gridSize = numGridCells;
		gridCellSize = size/numGridCells;
		myTitle = title;
	}
	
	/**
	 * This method is responsible for retrieving the title of the simulation
	 * @return the title of the simulation
	 */
	public String getTitle(){
		return myTitle;
	}
	
	/**
	 * This method is responsible for retrieving the dimension of the simulation scene (assuming a square scene)
	 * @return the dimension of the simulation scene (in pixels)
	 */
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
	
	/**
	 * 
	 * @param defaultState
	 * @param defaultColor
	 */
	public void initEmpty(String defaultState, Color defaultColor){		//Any cells that haven't been initialized are set to some default state and color eg. empty/gray
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myCells[x][y] == null){	
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
