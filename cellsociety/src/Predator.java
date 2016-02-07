import javafx.scene.Scene;
import javafx.scene.paint.Color;
import java.util.*;

public class Predator extends Simulation {
	private static final String TITLE = "WA-TOR";
	private static final String SHARK = "SHARK";
	private static final String FISH = "FISH";
	private static final String EMPTY = "EMPTY";
	private Color FISHCOLOR = Color.GREEN;
	private Color SHARKCOLOR = Color.YELLOW;
	private Color BACKGROUND = Color.BLUE;
	private int gridSize;
	private GridCell[][] myCells;
	private int[][] breedGrid;
	private int[][] dieGrid;
	private int myPopulation;
	private double percentFish;
	private int sharkBreedTime;
	private int sharkDieTime;
	private int fishBreedTime;
	private ArrayList<GridCell> taken = new ArrayList<GridCell>();
	
	
	public Predator(int size, int numCells, int fishBreed, int sharkBreed, int sharkDie, int population, double fish) { //Initializes simulation constants
		super(TITLE, size, numCells);
		myPopulation = population;
		percentFish = fish;
		sharkBreedTime = sharkBreed;
		sharkDieTime = sharkDie;
		fishBreedTime = fishBreed;
	}
	
	public Scene init(){ //Assigns fish, sharks, and empty cells - returns a Scene with these attributes
		super.init();
		gridSize = super.getGridSize();
		breedGrid = new int[gridSize][gridSize];
		dieGrid = new int[gridSize][gridSize];
		randomInit(myPopulation, percentFish, FISH, SHARK, EMPTY, FISHCOLOR, SHARKCOLOR, BACKGROUND);
		initGridCells();
		myCells = getCells();
		return super.getMyScene();
	}

	@Override
	public void update() {
		updateEmpty();
		updateSharks();
		updateFish();
		updateStates();
		taken.clear();
	}
	
	private void updateEmpty(){ //Sets default next state to empty
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myCells[x][y].getState()==EMPTY){
					myCells[x][y].setNextState(EMPTY);
				}
			}
		}
	}
	
	private void updateSharks(){  //Applies rulse for shark death/breeding/eating
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myCells[x][y].getState()==SHARK){
					GridCell cell = myCells[x][y];
					ArrayList<GridCell> neighbors = getCardinalNeighbors(x,y);
					ArrayList<GridCell> emptyNeighbors = getSpecialNeighbors(neighbors,EMPTY);
					ArrayList<GridCell> fishNeighbors = getSpecialNeighbors(neighbors,FISH);
					GridCell fish = getRandomCell(fishNeighbors);
					GridCell empty = getRandomCell(emptyNeighbors);
					if(fish!=null){ //if fish available eat it
						eatFish(cell,fish,emptyNeighbors);
					}
					else if(dieGrid[x][y]+1==sharkDieTime){ //if shark reached die time, die and reset breed and die times
						cell.setNextState(EMPTY);
						breedGrid[x][y]=0;
						dieGrid[x][y]=0;
					}
					else if(empty!=null){ //if not reached die time, and can move, move
						moveShark(cell,empty);
					}
					else{ //if cannot move, keep it and reset breed time but increment die time
						cell.setNextState(SHARK);
						breedGrid[x][y]=0;
						dieGrid[x][y]++;
					}
				}
			}
		}
	}

	private void updateFish(){ //applies rules for fish breeding and dying time
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myCells[x][y].getState()==FISH){
					GridCell cell = myCells[x][y];
					ArrayList<GridCell> neighbors = getCardinalNeighbors(x,y);
					ArrayList<GridCell> emptyNeighbors = getSpecialNeighbors(neighbors,EMPTY);
					GridCell empty = getRandomCell(emptyNeighbors);
					if(breedGrid[x][y]+1>=fishBreedTime){ //if next time it can breed, breed
						breedAnimal(cell,emptyNeighbors);
					}
					if(empty!=null){ //if can move, move
						moveFish(cell,empty);
					}
					else{ //if cant move, keep it here and increment breed time here
						cell.setNextState(FISH);
						breedGrid[x][y]++;
					}
				}
			}
		}
	}
	
	private void moveShark(GridCell shark, GridCell empty) { //change next state of new cell and clear current cell
		empty.setNextState(SHARK);
		shark.setNextState(EMPTY);
		breedGrid[empty.getX()][empty.getY()] = 0; //bc moving shark implies couldnt find fish so reset breed time
		dieGrid[empty.getX()][empty.getY()] = dieGrid[shark.getX()][shark.getY()]+1; //increment die time in next location bc couldnt find fish
		breedGrid[shark.getX()][shark.getY()] = 0; //clear breed and die time in current location
		dieGrid[shark.getX()][shark.getY()] = 0;
	}

	private void eatFish(GridCell shark, GridCell fish, ArrayList<GridCell> emptyNeighbors) { //Updates grid when sharks eat fish
		fish.setState(EMPTY); //so you don't look at it when iterating through fish
		fish.setNextState(SHARK);
		shark.setNextState(EMPTY);
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
	
	private void moveFish(GridCell fish, GridCell empty){ //Controls fish movements
		empty.setNextState(FISH);
		fish.setNextState(EMPTY);
		breedGrid[empty.getX()][empty.getY()]=breedGrid[fish.getX()][fish.getY()]+1;
		breedGrid[fish.getX()][fish.getY()]=0;	
	}
	
	private void breedAnimal(GridCell animal, ArrayList<GridCell> emptyNeighbors) { //Creates a new shark or fish
		GridCell newAnimal = getRandomCell(emptyNeighbors);
		if(newAnimal!=null){ 
			newAnimal.setNextState(animal.getState());
			breedGrid[animal.getX()][animal.getY()]=0;
		}
	}
	
	private GridCell getRandomCell(ArrayList<GridCell> selections){ //Returns a random gridcell
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
	
	private ArrayList<GridCell> getSpecialNeighbors(ArrayList<GridCell> neighbors, String type){ //Returns all neighbors of a specific type
		ArrayList<GridCell> special = new ArrayList<GridCell>();
		for(GridCell n: neighbors){
			if(n.getState()==type){
				special.add(n);
			}
		}
		return special;
	}

	@Override
	public void updateColors() { //Updates grid colors to reflect changes in state
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				GridCell cell = myCells[x][y];
				if(cell.getState()==SHARK){
					cell.setMyColor(SHARKCOLOR);
				}
				else if(cell.getState()==FISH){
					cell.setMyColor(FISHCOLOR);
				}
				else{
					cell.setMyColor(BACKGROUND);
				}
			}
		}

	}

}

