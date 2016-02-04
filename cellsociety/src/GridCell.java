
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridCell {
	
	private Color myColor = Color.BLACK;
	private Rectangle mySquare;
	private String currState;
	private String nextState;
	
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
