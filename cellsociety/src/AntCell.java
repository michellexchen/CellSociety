import java.util.ArrayList;

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
		setPher(homePher*(1-evapRate),"NEST");
		setPher(foodPher*(1-evapRate),"FOOD");
	}
	
	public void receivePher(double amt){
		setPher(homePher*(1+amt),"NEST");
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