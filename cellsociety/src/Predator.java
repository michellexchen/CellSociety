import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.paint.Color;
import java.util.*;
/**
 * 
 * @author Group 12: Saumya Jain, Colette Torres, Michelle Chen
 * This class creates, populates, and updates a Scene representing the Wa-Tor predator/prey simulation
 * The class populates a grid with fish, sharks, and empty space
 * It applies rules about movement, eating, dying, and breeding of sharks and fish as per the rules online
 * It passes this information to a superclass Simulation that updates a visual display 
 *
 */

public class Predator extends Simulation {
	private static final String TITLE = "WA-TOR";
	private static final String SHARK = "SHARK";
	private static final String FISH = "FISH";
	private static final String EMPTY = "EMPTY";
	private final Color FISHCOLOR = Color.GREEN;
	private final Color SHARKCOLOR = Color.YELLOW;
	private final Color BACKGROUND = Color.BLUE;
	private int gridSize;
	private int[][] breedGrid;
	private int[][] dieGrid;
	private int myPopulation;
	private double percentFish;
	private int sharkBreedTime;
	private int sharkDieTime;
	private int fishBreedTime;
	private List<GridCell> taken = new ArrayList<GridCell>();
	private List<GridCell> cellList = new ArrayList<GridCell>();
	private int numSharks;
	private int numFish;
	
	/**
	 * Initializes fields
	 * @param size Size of the Scene
	 * @param numCells Number of cells per row/column
	 * @param fishBreed Breeding time of fish
	 * @param sharkBreed Breeeding time of shark
	 * @param sharkDie Lifespan of a shark
	 * @param population Total number of sharks+fish
	 * @param fish Percentage of population that is fish
	 */

	public Predator(int size, int numCells, int fishBreed, int sharkBreed, int sharkDie, int population, double fish, boolean tor, boolean tri) { //Initializes simulation constants
		super(TITLE, size, numCells, tor, tri);
		myPopulation = population;
		percentFish = fish;
		sharkBreedTime = sharkBreed;
		sharkDieTime = sharkDie;
		fishBreedTime = fishBreed;
		initialize();
	}
	public Predator(List<String> columns, int size, int fishBreed, int sharkBreed, int sharkDie, boolean tor, boolean tri){
		super(columns, TITLE, size, tor, tri);
		sharkBreedTime = sharkBreed;
		sharkDieTime = sharkDie;
		fishBreedTime = fishBreed;
		
		gridSize = super.getGridSize();
		breedGrid = new int[gridSize][gridSize];
		dieGrid = new int[gridSize][gridSize];
		cellList = getCellList();
	}
	
	/**
	 * Assigns fish, sharks, and empty cells - returns a Scene with these attributes
	 */
	public void initialize(){ 
		super.init();
		gridSize = super.getGridSize();
		breedGrid = new int[gridSize][gridSize];
		dieGrid = new int[gridSize][gridSize];
		randomInit(myPopulation, percentFish, FISH, SHARK, EMPTY, FISHCOLOR, SHARKCOLOR, BACKGROUND);
		super.displayGrid();
		cellList = getCellList();
		initChart();
	}
	
	@Override
	public void initExplicit(char current, int col, int row) {
		if(current == '0'){
			super.getCells()[col][row] = new GridCell(EMPTY, Color.BLUE, col, row);
		}
		else if(current == '1'){
			super.getCells()[col][row] = new GridCell(FISH, Color.GREEN, col, row);
			numFish++;
		}
		else if(current == '2'){
			super.getCells()[col][row] = new GridCell(SHARK, Color.YELLOW, col, row);
			numSharks++;
		}
		
	}

	/**
	 * Updates state of cells
	 */
	@Override
	public void update() {
		Collections.shuffle(cellList);
		updateEmpty();
		updateSharks();
		updateFish();
		updateStates();
		taken.clear();
	}

	/**
	 * Sets default next state to empty
	 */
	private void updateEmpty(){ //Sets default next state to empty
		for(GridCell cell: cellList){
			if(cell.getState()==EMPTY){
				cell.setNextState(EMPTY);
				cell.setNextColor(BACKGROUND);

			}
		}

	}

	/**
	 * Applies rules for shark death/breeding/eating
	 * Sharks eat available fish
	 * Checks if fish and sharks have reached end of lifespan
	 * Checks if fish and sharks have reached breeding time
	 */
	private void updateSharks(){ 
		for(GridCell cell: cellList){
			int x = cell.getX();
			int y = cell.getY();
			if(cell.getState()==SHARK){
				List<GridCell> neighbors = cell.getCardinalNeighbors();
				List<GridCell> emptyNeighbors = getSpecialNeighbors(neighbors,EMPTY);
				List<GridCell> fishNeighbors = getSpecialNeighbors(neighbors,FISH);
				GridCell fish = getRandomCell(fishNeighbors);
				GridCell empty = getRandomCell(emptyNeighbors);
				if(fish!=null){
					eatFish(cell,fish,emptyNeighbors);
				}
				else if(dieGrid[x][y]+1==sharkDieTime){ 
					cell.setNextState(EMPTY);
					cell.setNextColor(BACKGROUND);
					numSharks--;
					breedGrid[x][y]=0;
					dieGrid[x][y]=0;
				}
				else if(empty!=null){ 
					moveShark(cell,empty);
				}
				else{ 
					cell.setNextState(SHARK);
					cell.setNextColor(SHARKCOLOR);
					breedGrid[x][y]=0;
					dieGrid[x][y]++;
				}
			}
		}
	}

	/**
	 * applies rules for fish movement and breeding
	 */
	private void updateFish(){ 
		for(GridCell cell: cellList){
			int x = cell.getX();
			int y = cell.getY();
			if(cell.getState()==FISH){
				List<GridCell> neighbors = cell.getCardinalNeighbors();
				List<GridCell> emptyNeighbors = getSpecialNeighbors(neighbors,EMPTY);
				GridCell empty = getRandomCell(emptyNeighbors);
				if(breedGrid[x][y]+1>=fishBreedTime){ 
					breedAnimal(cell,emptyNeighbors);
				}
				if(empty!=null){ 
					moveFish(cell,empty);
				}
				else{ 
					cell.setNextState(FISH);
					cell.setNextColor(FISHCOLOR);
					breedGrid[x][y]++;
				}
			}
		}
	}
	
	
	/**
	 * Changes cell states when sharks move
	 * @param shark A gridcell containing a shark
	 * @param empty An empty gridcell
	 */
	private void moveShark(GridCell shark, GridCell empty) {
		empty.setNextState(SHARK);
		empty.setNextColor(SHARKCOLOR);
		shark.setNextState(EMPTY);
		shark.setNextColor(BACKGROUND);
		breedGrid[empty.getX()][empty.getY()] = 0; //bc moving shark implies couldnt find fish so reset breed time
		dieGrid[empty.getX()][empty.getY()] = dieGrid[shark.getX()][shark.getY()]+1; //increment die time in next location bc couldnt find fish
		breedGrid[shark.getX()][shark.getY()] = 0; //clear breed and die time in current location
		dieGrid[shark.getX()][shark.getY()] = 0;
	}
	/**
	 * 
	 * @param shark A gridcell with a shark
	 * @param fish A gridcell with a fish
	 * @param emptyNeighbors A list of empty cells neighboring the shark
	 */
	private void eatFish(GridCell shark, GridCell fish, List<GridCell> emptyNeighbors) {
		numFish--;
		fish.setState(EMPTY); //so you don't look at it when iterating through fish
		fish.setNextState(SHARK);
		fish.setNextColor(SHARKCOLOR);
		shark.setNextState(EMPTY);
		shark.setNextColor(BACKGROUND);
		if(breedGrid[shark.getX()][shark.getY()]+1>=sharkBreedTime){ 
			breedAnimal(shark,emptyNeighbors);
		}
		else{
			breedGrid[shark.getX()][shark.getY()]++;
		}
		breedGrid[fish.getX()][fish.getY()] = breedGrid[shark.getX()][shark.getY()];
		breedGrid[shark.getX()][shark.getY()] = 0;
		dieGrid[shark.getX()][shark.getY()] = 0;
	}
	/**
	 * Controls fish movements
	 * @param fish Gridcell with a fish
	 * @param empty Empty gridcell
	 */
	private void moveFish(GridCell fish, GridCell empty){ 
		empty.setNextState(FISH);
		empty.setNextColor(FISHCOLOR);
		fish.setNextState(EMPTY);
		fish.setNextColor(BACKGROUND);
		breedGrid[empty.getX()][empty.getY()]=breedGrid[fish.getX()][fish.getY()]+1;
		breedGrid[fish.getX()][fish.getY()]=0;	
	}
	/**
	 * Adds a new fish or shark to an empty cell when a shark or fish breeds
	 * @param animal A gridcell containing a shark or fish
	 * @param emptyNeighbors A list of empty cells neighboring the animal cell
	 */
	private void breedAnimal(GridCell animal, List<GridCell> emptyNeighbors) { 
		GridCell newAnimal = getRandomCell(emptyNeighbors);
		if(newAnimal!=null){
			if(animal.getState()==SHARK){
				numSharks++;
			}
			else if(animal.getState()==FISH){
				numFish++;
			}
			newAnimal.setNextState(animal.getState());
			newAnimal.setNextColor(animal.getMyColor());
			breedGrid[animal.getX()][animal.getY()]=0;
		}
	}
	/**
	 * Returns a random gridcell from a list
	 * @param selections A list of cells
	 * @return A random grid cell
	 */
	private GridCell getRandomCell(List<GridCell> selections){ 
		Random rnd = new Random();
		while(selections.size()>0){
			GridCell chosen = selections.get(rnd.nextInt(selections.size()));
			if(!taken.contains(chosen)){
				taken.add(chosen);
				return chosen;
			}
			else{
				selections.remove(chosen);
			}
		}
		return null;
	}
	/**
	 * 
	 * @param neighbors A list of neighboring cells containing sharks or fish
	 * @param type A type of cell - fish or shark
	 * @return All neighbors of the type specified
	 */
	private List<GridCell> getSpecialNeighbors(List<GridCell> neighbors, String type){ //Returns all neighbors of a specific type
		List<GridCell> special = new ArrayList<GridCell>();
		for(GridCell n: neighbors){
			if(n.getState()==type){
				special.add(n);
			}
		}
		return special;
	}
	
	@Override
	public List<Integer> getDataVals() {
		ArrayList<Integer> dataVals = new ArrayList<Integer>();
		dataVals.add(numSharks);
		dataVals.add(numFish);
		return dataVals;
	}
	
	@Override
	public List<String> getDataLabels() {
		ArrayList<String> dataLabels = new ArrayList<String>();
		dataLabels.add("Sharks");
		dataLabels.add("Fish");
		return dataLabels;
	}

}