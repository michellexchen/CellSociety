import javafx.scene.Scene;
import javafx.scene.paint.Color;
import java.util.*;

public class Slime extends Simulation{
	private static final String TITLE = "Slime Molds";
	public static final String EMPTY = "EMPTY";
	public static final String AGENT = "AGENT";
	public static final Color EMPTYCOLOR = Color.GREEN;
	public static final Color AGENTCOLOR = Color.RED;
	private final double DIFF = 1;
	private final double EVAP = .9;
	private final double CAMP = 2;
	private final double threshold = 2;
	private int numAgents;
	private List<SlimeAgent> myAgents;
	private int totalCamp;
	
	public Slime(int size, int numCells, boolean toroidal, boolean triangular, int numSlime) {
		super(TITLE,size,numCells, toroidal, triangular);
		numAgents = numSlime;
		myAgents = new ArrayList<SlimeAgent>();
		initialize();
		initChart();
	}
	
	public Slime(String[] columns, int size,boolean tor, boolean tri){
		super(columns, TITLE, size, tor, tri);
	}

	public void initialize(){
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
		
		if(current == '0'){
			getCells()[col][row] = new SlimeCell(EMPTY, EMPTYCOLOR, col, row, DIFF, EVAP);
		}
		
		else if(current == '1'){
			SlimeCell newCell = new SlimeCell(AGENT, AGENTCOLOR, col, row, DIFF, EVAP);
			int direction = (int) (Math.random()*8);
			SlimeAgent temp = new SlimeAgent(newCell, direction, CAMP, threshold);
			myAgents.add(temp);
			getCells()[col][row] = newCell;
		}
	}

	private void initAgents(List<int[]> locs){
		for(int[] location: locs){
			int direction = (int) (Math.random()*8);
			SlimeCell current = new SlimeCell(AGENT, AGENTCOLOR, location[0], location[1], DIFF, EVAP );
			SlimeAgent temp = new SlimeAgent(current, direction, CAMP, threshold); //Agent contains cell
			myAgents.add(temp);
			super.getCells()[location[0]][location[1]] = current;
		}
	}
	
	public void initEmpty(){
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
		Collections.shuffle(myAgents);
		for(SlimeAgent temp: myAgents){
			temp.update();
		}
		for(GridCell[] c: super.getCells()){
			for(GridCell d: c){
				SlimeCell e =  (SlimeCell) d;
				e.update();
			}
		}
		
		for(GridCell[] c: super.getCells()){
			for(GridCell d:c){
				
				SlimeCell e = (SlimeCell) d;
				if(e.getNextState() == EMPTY && e.getState() == EMPTY){
					if(e.getPrevCamp() > e.getCamp()){
						e.setNextColor(e.getMyColor().darker());
					}
					else if(e.getPrevCamp() < e.getCamp()){
						e.setNextColor(e.getMyColor().brighter());
					}
					else{
						e.setNextColor(e.getMyColor());
					}
				}
				totalCamp += e.getCamp();
				e.setPrevCamp(e.getCamp());
			}
		}
		updateStates();
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

}
