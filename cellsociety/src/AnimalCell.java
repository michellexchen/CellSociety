import javafx.scene.paint.Color;
import java.util.*;

public class AnimalCell extends GridCell {
	private ArrayList<Animal> animals = new ArrayList<Animal>();
	private int maxCapacity;
	
	public AnimalCell(String state, Color color, int x, int y, int maxCap) {
		super(state, color, x, y);
		maxCapacity = maxCap;
	}
	
	public void addAnimal(Animal animal){
		animals.add(animal);
		animal.setX(super.getX());
		animal.setY(super.getY());
	}
	
	public void removeAnimal(Animal animal){
		animals.remove(animal);
	}
	
	public Animal getAnimal(){
		if (animals.size() == 1){
			return animals.get(0);	
		}
		return null;
	}
	
	public ArrayList<Animal> getAnimals(){
		if (animals.size() > 1){
			return animals;
		}
		return null;
	}
	
	public boolean atCapacity(){
		return animals.size()==maxCapacity;
	}
	
	public int getCapacity(){
		return animals.size();
	}

}
