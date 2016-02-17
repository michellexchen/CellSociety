/**SAUMYA JAIN
 * This class is part of my masterpiece.
 * The class was written and refactored during the second week, and I was able to apply the principle of active classes. 
 * By extending the GridCell class I take advantage of inheritance. In this subclass I added active behaviors so that SlimeCells can update themselves and act on their own neighbors.
 * This is a major divergence from the flaws of GridCell, because this class minimizes use of getters/setters and takes care of its state whenever possible. I think this is a good example of encapsulated Cell behavior into one class
 *  
 */
import javafx.scene.paint.Color;

public class SlimeCell extends GridCell {
	
	private double currentCamp = 0;
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
			this.setNextColor(Slime.EMPTYCOLOR);
		}
		diffuse();
		
		if(this.getState() == Slime.EMPTY){
			evaporate();
		}
	}
	
	private void diffuse(){
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
	
	private void evaporate(){
		if(currentCamp > campEvap){
			currentCamp -= campEvap;
		}
		else{
			currentCamp = 0;
		}
	}

	public double updateCamp(){
		double diff = currentCamp - prevCamp;
		
		if(diff < 0){
			setNextColor(getMyColor().darker());
		}	
		else if(diff > 0){
			setNextColor(getMyColor().brighter());
		}

		prevCamp = currentCamp;	
		return diff;
	}
}
