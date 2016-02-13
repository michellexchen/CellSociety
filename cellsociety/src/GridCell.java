
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
		
	}
	
	private int mod8(int i){
		if(i%8<0){
			return 8+(i%8);
		}
		return i%8;
	}
	
	public void initForwardNeighbors(){
		Map<Integer,ArrayList<GridCell>> neighborMap = new HashMap<Integer,ArrayList<GridCell>>();
		for(int i=0; i<allNeighbors.size(); i++){
			ArrayList<GridCell> neighbors = new ArrayList<GridCell>();
			int[] indices = new int[]{i-1,i,i+1};
			for(int x: indices){
				GridCell neighbor = myNeighbors.get(mod8(x));
				if(neighbor!=null){
					neighbors.add(neighbor);
				}
			}
			neighborMap.put(i, neighbors);
		}
		forwardNeighbors = neighborMap;
	}
	
	public void initBackwardNeighbors(){
		Map<Integer,ArrayList<GridCell>> neighborMap = new HashMap<Integer,ArrayList<GridCell>>();
		for(int i=0; i<allNeighbors.size(); i++){
			ArrayList<GridCell> neighbors = new ArrayList<GridCell>();
			for(GridCell neighbor: allNeighbors){
				if(!forwardNeighbors.get(i).contains(neighbor)){
					neighbors.add(neighbor);
				}
			}
			neighborMap.put(i,neighbors);
		}	
		backwardNeighbors = neighborMap;
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
		if(direction%2 == 1){
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
	
	public int getOrientationTo(GridCell cell){
		for(int key: myNeighbors.keySet()){
			if(myNeighbors.get(key).equals(cell)){
				return key;
			}
		}
		return 0; //not sure what to return here for default
	}

}