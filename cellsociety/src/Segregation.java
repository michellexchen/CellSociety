
import java.util.*;
import javafx.scene.paint.Color;
/**
 * 
 * @author Saumya Jain, Collette Torres, Michelle Chen
 * This class reresents the Segregation model simulation
 * The class populates a grid with members of two groups
 * Members of the groups move if they are dissatisfied, as per the rules in Schelling's model of segregation
 * The class passes the states of cells to the Simulation superclass that outputs the results visually
 *
 */

public class Segregation extends Simulation {
	private static final String TITLE = "Segregation";
	private static final String GROUP1 = "GROUP1";
	private static final String GROUP2 = "GROUP2";
	private static final String EMPTY = "EMPTY";
	private static final Color GROUP1COLOR = Color.RED;
	private static final Color GROUP2COLOR = Color.BLUE;
	private static final Color BACKGROUND = Color.GAINSBORO;
	private static final String GROUP1_LABEL = "# Group 1 Satisfied";
	private static final String GROUP2_LABEL = "# Group 2 Satisfied";
	
	private int myPopulation;
	private int group1Pop;
	private int group1Dis;
	private int group2Pop;
	private int group2Dis;
	private double percentGroup1;
	private double myThreshold;
	private List<GridCell> emptyCells;
	private List<GridCell> nextEmpty = new ArrayList<GridCell>();
	private List<GridCell> cellList = new ArrayList<GridCell>();
	
	/**
	 * 
	 * @param size Size of Scene
	 * @param numCells Number of cells per row/col
	 * @param population Total number of nonempty cells
	 * @param group1population Proportion of population belonging to group one
	 * @param threshold Threshold for satisfaction
	 */

	public Segregation(int size, int numCells, int population, double group1population, double threshold, boolean tor, boolean tri) {
		super(TITLE,size,numCells, tor, tri);
		myPopulation = population;
		group1Pop = (int)(population*group1population);
		group2Pop = population-group1Pop;
		percentGroup1 = group1population;
		myThreshold = threshold;
		
		super.getStateMap().put(GROUP1, GROUP1COLOR);
		super.getStateMap().put(GROUP2, GROUP2COLOR);
		initialize();
		initChart();

	}
	
	public Segregation(List<String> columns, int size, double thresh, boolean tor, boolean tri){
		super(columns, TITLE, size, tor, tri);
		myThreshold = thresh;
		initChart();

	}
	
	@Override
	public void initExplicit(char current, int col, int row) {
		if(emptyCells == null){
			emptyCells = new ArrayList<GridCell>();
		}

		if(current == '0'){
			GridCell empty = new GridCell(EMPTY, BACKGROUND, col, row);
			emptyCells.add(empty);
			getCells()[col][row] = empty;
		}
		else if(current == '1'){
			getCells()[col][row] = new GridCell(GROUP1, GROUP1COLOR, col, row);
			group1Pop++;
		}
		else if(current == '2'){
			getCells()[col][row] = new GridCell(GROUP2, GROUP2COLOR, col, row);
			group2Pop++;
		}
		cellList = getCellList();

	}
	
	/**
	 * Initializes a Scene with randomly distributed members of group1 and group 2
	 */

	public void initialize(){
		super.init();
		randomInit(myPopulation, percentGroup1, GROUP1, GROUP2, EMPTY, GROUP1COLOR, GROUP2COLOR, BACKGROUND); 
		emptyCells = getEmptyCells();
		cellList = getCellList();
		initChartStats();
		super.displayGrid();
	}
	
	private void initChartStats(){
		for(GridCell cell: cellList){
			if(isDissatisfied(cell)&&cell.getState()==GROUP1){
				group1Dis++;
			}
			if(isDissatisfied(cell)&&cell.getState()==GROUP2){
				group2Dis++;
			}
		}
	}
	
	/**
	 * Updates state of cellls
	 * Checks if populated cells are satisfied
	 * Moves dissatisfied cells
	 * Updates list of empty cells
	 * Randomizes order of checking so that one area of the grid doesn't consistently move before another
	 */
	@Override
	public void update(){
		group1Dis = 0;
		group2Dis = 0;
		
		cellList = super.getCellList();
		
		for(GridCell e: emptyCells){
			e.setNextState(EMPTY);
			e.setNextColor(BACKGROUND);
		}
			Collections.shuffle(cellList);
			for(GridCell cell: cellList){
				String currState = cell.getState();
				if(currState != EMPTY)
				{
					if(isDissatisfied(cell)){ //if not satisfied and can move, move
						if(currState==GROUP1){
							group1Dis++;
						}
						else if(currState==GROUP2){
							group2Dis++;
						}
						GridCell empty = getRandEmpty();
						if(empty != null){
							empty.setNextState(currState);
							empty.setNextColor(cell.getMyColor());
							cell.setNextState(EMPTY);
							cell.setNextColor(BACKGROUND);
							nextEmpty.add(cell);
						}	
					}
					
					if(cell.getNextState()==null){ //if satisfied or can't move, do nothing
						cell.setNextState(currState);
						cell.setNextColor(cell.getMyColor());
					}
				}
			}
		emptyCells.addAll(nextEmpty);
		nextEmpty.clear();
		//setting current state to next state and clearing next state
		updateStates();
	}
	/**
	 * Checks if cell is satisfied based on threshold 
	 * @param cell A cell to be checked for satisfaction
	 * @return
	 */
	private boolean isDissatisfied(GridCell cell){
		List<GridCell> neighbors = cell.getAllNeighbors();
		double numSame = 0;
		double numDiff = 0;
		for(GridCell n: neighbors){
			if(cell.getState()==n.getState()){
				numSame++;
			}
			else if(n.getState() != EMPTY){
				numDiff++;
			}
		}
		
		return (numSame/(numSame+numDiff)<myThreshold);
	}
	/**
	 * 
	 * @return A random cell from a list of empty cells
	 */
	private GridCell getRandEmpty(){
		Random rnd = new Random();
		if(emptyCells.size() == 0){
			return null;
		}
		GridCell empty = emptyCells.get(rnd.nextInt(emptyCells.size()));
		emptyCells.remove(empty);
		return empty;
	}
	@Override
	public List<Integer> getDataVals() {
		ArrayList<Integer> dataVals = new ArrayList<Integer>();
		dataVals.add(group1Pop-group1Dis);
		dataVals.add(group2Pop-group2Dis);
		return dataVals;
	}
	@Override
	public List<String> getDataLabels() {
		ArrayList<String> dataLabels = new ArrayList<String>();
		dataLabels.add(GROUP1_LABEL);
		dataLabels.add(GROUP2_LABEL);
		return dataLabels;
		
	}
	
	@Override
	public int getChartY(){
		return Math.max(group1Pop,group2Pop);
	}

}		
	
