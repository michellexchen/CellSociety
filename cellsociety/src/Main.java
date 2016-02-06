
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000/2;
	private final double SPEED = .5;	
	private final int BUTTONHEIGHT = 50;
	private final int BUTTONPADDING = 20;

	private Stage myStage;
	private Scene myScene;
	private static Simulation currentSim = new XMLReader("./cellsociety/src/XML/SegregationXML2.txt").getSimulation();
	private Timeline animation;

	
	@Override
	public void start(Stage gameStage) {
		myStage  = gameStage;
		myStage.setTitle(currentSim.getTitle());
		myScene = currentSim.init();
		addButtons();
        myStage.setHeight(currentSim.getSceneSize()+BUTTONHEIGHT+BUTTONPADDING);
		myStage.setScene(myScene);

		myStage.show();
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> currentSim.step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		
	}
	
	private void addButtons(){
    	Button start = new Button("Start");
        start.setOnMouseClicked(e->animation.play());
        
        Button stop = new Button("Stop");
        stop.setOnMouseClicked(e->animation.stop());
     
        Button pause = new Button("Pause");
        pause.setOnMouseClicked(e->	animation.pause());
        
        Button step = new Button("Step");
        step.setOnMouseClicked(e->currentSim.step());

        Button speedUp = new Button("Speed Up");
        speedUp.setOnMouseClicked(e->animation.setRate(animation.getCurrentRate()+SPEED));
        
        Button slowDown = new Button("Slow Down");
        slowDown.setOnMouseClicked(e->{if(animation.getCurrentRate() > SPEED){
			animation.setRate(animation.getCurrentRate()-SPEED);
		}});
        
        Button switchSim = new Button("Switch");
        switchSim.setOnMouseClicked(e->{});
        
        
        HBox buttons = new HBox(start,stop,pause,step,speedUp,slowDown,switchSim);
        buttons.setPrefSize(currentSim.getSceneSize(), BUTTONHEIGHT);
        buttons.setLayoutY(currentSim.getSceneSize());  		
        buttons.getStyleClass().add("hbox");
        
        myScene.getStylesheets().add("style.css");
        
        currentSim.getRoot().getChildren().add(buttons);
        
	}
	
	 public static void main(String[] args) {
		 launch(args);
     }
}
