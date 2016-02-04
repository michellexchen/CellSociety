import javafx.scene.Scene;

public class Fire extends Simulation {
	private double probCatch = .2;
	private int gridSize;
	private Scene myScene;
	private GridCell[][] myGrid;
	//getcardinalneighbors
	
	public Fire() {
		// TODO Auto-generated constructor stub
		gridSize = getGridSize();
		myGrid = getCells();
	}
	
	public Scene init(){
		myScene = super.init(gridSize,gridSize);
		//fill
		for (int a = 0; a<gridSize; a++) { //row
			for (int b = 0; b<gridSize; b++) {
				//if (a & b ==0)
			}
		}
	}
	
	//function to set color
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateColors() {
		// TODO Auto-generated method stub
		
	}


}
