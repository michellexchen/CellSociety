
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000/2;
	private Stage myStage;
	private final int SIZE = 800;

	private final int NUMCELLS = 100;
	private static Simulation currentSim = new XMLReader("predator.txt").getSimulation();
	private Timeline animation;
	
	@Override
	public void start(Stage gameStage) {
		myStage  = gameStage;
		myStage.setTitle(currentSim.getTitle());

		myStage.setScene(currentSim.init());
		myStage.show();
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> currentSim.step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		
		addButtons();	
	}
	
	private void addButtons(){
    	Button start = new Button("Start");
        start.setOnMouseClicked(e->playSim());
        
        Button stop = new Button("Stop");
        stop.setOnMouseClicked(e->stopSim());
     
        Button pause = new Button("Pause");
        pause.setOnMouseClicked(e->pauseSim());
        
        Button step = new Button("Step");
        step.setOnMouseClicked(e->step());
        
        Button speedUp = new Button("Speed Up");
        speedUp.setOnMouseClicked(e->speedUp());
        
        Button slowDown = new Button("Slow Down");
        slowDown.setOnMouseClicked(e->slowDown());
        
        HBox buttons = new HBox(2,start,stop,pause,step,speedUp,slowDown);
        
        currentSim.getRoot().getChildren().add(buttons);
        
	}
	
	private void playSim(){
		animation.play();
	}
	
	private void stopSim(){ 
		animation.stop();
	}
	
	private void pauseSim(){
		animation.pause();
	}
	
	private void step(){
		currentSim.step();
	}
	
	private void speedUp(){
		animation.setRate(2.0);
	}
	
	private void slowDown(){
		animation.setRate(0.5);
	}
	
	 public static void main(String[] args) {
		 launch(args);
     }
}
