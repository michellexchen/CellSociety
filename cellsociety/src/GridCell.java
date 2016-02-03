
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridCell {
	
	private Color myColor;
	private Rectangle mySquare;
	private String currState;
	private String nextState;
	
	public GridCell(String state) {
		currState = state;
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

}
