import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.scene.Scene;


public class Segregation extends Simulation {
	private double threshold = 0.30;
	private int population = 50;
	private int gridSize;
	private Scene myScene;
	private GridCell[][] myGrid;
	private ArrayList<GridCell> emptyCells;
	
	public Segregation() {
		// TODO Auto-generated constructor stub
		gridSize = getGridSize();
		myGrid = getCells();
	}
	
	public Scene init(){
		myScene = super.init(gridSize,gridSize);
		
		HashMap<Integer,Integer> randCoords = new HashMap<Integer,Integer>();
		Random rnd = new Random();
		while(population>0){
			int x = rnd.nextInt(gridSize);
			int y = rnd.nextInt(gridSize);
			if(randCoords.get(x)!=null){
				randCoords.put(x,y);
			}
			population--; 
		}
		
		for(Integer x: randCoords.keySet()){
			int y = randCoords.get(x);
			if(rnd.nextInt()%2==0){
				myGrid[x][y] = new SegregationCell("GROUP1");
			}
			else{
				myGrid[x][y] = new SegregationCell("GROUP2");
			}
		}
		
		for(GridCell[] row: getCells()){
			for(GridCell c: row){
				if(c==null){
					c = new SegregationCell("EMPTY");
					emptyCells.add(c);
				}
			}
		}
		
		initCells();
		
		return myScene;
	}
	
	@Override
	public void update(){
		//go through entire grid and calculate satisfaction
			//if satisfied: next state = satisfied; 
			//if dissastisfied: next state = empty
				//get random empty and change its state, update empty
			//if empty: if next state != null, next state = empty;
		for(int x=0; x<gridSize; x++){
			for(int y=0; y<gridSize; y++){
				GridCell currCell = myGrid[x][y];
				String currState = currCell.getState();
				if(currState=="EMPTY"){
					emptyCells.add(currCell);
				}
				ArrayList<GridCell> neighbors = getAllNeighbors(x,y);
				int num = 0;
				for(GridCell n: neighbors){
					if(currState==n.getState()){
						num++;
					}
				}
				if(currState!="EMPTY" && num/8<threshold){ //make not magic number
					GridCell empty = getRandEmpty();
					empty.setNextState(currState);
				}
				if(currCell.getNextState()!=null){
					currCell.setNextState(currState);
				}
			}
		}
	}
	
	private GridCell getRandEmpty(){
		Random rnd = new Random();
		GridCell empty = emptyCells.get(rnd.nextInt(emptyCells.size()));
		emptyCells.remove(empty);
		return empty;
	}

	@Override
	public void updateColors() {
		// TODO Auto-generated method stub
		
	}

}
