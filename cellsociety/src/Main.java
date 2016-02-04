import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Duration;

public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000/60;
	private Stage myStage;
	private final int SIZE = 800;
	private Simulation currentSim;
	
	@Override
	public void start(Stage gameStage) {
		myStage  = gameStage;
		myStage.setTitle("Simulation");
		
		myStage.setScene(currentSim.init(SIZE,SIZE));
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
