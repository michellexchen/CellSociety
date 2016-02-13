
import java.util.ArrayList;
import java.util.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class GridCell {
	/*
	 * This class is the basic building block of a cellular automata
	 * Every cell has a state, a location, and a color
	 * The Simulation class and its subclasses manage the state and color of the cells according to specific rules
	 * The Simulation class visually represents the information stored in this class
	 */
	private Color myColor = Color.BLACK;
	private Color nextColor;
	private Shape mySquare;
	private String currState;
	private String nextState;
	private int[] myCoordinates = new int[2];
	private Map<Integer, GridCell> myNeighbors = new HashMap<Integer, GridCell>();
	private List<GridCell> cardNeighbors = new ArrayList<GridCell>();
	private List<GridCell> allNeighbors = new ArrayList<GridCell>();
	private Map<Integer,ArrayList<GridCell>> forwardNeighbors;
	private Map<Integer,ArrayList<GridCell>> backwardNeighbors;

	/**
	 * 
	 * @param state The initial state of the gridcell
	 * @param color The initial color of the gridcell
	 * @param x The x-location of that cell
	 * @param y The y-location of that cell
	 */
	public GridCell(String state, Color color, int x, int y) {
		currState = state;
		myColor = color;
		myCoordinates = new int[]{x,y};
		
		//forwardNeighbors = initForwardNeighbors();
		//backwardNeighbors = initBackwardNeighbors();
		
	}
	
	private Map<Integer,ArrayList<GridCell>> initForwardNeighbors(){
		Map<Integer,ArrayList<GridCell>> neighborMap = new HashMap<Integer,ArrayList<GridCell>>();
		for(int i=0; i<8; i++){
			ArrayList<GridCell> neighbors = new ArrayList<GridCell>();
			neighbors.add(myNeighbors.get((i-1)%8));
			neighbors.add(myNeighbors.get(i));
			neighbors.add(myNeighbors.get((i+1)%8));
			neighborMap.put(i, neighbors);
		}
		return forwardNeighbors;
	}
	
	private Map<Integer,ArrayList<GridCell>> initBackwardNeighbors(){
		Map<Integer,ArrayList<GridCell>> neighborMap = new HashMap<Integer,ArrayList<GridCell>>();
		for(int i=0; i<8; i++){
			ArrayList<GridCell> neighbors = new ArrayList<GridCell>();
			Collections.copy(neighbors, allNeighbors);
			ArrayList<GridCell> forward = forwardNeighbors.get(i);
			neighbors.removeAll(forward);
			neighborMap.put(i,neighbors);
		}	
		return neighborMap;
	}
	
	public List<GridCell> getRangeNeighbors(int direction, int range){ //returns neighbors within a specified range from direcetion. Ex: (5, 2) returns neighbors from 3-7 inclusive
		List<GridCell> neighbors = new ArrayList<GridCell>();
		for(int i = direction-range; i <= direction+range; i++){
			int neighbor = i%8;
			if(myNeighbors.get(neighbor) != null){
				neighbors.add(myNeighbors.get(neighbor));
			}	
		}
		return neighbors;
	}
	
	/*
	 * Getters and setters below
	 */
	public String getState(){
		return currState;
	}
	
	public String getNextState(){
		return nextState;
	}
	
	public Shape getMySquare() {
		return mySquare;
	}

	public Color getMyColor() {
		return myColor;
	}
	
	public int getX(){
		return myCoordinates[0];
	}
	
	public int getY(){
		return myCoordinates[1];
	}

	public void setState(String state){
		currState = state;
	}
	
	public void setNextState(String state){
		nextState = state;
	}
	
	public void setMyColor(Color myColor) {
		this.myColor = myColor;
	}
	
	public void setNextColor(Color myColor) {
		this.nextColor = myColor;
	}
	
	public void updateColor() {
		this.myColor = this.nextColor;
	}
	

	public void setMyShape(Shape mySquare) {

		this.mySquare = mySquare;
	}
	
	public void addNeighbor(int direction, GridCell neighbor){
		if(!myNeighbors.containsKey(direction)){
			myNeighbors.put(direction, neighbor);
		}
		allNeighbors.add(neighbor);
		if(direction%2 == 0){
			cardNeighbors.add(neighbor);
		}
	}
	
	public List<GridCell> getCardinalNeighbors(){
		return cardNeighbors;
	}
	
	public List<GridCell> getAllNeighbors(){
		return allNeighbors;
	}
	
	public List<GridCell> getForwardNeighbors(int direction){
		return forwardNeighbors.get(direction);
	}
	
	public List<GridCell> getBackwardNeighbors(int direction){
		return backwardNeighbors.get(direction);
	}

}