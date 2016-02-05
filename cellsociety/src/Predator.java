import javafx.scene.Scene;
import javafx.scene.paint.Color;
import java.util.*;

public class Predator extends Simulation {
	private int gridSize;
	private Scene myScene;
	private GridCell[][] myGrid;
	private int[][] breedGrid;
	private int[][] dieGrid;
	private int myPopulation;
	private double percentFish;
	private double percentShark;
	private int sharkBreedTime;
	private int sharkDieTime;
	private int fishBreedTime;
	private ArrayList<GridCell> taken = new ArrayList<GridCell>();
	
	
	public Predator(int fishBreed, int sharkBreed, int sharkDie, int population, double fish, double shark) {
		myPopulation = population;
		percentFish = fish;
		percentShark = shark;
		sharkBreedTime = sharkBreed;
		sharkDieTime = sharkDie;
		fishBreedTime = fishBreed;
	}
	
	@Override
	public Scene init(int size, int numCells){
		myScene = super.init(size,numCells);
		gridSize = super.getGridSize();
		breedGrid = new int[gridSize][gridSize];
		dieGrid = new int[gridSize][gridSize];
		randomInit(myGrid, myPopulation, percentFish, percentShark, "FISH", "SHARK", Color.GREEN, Color.YELLOW, Color.BLUE); //make not magic strings
		initGridCells();
		myGrid = getCells();
		return myScene;
	}

	@Override
	public void update() {
		updateEmpty();
		updateSharks();
		updateFish();
		updateStates();
		taken.clear();
	}
	
	private void updateEmpty(){
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myGrid[x][y].getState()=="EMPTY"){
					myGrid[x][y].setNextState("EMPTY");
				}
			}
		}
	}
	
	private void updateSharks(){
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myGrid[x][y].getState()=="SHARK"){
					GridCell cell = myGrid[x][y];
					ArrayList<GridCell> neighbors = getCardinalNeighbors(x,y);
					ArrayList<GridCell> emptyNeighbors = getSpecialNeighbors(neighbors,"EMPTY");
					ArrayList<GridCell> fishNeighbors = getSpecialNeighbors(neighbors,"FISH");
					GridCell fish = getRandomCell(fishNeighbors);
					GridCell empty = getRandomCell(emptyNeighbors);
					if(fish!=null){
						eatFish(cell,fish,emptyNeighbors);
					}
					else if(dieGrid[x][y]+1==sharkDieTime){
						cell.setNextState("EMPTY");
						breedGrid[x][y]=0;
						dieGrid[x][y]=0;
					}
					else if(empty!=null){
						moveShark(cell,empty);
					}
					else{
						cell.setNextState("SHARK");
						breedGrid[x][y]=0;
						dieGrid[x][y]++;
					}
					
					
				}
			}
		}
	}

	private void updateFish(){
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				if(myGrid[x][y].getState()=="FISH"){
					GridCell cell = myGrid[x][y];
					ArrayList<GridCell> neighbors = getCardinalNeighbors(x,y);
					ArrayList<GridCell> emptyNeighbors = getSpecialNeighbors(neighbors,"EMPTY");
					GridCell empty = getRandomCell(emptyNeighbors);
					if(breedGrid[x][y]+1>=fishBreedTime){
						breedAnimal(cell,emptyNeighbors);
					}
					if(empty!=null){
						moveFish(cell,empty);
					}
					else{
						cell.setNextState("FISH");
						breedGrid[x][y]++;
					}
				}
			}
		}
	}
	
	private void moveShark(GridCell shark, GridCell empty) {
		empty.setNextState("SHARK");
		shark.setNextState("EMPTY");
		breedGrid[empty.getX()][empty.getY()] = 0;
		dieGrid[empty.getX()][empty.getY()] = dieGrid[shark.getX()][shark.getY()]+1;
		breedGrid[shark.getX()][shark.getY()] = 0;
		dieGrid[shark.getX()][shark.getY()] = 0;
	}

	private void eatFish(GridCell shark, GridCell fish, ArrayList<GridCell> emptyNeighbors) {
		fish.setState("EMPTY");
		fish.setNextState("SHARK");
		shark.setNextState("EMPTY");
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
	
	private void moveFish(GridCell fish, GridCell empty){
		empty.setNextState("FISH");
		fish.setNextState("EMPTY");
		breedGrid[empty.getX()][empty.getY()]=breedGrid[fish.getX()][fish.getY()];
		breedGrid[fish.getX()][fish.getY()]=0;	
	}
	
	private void breedAnimal(GridCell animal, ArrayList<GridCell> emptyNeighbors) {
		GridCell newAnimal = getRandomCell(emptyNeighbors);
		if(newAnimal!=null){ 
			newAnimal.setNextState(animal.getState());
			breedGrid[animal.getX()][animal.getY()]=0;
		}
	}
	
	private GridCell getRandomCell(ArrayList<GridCell> selections){
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
	
	private ArrayList<GridCell> getSpecialNeighbors(ArrayList<GridCell> neighbors, String type){
		ArrayList<GridCell> special = new ArrayList<GridCell>();
		for(GridCell n: neighbors){
			if(n.getState()==type){
				special.add(n);
			}
		}
		return special;
	}

	@Override
	public void updateColors() {
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				GridCell cell = myGrid[x][y];
				if(cell.getState()=="SHARK"){
					cell.setMyColor(Color.YELLOW);
				}
				else if(cell.getState()=="FISH"){
					cell.setMyColor(Color.GREEN);
				}
				else{
					cell.setMyColor(Color.BLUE);
				}
			}
		}

	}

}

