import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Duration;

public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000/2;
	private Stage myStage;
	private final int SIZE = 800;
	private final int NUMCELLS = 5;
	private Simulation currentSim = new Predator(1,6,6,20,0.75,0.25);//new Segregation(5000,0.25,0.75,0.50);
	
	@Override
	public void start(Stage gameStage) {
		myStage  = gameStage;
		myStage.setTitle("Simulation");
		
		myStage.setScene(currentSim.init(SIZE, NUMCELLS));
		myStage.show();
				
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> currentSim.step());
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		
		animation.play();
		
	}
	
	 public static void main(String[] args) {
	        launch(args);
	    }
}
