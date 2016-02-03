
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class GridCell {
	
	private Color myColor;
	private Rectangle mySquare;
	private String currState;
	private String nextState;
	
	public GridCell(String state) {
		currState = state;
	}

	public abstract void changeColors(); //Changes colors of cells based on their NEW state
	
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
