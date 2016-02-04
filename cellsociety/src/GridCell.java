
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridCell {
	
	private Color myColor;
	private Rectangle mySquare;
	private String currState;
	private String nextState;
	private int[] myCoordinates;
	
	public GridCell(String state, Color color) {
		currState = state;
		myColor = color;
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
	
	public void setX(int x){
		myCoordinates[0] = x;
	}
	
	public void setY(int y){
		myCoordinates[1] = y;
	}
	
	public void setMySquare(Rectangle mySquare) {
		this.mySquare = mySquare;
	}
}
