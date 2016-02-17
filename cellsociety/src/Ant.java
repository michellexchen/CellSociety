
public class Ant extends Animal {
	private static final String NAME = "ANT";
	private static final int BREED = 0;
	
	private boolean hasFood;

	public Ant(int antLife, int x, int y) {
		super(NAME, BREED, antLife, x, y);
	}
	
	public boolean hasFoodItem(){
		return hasFood;
	}
	
	public void dropFood(){
		hasFood = false;
	}
	
	public void pickUpFood(){
		hasFood = true;
	}

}