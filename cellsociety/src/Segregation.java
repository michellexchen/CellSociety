import java.util.*;
import java.util.Random;

import javafx.scene.Scene;


public class Segregation extends Simulation {
	private final static double THRESHOLD = 0.30;
	private final static int MY_POPULATION = 4;
	private final static double PERCENT_GROUP1 = 0.5;
	private final static double PERCENT_GROUP2 = 0.5;
	static HashMap<Integer,ArrayList<Integer>> randCoords = new HashMap<Integer,ArrayList<Integer>>();
	private int gridSize;
	private Scene myScene;
	private GridCell[][] myGrid;
	private ArrayList<GridCell> emptyCells;
	
	public Segregation() {
		// TODO Auto-generated constructor stub
		gridSize = 4;//getGridSize();
		myGrid = getCells();
	}
	
	public Scene init(){
		int population = MY_POPULATION; 
		int popGroup1 = (int)(population*PERCENT_GROUP1);
		int popGroup2 = (int)(population*PERCENT_GROUP2);
		
		//myScene = super.init(gridSize,gridSize);
		
		//ArrayList<int[]> randCoords = new ArrayList<int[]>(); //make y list of ints 
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
			ArrayList<Integer> ycoords = new ArrayList<Integer>();
			ycoords.add(y);
			randCoords.put(x, ycoords);
		}

		
		for(int i=0; i<randCoords.size(); i++){ 
			int[] coord = randCoords.get(i);
			int x = coord[0];
			int y = coord[1];
			
			if(popGroup1>0){ //alternating between group1 and group2 
				myGrid[x][y] = new GridCell("GROUP1");
				popGroup1--;
				continue;
			}
			myGrid[x][y] = new GridCell("GROUP1");
			
		}
		
		for(GridCell[] row: getCells()){
			for(GridCell c: row){
				if(c==null){
					c = new GridCell("EMPTY");
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
				else{
					ArrayList<GridCell> neighbors = getAllNeighbors(x,y);
					int num = 0;
					for(GridCell n: neighbors){
						if(currState==n.getState()){
							num++;
						}
					}
					if(num/neighbors.size()<THRESHOLD){ //make not magic number
						GridCell empty = getRandEmpty();
						empty.setNextState(currState);
					}
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
	
	public static void main(String[] args){
		Segregation test = new Segregation();
		test.init();
		for(int x: randCoords.keySet()){
			System.out.print(x);
			System.out.println(Arrays.toString(randCoords.get(x).toArray()));
		}
	}

}
