
public class Ant extends Animal {
	private static final String NAME = "ANT";
	private static final int BREED = 0;
	
	private boolean hasFood;

	public Ant(int antLife, int x, int y) {
		super(NAME, BREED, antLife, x, y);
		// TODO Auto-generated constructor stub
	}
	
	public int getX(){
		return super.getX();
	}
	
	public int getY(){
		return super.getY();
	}
	
	public void setOrientation(int direction){
		super.setOrientation(direction);
	}
	
	public void updateDieTime(){
		super.updateDieTime();
	}
	
	public String getKind(){
		return super.getKind();
	}
	
	public boolean timeToDie(){
		return super.timeToBreed();
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