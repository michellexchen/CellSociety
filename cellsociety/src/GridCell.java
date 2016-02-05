
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridCell {
	
	private Color myColor = Color.BLACK;
	private Rectangle mySquare;
	private String currState;
	private String nextState;
	private int[] myCoordinates = new int[2];
	
	public GridCell(String state, Color color, int x, int y) {
		currState = state;
		myColor = color;
		myCoordinates = new int[]{x,y};
	}
	
	public String getState(){
		return currState;
	}
	
	public String getNextState(){
		return nextState;
	}
	
	public Rectangle getMySquare() {
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
	
	public void setMySquare(Rectangle mySquare) {
		this.mySquare = mySquare;
	}

}