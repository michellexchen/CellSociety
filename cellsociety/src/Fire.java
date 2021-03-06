
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
/**
 * 
 * @author Michelle
 *
 * This class is responsible for setting up the Fire simulation.
 * The fire simulation takes in a user inputed probability probCatch of a tree burning down and simulates the spread of fire in a forest.
 * Every time a tree next to a healthy tree catches on fire, the healthy tree has probCatch probability of catching on fire at the next step.
 * The forest is surrounded by empty spaces; when no more trees are on fire the simulation ends.
 * 
 */
public class Fire extends Simulation {
	private static final String TITLE = "Fire";
	private static final String BURNING = "BURNING";
	private static final String TREE = "TREE";
	private static final String EMPTY = "EMPTY";
	private static final Color BURNINGCOLOR = Color.RED;
	private static final Color TREECOLOR = Color.GREEN;
	private static final Color BACKGROUND = Color.YELLOW;
	private double myProbCatch;
	private int numTree;
	private int numBurning;


	public Fire(int size, int numCells, double probCatch, boolean tor, boolean tri) {
		super(TITLE,size,numCells, tor, tri);
		myProbCatch = probCatch;
		initialize();
	}
	
	public Fire(List<String> columns, int size, double probCatch, boolean tor, boolean tri){
		super(columns, TITLE, size, tor, tri);
		myProbCatch = probCatch;
		initChart();
	}
	
	@Override
	public void initExplicit(char current, int col, int row) {
		if(current == '0'){
			getCells()[col][row] = new GridCell(EMPTY, BACKGROUND, col, row);
		}
		else if(current == '1'){
			getCells()[col][row] = new GridCell(TREE, TREECOLOR, col, row);
		}
		else if(current == '2'){
			getCells()[col][row] = new GridCell(BURNING, BURNINGCOLOR, col, row);
		}
	}
	
	/**
	 *  Initializes the scene and sets up the grid with a burning tree in the middle, surrounded by healthy trees. 
	 *  The grid is bordered by empty cells.
	 */
	public void initialize(){
		super.init();
		GridCell[][]myCells = super.getCells();
		int gridSize = super.getGridSize();
		for (int a = 0; a<gridSize; a++) { //row
			for (int b = 0; b<gridSize; b++) {
				if (a == (gridSize-1)/2&& b == (gridSize-1)/2) {
					myCells[a][b] = new GridCell(BURNING, BURNINGCOLOR,a,b);
					numBurning++;
				}
				else if (a == 0 || b == 0 || a == gridSize-1 || b == gridSize-1){
					myCells[a][b] = new GridCell(EMPTY, BACKGROUND,a,b);
				}
				else {
					myCells[a][b] = new GridCell(TREE, TREECOLOR,a,b);
					numTree++;
				}
			}
		}
		displayGrid();	
		initChart();	
	}
	
	/**
	 * Goes through the entire grid and updates cells to their next state. 
	 * If the cell is currently empty, its next state is empty.
	 * If the cell is currently burning, its next state is empty.
	 * If the cell contains a healthy tree, if its neighbor is burning and a random number between 0 and 1 < probCatch, its next state is burning.
	 * Otherwise (if the neighbor is a tree or the random number > probCatch), the cell's next state is still a healthy tree.
	 */
	@Override
	public void update() {
		for (int x = 0; x<super.getGridSize(); x++) { 
			for (int y = 0; y<super.getGridSize(); y++) {
				GridCell curr = getCells()[x][y];
				String currState = curr.getState();
				if (currState == EMPTY){
					setNext(curr, currState, BACKGROUND);
				} else if (currState == BURNING) {
					setNext(curr, EMPTY, BACKGROUND);
				} else if (currState == TREE) {
					List<GridCell> neighbors = curr.getCardinalNeighbors();
					List<String> blah = new ArrayList<String>();
					for (GridCell cell: neighbors){
						blah.add(cell.getState());
					}
					if (blah.contains(BURNING)) {
						if (Math.random()<myProbCatch) {
							numBurning++;
							numTree--;
							setNext(curr, BURNING, BURNINGCOLOR);
						}
						else {
							setNext(curr, currState, TREECOLOR);
						}
					}
					else {
						setNext(curr, currState, TREECOLOR);
					}						
				}
			}
		}		
		updateStates();		
	}

	public void setNext(GridCell curr, String currState, Color color){
		curr.setNextState(currState);
		curr.setNextColor(color);
	}
	@Override
	public List<Integer> getDataVals() {
		ArrayList<Integer> dataVals = new ArrayList<Integer>();
		dataVals.add(numBurning);
		dataVals.add(numTree);
		return dataVals;
	}

	@Override
	public List<String> getDataLabels() {
		ArrayList<String> dataLabels = new ArrayList<String>();
		dataLabels.add("Burning");
		dataLabels.add("Trees");
		return dataLabels;
	}

}