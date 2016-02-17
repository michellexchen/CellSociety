// This entire file is part of my masterpiece.
// Colette Torres
/*
 * This class is responsible for creating a cell that contains one or more ants. 
 * It extends the AnimalCell class, taking advantage of Inheritance hierarchy by overriding
 * method implementations where needed, and otherwise simply having functions specific to cells
 * containing ants in particular, not methods applicable to any other animal cell.  Those are all
 * contained in its superclass instead.
 * I also think this class shows good design because it shows off data abstraction by ensuring that all of 
 * the cell's fields are private and there are only getter and setter methods where absolutely needed.
 * In fact, I purposefully used encapsulation and eliminated a public setter for the pheromones, making
 * it private and instead, I just have an addPheromones and topPher method
 * so that one can't totally reset the pheromones on their own.  I also like that this class is very modular, 
 * as I purposefully tried to avoid having duplicate code by creating only one method for 
 * dealing with both kinds of pheromones: home and food by simply making use of a parameter to indicate 
 * pheromone type and performing the same function on the right variable.
 * Creating this AntCell was a design decision I made in dealing with the Foraging Ants simulation and 
 * I think it was a good design decision for the future to keep the program flexible by not just making
 * an AnimalCell extension of GridCell that would have lots of functions that could be used for Foraging
 * Ants, but instead extending it one step further to have a class designed for a very specific purpose, and 
 * keeping AnimalCell general enough for other applications.
 */

import java.util.List;

import javafx.scene.paint.Color;

public class AntCell extends AnimalCell {
	private static final int RED_MAX = 255;
	private static final int RED_ADJUST = 10;
	private double homePher;
	private double foodPher;
	private double evapRate;
	private double diffRate;
	private double maxPher;
	private double minPher;
	private double K;
	private double N;
	
	public AntCell(String state, Color color, int x, int y, int antMax, double pherMax, double pherMin, double evaporation, double diffusion, double K, double N) {
		super(state, color, x, y, antMax);
		evapRate = evaporation;
		diffRate = diffusion;
		maxPher = pherMax;
		minPher = pherMin;
		if(state==Ants.NEST){
			homePher = maxPher;
		}
		else if(state==Ants.FOOD){
			foodPher = maxPher;
		}
		this.K = K;
		this.N = N;
	}
	
	private void setPher(double amtPher,String pherKind){
		if(amtPher>maxPher){
			amtPher = maxPher;
		}
		else if (amtPher<minPher){
			amtPher = minPher;
		}
		switch(pherKind){
			case Ants.NEST:
				homePher = amtPher;
				break;
			case Ants.FOOD:
				foodPher = amtPher;
				break;
		}
	}
	
	public void topPheromones(String pherKind){
		setPher(maxPher,pherKind);
	}
	
	public double getPher(String pherKind){
		switch(pherKind){
		case Ants.NEST:
			return homePher;
		case Ants.FOOD:
			return foodPher;
		default:
			return -1;
		}
	}
	
	public void evaporate(){
		addPher(-homePher*evapRate,Ants.NEST);
		addPher(-foodPher*evapRate,Ants.FOOD);
	}
	
	public void addPher(double amt,String pherKind){
		switch(pherKind){
		case Ants.NEST:
			setPher(homePher+amt,Ants.NEST);
			break;
		case Ants.FOOD:
			setPher(foodPher+amt,Ants.FOOD);
		}
	}
	
	public void diffuse(){
		List<GridCell> neighbors = getAllNeighbors();
		for(GridCell neighbor: neighbors){
			if(neighbor.getState()==Ants.FOOD || neighbor.getState()==Ants.OBSTACLE){
				continue;
			}
			((AntCell)neighbor).addPher(homePher*diffRate,Ants.NEST);
			((AntCell)neighbor).addPher(foodPher*diffRate,Ants.FOOD);
		}
		addPher(-homePher*diffRate,Ants.NEST);
		addPher(-foodPher*diffRate,Ants.FOOD);
	}
	
	public double getProbability(){
		return Math.pow((K+foodPher),N);
	}
	
	@Override
	public void updateColor(){
		if(getState()==Ants.GROUND && getCapacity()>0)
		{
			super.setMyColor(Color.rgb((int)(RED_MAX*(getCapacity())/RED_ADJUST), 0, 0));
		}
	}

}