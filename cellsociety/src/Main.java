
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000/2;
	private final double SPEED = .5;	
	private int SIZE = 700;
	private final int NUMCELLS = 100;
	private final int BUTTONHEIGHT = 100;
	private final int BUTTONCENTER = 157;
	private final double BUTTONINSET = 17.5;
	private final Color BUTTONCOLOR = Color.GRAY;

	private Stage myStage;
	private static Simulation currentSim = new XMLReader("./src/XML/FireXML.txt").getSimulation();
	private Timeline animation;

	
	@Override
	public void start(Stage gameStage) {
		myStage  = gameStage;
		myStage.setTitle(currentSim.getTitle());
		myStage.setScene(currentSim.init());
		myStage.setHeight(SIZE + BUTTONHEIGHT);

		myStage.show();
		
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                e -> currentSim.step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		
		addButtons(0, SIZE);
	}
	
	private void addButtons(int x, int y){
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
        
        HBox buttons = new HBox(5,start,stop,pause,step,speedUp,slowDown);
        buttons.setPrefSize(SIZE, BUTTONHEIGHT);
        buttons.setPadding(new Insets(BUTTONINSET));
        buttons.setLayoutX(BUTTONCENTER);
        buttons.setLayoutY(SIZE);  		
        
        Rectangle bg = new Rectangle(0,SIZE,SIZE,BUTTONHEIGHT);
		bg.setFill(BUTTONCOLOR);
		
		currentSim.getRoot().getChildren().add(bg);
        currentSim.getRoot().getChildren().add(buttons);
	}
	
	 public static void main(String[] args) {
		 launch(args);
     }
}
