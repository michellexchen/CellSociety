import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Life extends Simulation {

	private final String DEAD = "DEAD";
	private final String ALIVE = "ALIVE";
	private final Color DEADCOLOR = Color.GRAY;
	private final Color ALIVECOLOR = Color.BLUE;
	
	public Life() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Scene init(int size, int numGridCells){
		
		super.init(size,numGridCells);
		
		for(int x=0; x<super.getGridSize(); x++){
			for(int y=0; y<super.getGridSize(); y++){
					GridCell c = new GridCell(DEAD, DEADCOLOR);
					getCells()[x][y] = c;
			}
		}
		super.initGridCells();
		return super.getMyScene();
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateColors() {
		// TODO Auto-generated method stub

	}
	
	

}
