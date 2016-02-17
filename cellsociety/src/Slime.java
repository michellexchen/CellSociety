/**SAUMYA JAIN
 * This class is part of my masterpiece. 
 * Compared with my previous project all of the functions in this class are concise and easy to follow, and I eliminated previous bad practices like using magic numbers and hard coding
 * This class is also an effective example of using Inheritance. I make use of superclass functions to construct and initialize this class and make the updates fit into the general Simulation abstraction
 * 
 * 
 */
 
 
import javafx.scene.paint.Color;
import java.util.*;

public class Slime extends Simulation{
	private static ResourceBundle myResources = ResourceBundle.getBundle("Resources/English");
	private static final String TITLE = myResources.getString("Slime");
	public static final String EMPTY = "EMPTY";
	public static final String AGENT = "AGENT";
	public static final Color EMPTYCOLOR = Color.GREEN;
	public static final Color AGENTCOLOR = Color.RED;
	private final double DIFF = 1;
	private final double EVAP = .9;
	private final double CAMP = 5;
	private final double THRESHOLD = 2;
	private final char AGENTCELL = '1';
	private int numAgents;
	private int totalCamp;
	private List<SlimeAgent> myAgents;
	
	public Slime(int size, int numCells, boolean toroidal, boolean triangular, int numSlime) {
		super(TITLE,size,numCells, toroidal, triangular);
		numAgents = numSlime;
		myAgents = new ArrayList<SlimeAgent>();
		initialize();
		initChart();
	}
	
	public Slime(List<String> columns, int size,boolean tor, boolean tri){
		super(columns, TITLE, size, tor, tri);
		initChart();
	}
	
	private void initialize(){
		super.init();
		List<int[]> agentLocs = super.getRandomCoordinates(numAgents);
		initAgents(agentLocs);
		initEmpty();
		super.displayGrid();
	}

	@Override
	public void initExplicit(char current, int col, int row) {
		if(myAgents == null){
			myAgents = new ArrayList<SlimeAgent>();
		}
		
		if(current == AGENTCELL){
			SlimeCell newCell = new SlimeCell(AGENT, AGENTCOLOR, col, row, DIFF, EVAP);
			int direction = (int) (Math.random()*8);
			SlimeAgent temp = new SlimeAgent(newCell, direction, CAMP, THRESHOLD);
			myAgents.add(temp);
			getCells()[col][row] = newCell;
		}
		else{
			getCells()[col][row] = new SlimeCell(EMPTY, EMPTYCOLOR, col, row, DIFF, EVAP);
		}
	}

	private void initAgents(List<int[]> locs){
		for(int[] location: locs){
			int direction = (int) (Math.random()*8);
			SlimeCell current = new SlimeCell(AGENT, AGENTCOLOR, location[0], location[1], DIFF, EVAP );
			SlimeAgent temp = new SlimeAgent(current, direction, CAMP, THRESHOLD); 
			myAgents.add(temp);
			super.getCells()[location[0]][location[1]] = current;
		}
	}
	
	private void initEmpty(){
		for(int i = 0; i < super.getGridSize(); i++){
			for(int j = 0; j < super.getGridSize(); j ++){
				if(super.getCells()[i][j] == null){
					SlimeCell temp = new SlimeCell(EMPTY, EMPTYCOLOR, i,j,DIFF,EVAP);
					super.getCells()[i][j] = temp;
				}
			}
		}
	}
	
	@Override
	public void update() {

		totalCamp = 0;
		for(SlimeAgent temp: myAgents){
			temp.update();
		}
		
		for(GridCell c: super.getCellList()){
			SlimeCell e =  (SlimeCell) c;
			e.update();
		}
		updateCamp();
		super.updateStates();

	}
	
	private void updateCamp(){
		for(GridCell c: super.getCellList()){
			SlimeCell e = (SlimeCell) c;
			totalCamp += e.updateCamp();
		}	
	}

	@Override
	public List<Integer> getDataVals() {
		ArrayList<Integer> dataVals = new ArrayList<Integer>();
		dataVals.add(totalCamp);
		return dataVals;
	}

	@Override
	public List<String> getDataLabels() {
		ArrayList<String> dataLabels = new ArrayList<String>();
		dataLabels.add("Camp");
		return dataLabels;
	}
	
	@Override
	public int getChartY(){
		return 10000;
	}

}
