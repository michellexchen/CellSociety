import java.util.*;
public class SlimeAgent {
	
	private SlimeCell myCell;
	private int myDirection;
	private double campDrop;
	private SlimeCell nextCell;
	private double thresh;
	
	public SlimeAgent(SlimeCell cell, int direction, double camp, double thres) {
		myCell = cell;
		myDirection = direction; 
		campDrop = camp;
		thresh = thres;
	}

	public void update(){
		List<GridCell> neighbors = myCell.getRangeNeighbors(myDirection, 3);

		for(int i = 0; i < neighbors.size(); i++){
			if(neighbors.get(i).getNextState() == Slime.AGENT)
				neighbors.remove(i);
		}
		if(neighbors.size() == 0){
			nextCell = myCell;	
		}
		else if(myCell.getCamp() >= thresh){
			followCamp(neighbors);	
		}	
		else{
			nextCell = myCell;	
		}
		
		myCell.addCamp(campDrop);
		move();
	}
	
	public void followCamp(List<GridCell> neighbors){
		nextCell = myCell;
		double max = -1;
		for(GridCell cell: neighbors){
			SlimeCell slime = (SlimeCell) cell;
			if(slime.getCamp() > max){
				max = slime.getCamp();
				nextCell = slime;
			}
		}
	}
	
	public void move(){
		nextCell.setNextState(Slime.AGENT);
		myCell = nextCell;
		nextCell = null;
	}

}
