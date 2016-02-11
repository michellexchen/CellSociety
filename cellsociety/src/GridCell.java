
import java.util.ArrayList;

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
	private Shape mySquare;
	private String currState;
	private String nextState;
	private int[] myCoordinates = new int[2];
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
	
	public void setMyShape(Shape mySquare) {
		this.mySquare = mySquare;
	}

}