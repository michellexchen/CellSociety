import java.util.ArrayList;
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
	
	public void setPher(double amtPher,String pherKind){
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
	
	public void addAnimal(Animal animal){
		super.addAnimal(animal);
	}
	
	public void removeAnimal(Animal animal){
		super.removeAnimal(animal);
	}
	
	public Animal getAnimal(){
		return super.getAnimal();
	}
	
	public ArrayList<Animal> getAnimals(){
		return super.getAnimals();
	}
	
	public boolean atCapacity(){
		return super.atCapacity();
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
			if(neighbor.getState()==Ants.FOOD || neighbor.getState()=="OBSTACLE"){
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
		setMyColor(this.getMyColor());
	}
	
	@Override
	public Color getMyColor(){
		if(getState()==Ants.GROUND && getCapacity()>0)
		{
			return Color.rgb((int)(RED_MAX*(getCapacity())/RED_ADJUST), 0, 0);
		}
		return super.getMyColor();
	}

}