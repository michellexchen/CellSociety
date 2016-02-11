import java.util.ArrayList;

import javafx.scene.paint.Color;

public class AntCell extends AnimalCell {
	private double homePher;
	private double foodPher;
	private double evapRate;
	private double diffRate;

	public AntCell(String state, Color color, int x, int y, int maxAnt) {
		super(state, color, x, y, maxAnt);
		// TODO Auto-generated constructor stub
	}
	
	public void setPher(double amtPher,String pherKind){
		switch(pherKind){
		case "HOME":
			homePher = amtPher;
			break;
		case "FOOD":
			foodPher = amtPher;
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
		homePher = homePher*(1+amt);
		foodPher = foodPher*(1+amt);
	}
	
	public void diffuse(){
		ArrayList<AntCell> neighbors = new ArrayList<AntCell>(); //get neighbors
		for(AntCell neighbor: neighbors){
			neighbor.receivePher(diffRate);
		}
		
	}

}
