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
	
	
	public Slime(int size, int numCells, boolean toroidal, boolean triangular, int numSlime) {
		super(TITLE,size,numCells, toroidal, triangular);
		numAgents = numSlime;
		myAgents = new ArrayList<SlimeAgent>();		
	}
	
	@Override
	public Scene init(){
		super.init();
		List<int[]> agentLocs = super.getRandomCoordinates(numAgents);
		initAgents(agentLocs);
		initEmpty();
		super.displayGrid();
		return super.getMyScene();
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
				e.setPrevCamp(e.getCamp());
			}
		}	
		updateStates();
	}

	@Override
	public List<Integer> getDataVals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDataLabels() {
		// TODO Auto-generated method stub
		return null;
	}

}
