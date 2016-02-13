import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class AntCell extends AnimalCell {
	private double homePher;
	private double foodPher;
	private double evapRate;
	private double diffRate;
	private double maxPher;
	private double minPher;
	private double K= 0.001;
	private double N = 10;
	
	public AntCell(String state, Color color, int x, int y, int antMax, double pherMax, double pherMin, double evaporation, double diffusion) {
		super(state, color, x, y, antMax);
		evapRate = evaporation;
		diffRate = diffusion;
		maxPher = pherMax;
		minPher = pherMin;
		if(state=="NEST"){
			homePher = maxPher;
		}
		else if(state=="FOOD"){
			foodPher = maxPher;
		}
	}
	
	public void setPher(double amtPher,String pherKind){
		switch(pherKind){
		case "NEST":
			if(amtPher>maxPher){
				homePher = maxPher;
			}
			else if (amtPher<minPher){
				homePher = minPher;
			}
			else{
				homePher = amtPher;
			}
			break;
		case "FOOD":
			if(amtPher>maxPher){
				foodPher = maxPher;
			}
			else if (amtPher<minPher){
				foodPher = minPher;
			}
			else{
				foodPher = amtPher;
			}
			break;
		}
	}
	
	public double getPher(String pherKind){
		switch(pherKind){
		case "NEST":
			return homePher;
		case "FOOD":
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
		addPher(-homePher*evapRate,"NEST");
		addPher(-foodPher*evapRate,"FOOD");
	}
	
	public void addPher(double amt,String pherKind){
		switch(pherKind){
		case "NEST":
			setPher(homePher+amt,"NEST");
			break;
		case "FOOD":
			setPher(foodPher+amt,"FOOD");
		}
	}
	
	public void diffuse(){
		List<GridCell> neighbors = getAllNeighbors();//get neighbors
		for(GridCell neighbor: neighbors){
			if(neighbor.getState()=="FOOD" || neighbor.getState()=="OBSTACLE"){
				continue;
			}
			((AntCell)neighbor).addPher(homePher*diffRate,"NEST");
			((AntCell)neighbor).addPher(foodPher*diffRate,"FOOD");
		}
		addPher(-homePher*diffRate,"NEST");
		addPher(-foodPher*diffRate,"FOOD");
		
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
		if(getState()=="GROUND" && getCapacity()>0)
		{
			return Color.rgb((int)(255*(getCapacity())/10), 0, 0);
		}
		return super.getMyColor();
	}

}