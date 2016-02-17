/**SAUMYA JAIN
 * This class is part of my masterpiece. 
 * In this class I tried to address a basic flaw in our design, which was the assumption that all actors are cells. 
 * For Slime Molds I tried to make a distinction between Agents and Cells, so that Cells act as hosts to agents but agents have state variables and behaviors that are independent of cells
 * This class also follows the best practices we've outlined so far, like keeping functions minimal in length and hiding information as much as possible. 
 * 
 * 
 */

import java.util.*;
public class SlimeAgent {
	
	private final int RANGE = 3;
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
		List<GridCell> neighbors = myCell.getRangeNeighbors(myDirection, RANGE);

		for(int i = 0; i < neighbors.size(); i++){
			if(neighbors.get(i).getNextState() != null)
				neighbors.remove(i);
		}
		if(myCell.getCamp() >= thresh){
			followCamp(neighbors);	
		}	
		else{
			nextCell = myCell;	
		}
		
		myCell.addCamp(campDrop);
		move();
		
	}
	
	private void followCamp(List<GridCell> neighbors){
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
	
	private void move(){
		myCell.setNextState(Slime.EMPTY);
		nextCell.setNextState(Slime.AGENT);
		myCell = nextCell;
		nextCell = null;
	}

}
