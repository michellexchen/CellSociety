package cellsociety;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Cell {
	
	private Color myColor;
	private Rectangle mySquare;
	private String curState;
	private String nextState;
	
	public Cell() {
	}
	
	public abstract void changeColors(); //Changes colors of cells based on their NEW state

	public Rectangle getMySquare() {
		return mySquare;
	}

	public Color getMyColor() {
		return myColor;
	}

	public void setMyColor(Color myColor) {
		this.myColor = myColor;
	}

}
