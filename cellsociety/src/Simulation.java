
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
	public abstract void update(); 
	
	/** 
	 * This method is generally responsible for setting the color that is to be displayed to reflect the current state of a cell
	 */
	public abstract void updateColors(); 
	
	/**
	 * This method is responsible for creating a simulation with a title, specific grid and cell dimension
	 * @param title the title of the simulation
	 * @param size the dimension for the size of the grid
	 * @param numGridCells the cell dimension of the grid (assuming a square grid)
	 */
	public Simulation(String title, int size, int numGridCells){
		sceneSize = size;
		gridSize = numGridCells;
		gridCellSize = (int)((double)(size)/(double)(numGridCells));
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
	
	/**
	 * This method is responsible for initializing the 2-D grid of cells and creating the scene for this grid
	 * @return the scene on which the simulation grid is displayed 
	 */
	public Scene init(){
		myCells = new GridCell[gridSize][gridSize];
		myScene = new Scene(root,sceneSize,sceneSize);
		return myScene; 
	}
	
	/**
	 * This method creates a list of random coordinates based on a user-determined amount
	 * @param population the amount of random coordinates needed
	 * @return a list of a specified amount of random coordinates 
	 */
	private ArrayList<int[]> getRandomCoordinates(int population){
		HashMap<Integer,ArrayList<Integer>> randCoords = new HashMap<Integer,ArrayList<Integer>>();  
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
	
	/**
	 * This method populates the 2-D array representing the simulation grid at certain coordinates
	 * @param coordinates the list of coordinates to be populated
	 * @param populationType1 the population for the first type of cell to be populated (implies population of second because the second's population is equal to the amount of remaining coordinates after the first type has been populated)
	 * @param type1 the label for the first type of cell to be populated 
	 * @param type2 the label for the second type of cell to be populated
	 * @param color1 the color for the first type of cell to be populated
	 * @param color2 the color for the second type of cell to be populated
	 */
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
	 * This method initializes the appearance and state of all of the empty cells in the grid
	 * @param defaultState the default state of cells in the grid not populated
	 * @param defaultColor the default color of cells in the grid not populated 
	 */
	public void initEmpty(String defaultState, Color defaultColor){		
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
	
	/**
	 * This method randomly populates a grid with cells of two types as well as empty cells based on the given parameters
	 * @param population the total population of non-empty cells to be populated
	 * @param percent1 the percent of the population to be populated as the first type 
	 * @param type1 the label of the first type of cell to be populated
	 * @param type2 the label of the second type of cell to be populated
	 * @param defaultType the label of the default type of cell (typically empty)
	 * @param color1 the color of the first type of cell to be populated
	 * @param color2 the color of the second type of cell to be populated
	 * @param defaultColor the color of the second type of cell to be populated 
	 */
	public void randomInit(int population, double percent1, String type1, String type2, String defaultType, Color color1, Color color2, Color defaultColor){
		ArrayList<int[]> coordinates = getRandomCoordinates(population);
		populateGrid(coordinates,(int)(population*percent1),type1,type2,color1,color2);
		initEmpty(defaultType, defaultColor);
	}
	
	/**
	 * This method returns the cell dimension of the grid 
	 * @return the cell dimension of the grid 
	 */
	public int getGridSize(){
		return gridSize;
	}
	
	/**
	 * This method returns the root node of the simulation scene 
	 * @return the root node of the simulation scene
	 */
	public Group getRoot(){
		return root;
	}
	
	/**
	 * This method returns the unpopulated cells in the grid
	 * @return a list of unpopulated cells in the grid 
	 */
	public ArrayList<GridCell> getEmptyCells(){
		return emptyCells;
	}
	
	/**
	 * This method initializes the cells to be displayed such that they appear on the grid as colored squares 
	 */
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

	/**
	 * This method updates the current states of each cell to the next determined state of each cell
	 */
	public void updateStates(){
		for (int m = 0; m<gridSize; m++) {
			for (int n = 0; n<gridSize; n++) {
				GridCell curr = myCells[m][n];
				curr.setState(curr.getNextState());
				curr.setNextState(null);
			}
		}
	}
	
	/**
	 * This method returns the 2-D array representing the simulation's grid 
	 * @return
	 */
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
	
	/**
	 * This method returns the neighbors to the North, South, East, and West of the cell at the specified coordinates 
	 * @param x the x-coordinate of the cell
	 * @param y the y-coordinate of the cell
	 * @return a list of cells neighboring a specified cell in the cardinal directions
	 */
	public ArrayList<GridCell> getCardinalNeighbors(int x, int y){
		ArrayList<GridCell> result = new ArrayList<GridCell>();
		if(x > 0){
			result.add(myCells[x-1][y]); 
		}
		if(x < gridSize -1){
			result.add(myCells[x+1][y]);
		}
		if(y > 0){
			result.add(myCells[x][y-1]);
		}
		if(y < gridSize -1){
			result.add(myCells[x][y+1]);
		}
		return result;
	}
	
	/**
	 * This method returns all the neighbors surrounding a specified cell 
	 * @param x the x-coordinate of the cell
	 * @param y the y-coordinate of the cell
	 * @return a list of all the cells surrounding a specified cell 
	 */
	public ArrayList<GridCell> getAllNeighbors(int x, int y){
		ArrayList<GridCell> result = getCardinalNeighbors(x,y);

		if(x > 0 && y > 0){
			result.add(myCells[x-1][y-1]); 
		}
		if(x < gridSize-1 && y > 0){
			result.add(myCells[x+1][y-1]); 
		}
		if(x < gridSize-1 && y < gridSize-1){
			result.add(myCells[x+1][y+1]); 
		}
		if(x>0 && y < gridSize-1){
			result.add(myCells[x-1][y+1]); 
		}
		
		return result;
	}
	
	/**
	 * This method returns the scene of the simulation
	 * @return the simulation scene
	 */
	public Scene getMyScene() {
		return myScene;
	}
}
