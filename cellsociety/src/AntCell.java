import java.util.ArrayList;

import javafx.scene.paint.Color;

public class AntCell extends AnimalCell {
	private double homePher;
	private double foodPher;
	private double evapRate;
	private double diffRate;
	private double maxPher;
	private double minPher;
	private double K;
	private double N;
	
	public AntCell(String state, Color color, int x, int y, int antMax, double pherMax, double pherMin, double evaporation, double diffusion) {
		super(state, color, x, y, antMax);
		evapRate = evaporation;
		diffRate = diffusion;
		maxPher = pherMax;
		minPher = pherMin;
	}
	
	public void setPher(double amtPher,String pherKind){
		switch(pherKind){
		case "HOME":
			if(amtPher>maxPher){
				homePher = maxPher;
			}
			else if (amtPher<minPher){
				homePher = minPher;
			}
			else{
				homePher = minPher;
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
				foodPher = minPher;
			}
			break;
		}
	}
	
	public double getPher(String pherKind){
		switch(pherKind){
		case "HOME":
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
		homePher = homePher*(1-evapRate);
		foodPher = foodPher*(1-evapRate);
	}
	
	public void receivePher(double amt){
		setPher(homePher*(1+amt),"HOME");
		setPher(foodPher*(1+amt),"FOOD");
	}
	
	public void diffuse(){
		ArrayList<AntCell> neighbors = new ArrayList<AntCell>(); //get neighbors
		for(AntCell neighbor: neighbors){
			neighbor.receivePher(diffRate);
		}
		
	}
	
	public double getProbability(){
		return Math.pow((K+foodPher),N);
	}

}