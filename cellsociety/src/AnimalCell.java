// This entire file is part of my masterpiece.
// Colette Torres
/*
 * This code is responsible for a cell that can contain zero or more animals objects.
 * This class is meant to be used as a superclass for cells containing different kinds of animals.
 * Thus, it contains basic functionality shared between simulation cells dealing with animals.
 * I think it is well designed because it paves the way for an Inheritance hierarchy, extending
 * the GridCell which provides the foundations for all cells in cellular automaton, but providing
 * basic functionality for cells that hold a(n) animal(s) -- providing a way to add or remove animal(s)
 * and keep track of a cell's capacity.
 * This was a design decision I made after the first sprint, when I realized that the Foraging Ants
 * simulation drew some parallels with Wa-Tor, and I realized this was a flexible way to deal with simulations
 * that had agents moving across cells, so that the cells did not need to keep track of all of the 
 * properties of the animals it contained as is the case in Wa-tor.  Instead, it was cleaner to have the
 * animals keep track of these pieces of information themselves, no matter where they moved, and just have the AnimalCell keep track of 
 * the animals it contained at any given point, and one could just access that information by accessing the animals. 
 * Thus, instead of keeping track of breeding and dying times, and other specific properties in a 2-D array, as 
 * I did in Wa-tor, I thought this showed off what we learned in class about Inheritance hierarchies. I also
 * think this class shows off the Open-Close principle, as it is very flexible, containing properties and methods any kind of cell containing animals could need, but nothing too specific
 * by animal kind.  Thus it can easily be extended.
 *  
 */

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
	
	public List<Animal> getAnimals(){
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
