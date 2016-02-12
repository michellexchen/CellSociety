public class Animal {
	private String animal;
	private int breedTime;
	private int dieTime;
	private int breedThresh;
	private int dieThresh;
	private int[] location;
	private double orientation;

	public Animal(String name, int animalBreed, int animalLife, int x, int y) {
		animal = name;
		breedThresh = animalBreed;
		dieThresh = animalLife;
	}

	public void setX(int x){
		location[0] = x;
	}
	
	public void setY(int y){
		location[1] = y;
	}
	
	public int getX(){
		return location[0];
	}
	
	public int getY(){
		return location[1];
	}
	
	public void setOrientation(double reorient){
		orientation = reorient;
	}
	
	public void updateBreedTime(){
		breedTime++;
	}
	
	public void resetBreedTime(){
		breedTime = 0;
	}
	
	public void updateDieTime(){
		dieTime++;
	}
	
	public void resetDieTime(){
		dieTime = 0;
	}
	
	public String getKind(){
		return animal;
	}
	
	public boolean timeToBreed(){
		return breedTime>=breedThresh;
	}
	
	public boolean timeToDie(){
		return dieTime==dieThresh;
	}
	

}