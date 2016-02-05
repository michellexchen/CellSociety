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
		for(int i=0; i<gridSize; i++){
			for(int j=0; j<gridSize; j++){
				System.out.print(myGrid[i][j].getState());
			}
			System.out.println();
		}
		for(int i=0; i<gridSize; i++){
			for(int j=0; j<gridSize; j++){
				System.out.print(breedGrid[i][j]);
			}
			System.out.println();
		}
		updateEmpty();
		updateSharks();
		updateFish();
		updateStates();
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
					ArrayList<GridCell> neighbors = getCardinalNeighbors(x,y);
					System.out.printf("Coordinates for shark at (%d,%d)",x,y);
					System.out.println();
					for(int i=0; i<neighbors.size();i++){
						System.out.printf("(%d,%d)",neighbors.get(i).getX(),neighbors.get(i).getY());
					}
					System.out.println();
					ArrayList<GridCell> emptyNeighbors = getSpecialNeighbors(neighbors,"EMPTY");
					ArrayList<GridCell> fishNeighbors = getSpecialNeighbors(neighbors,"FISH");
					if(fishNeighbors.size()>0){
						eatFish(x,y,emptyNeighbors,fishNeighbors);
					}
					else if(dieGrid[x][y]+1==sharkDieTime){
						myGrid[x][y].setNextState("EMPTY");
						breedGrid[x][y]=0;
						dieGrid[x][y]=0;
					}
					else if(emptyNeighbors.size()>0){
						System.out.println("moving");
						moveShark(x,y,emptyNeighbors);
					}
					else{
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
					ArrayList<GridCell> neighbors = getCardinalNeighbors(x,y);
					ArrayList<GridCell> emptyNeighbors = getSpecialNeighbors(neighbors,"EMPTY");
					if(emptyNeighbors.size()>0){
						moveFish(x,y,emptyNeighbors);
					}
					else{
						breedGrid[x][y]++;
					}
					if(breedGrid[x][y]+1>=fishBreedTime){
						breedAnimal("FISH",x,y,emptyNeighbors);
					}
				}
			}
		}
	}
	
	private void moveShark(int sourceX, int sourceY, ArrayList<GridCell> emptyNeighbors) {
		GridCell empty = getRandomCell(emptyNeighbors);
		empty.setNextState("SHARK");
		System.out.printf("Moved shark from (%d, %d) to ($d, %d)",sourceX,sourceY,empty.getX(),empty.getY());
		System.out.println();
		breedGrid[empty.getX()][empty.getY()] = 0;
		dieGrid[empty.getX()][empty.getY()] = dieGrid[sourceX][sourceY]+1;
		breedGrid[sourceX][sourceY] = 0;
		dieGrid[sourceX][sourceY] = 0;
	}

	private void eatFish(int sourceX, int sourceY, ArrayList<GridCell> emptyNeighbors, ArrayList<GridCell> fishNeighbors) {
		GridCell fish = getRandomCell(fishNeighbors);
		fishNeighbors.remove(fish);
		System.out.printf("Moved shark from (%d, %d) to (%d, %d)",sourceX,sourceY,fish.getX(),fish.getY());
		fish.setNextState("SHARK");
		if(breedGrid[sourceX][sourceY]+1>=sharkBreedTime){ 
			breedAnimal("SHARK",sourceX,sourceY,emptyNeighbors);
		}
		else{
			breedGrid[sourceX][sourceY]++;
		}
		breedGrid[fish.getX()][fish.getY()] = breedGrid[sourceX][sourceY];
		breedGrid[sourceX][sourceY] = 0;
		dieGrid[sourceX][sourceY] = 0;
	}
	
	private void moveFish(int sourceX, int sourceY, ArrayList<GridCell> emptyNeighbors){
		GridCell empty = getRandomCell(emptyNeighbors);
		emptyNeighbors.remove(empty);
		empty.setNextState("FISH");
		breedGrid[empty.getX()][empty.getY()]++;
		breedGrid[sourceX][sourceY]=0;
	}
	private void breedAnimal(String animal, int sourceX, int sourceY, ArrayList<GridCell> emptyNeighbors) {
		if(emptyNeighbors.size()>0){ 
			GridCell newAnimal = getRandomCell(emptyNeighbors);
			emptyNeighbors.remove(newAnimal);
			newAnimal.setNextState(animal);
			breedGrid[sourceX][sourceY]=0;
		}
	}
	
	private GridCell getRandomCell(ArrayList<GridCell> selections){
		Random rnd = new Random();
		if(selections.size()>0){
			return selections.get(rnd.nextInt(selections.size()));
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

