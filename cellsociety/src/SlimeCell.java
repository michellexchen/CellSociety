import javafx.scene.paint.Color;

public class SlimeCell extends GridCell {
	
	private double currentCamp;
	private double campDiff;
	private double campEvap;
	private double prevCamp = 0;

	public SlimeCell(String state, Color color, int x, int y, double diffuse, double evap) {
		super(state, color, x, y);
		campDiff = diffuse;
		campEvap = evap;
	}

	
	public void addCamp(double add){
		currentCamp += add;
	}
	
	public double getCamp(){
		return currentCamp;
	}
	
	public void update(){
		if(getNextState() == Slime.AGENT){
			this.setNextColor(Slime.AGENTCOLOR);
		}
		else{
			this.setNextState(Slime.EMPTY);
			this.setNextColor(Slime.EMPTYCOLOR.darker());
		}
		
		diffuse();
		evaporate();
	}
	
	public void diffuse(){
		double diff = 0;
		if(currentCamp > campDiff){
			diff = campDiff/this.getAllNeighbors().size();
		}
		else{
			diff = currentCamp/getAllNeighbors().size();
		}
		for(GridCell cell: getAllNeighbors()){
			SlimeCell slime = (SlimeCell) cell;
			slime.addCamp(diff);
			currentCamp -= diff;
		}
	}
	
	public void evaporate(){
		if(currentCamp > campEvap){
			currentCamp -= campEvap;
		}
		else{
			currentCamp = 0;
		}
	}
	
	public void setPrevCamp(double pc){
		prevCamp = pc;
	}
	
	public double getPrevCamp(){
		return prevCamp;
	}
}
