import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 * @author colettetorres
 * @author saumyajain
 * @author michellechen
 * 
 *         This class is responsible for running the simulation application. A
 *         menu screen is displayed to allow the user to select an XML file
 *         specifying details about the simulation they wish to run. After the
 *         user selects a simulation, it is displayed on the screen and played
 *         indefinitely until the user stops it. Via buttons, the user has
 *         control over starting, stopping, pausing, speeding up, or slowing
 *         down the simulation animation. The user may also choose to switch the
 *         simulation displayed on the screen, at which point, the menu screen
 *         is displayed again.
 *
 */
public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000 / 5;
	private final double SPEED = .5;
	private final int BUTTONHEIGHT = 50;
	private final int BUTTONPADDING = 40;
	private final int SIZE = 500;
	
	private String address1;
	private String address2;
	private ResourceBundle myResources;
	private Button start;
	private Text welcome;
	private Stage myStage;
	private Scene myScene;
	private SplashScreen myScreen;
	
	private SimulationOptional simOption;
	private boolean gotSim;
	private Simulation currentSim;

	private Timeline animation;
	private Scene splashScene;

	/**
	 * This method is responsible for starting the simulation by setting up the
	 * stage for the screen and first displaying the menu screen It is also
	 * responsible for setting up the simulation timeline
	 * 
	 */
	
	private String[] cols = {"01010", "00000", "11111", "01010", "11111"};

	@Override

	public void start(Stage gameStage) {
		myResources = ResourceBundle.getBundle("Resources/English");

		myStage = gameStage;
		myScreen = new SplashScreen();
		splashScene = myScreen.SplashScreen(this, SIZE);
		myStage.setScene(splashScene);
		//Simulation temp = new Life(500, 5, -1, false, false);//Slime(500, 100, false, false, 3000);
		//myStage.setScene(temp.init(cols));
		
		myStage.setTitle("Simulations Home Screen");
		myStage.show();

		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> currentSim.step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		//animation.play();
	
	}
	
	/**
	 * This method is responsible for adding a GUI to the simulation scene once
	 * a simulation has been chosen. It handles user input to control the
	 * simulation timeline (whether it's playing, stopped, or its speed is
	 * changed)
	 */
	private void addButtons(ResourceBundle myResources, Timeline animation) {
		Button start = new Button(myResources.getString("Start"));
		start.setOnMouseClicked(e -> animation.play());

		Button stop = new Button(myResources.getString("Stop"));
		stop.setOnMouseClicked(e -> animation.stop());

		Button pause = new Button(myResources.getString("Pause"));
		pause.setOnMouseClicked(e -> animation.pause());

		Button step = new Button(myResources.getString("Step"));
		step.setOnMouseClicked(e -> currentSim.step());

		Button speedUp = new Button(myResources.getString("Speed"));
		speedUp.setOnMouseClicked(e -> animation.setRate(animation.getCurrentRate() + SPEED));

		Button slowDown = new Button(myResources.getString("Slow"));
		slowDown.setOnMouseClicked(e -> {
			if (animation.getCurrentRate() > SPEED) {
				animation.setRate(animation.getCurrentRate() - SPEED);
			}
		});

		Button switchSim = new Button(myResources.getString("Switch"));
		switchSim.setOnMouseClicked(e -> {
			myStage.setScene(myScreen.SplashScreen(this, SIZE));

		});

		HBox buttons = new HBox(start, stop, pause, step, speedUp, slowDown, switchSim);
		buttons.setPrefSize(currentSim.getSceneSize(), BUTTONHEIGHT);
		buttons.setLayoutY(currentSim.getSceneSize());
		buttons.getStyleClass().add("hbox");
		myScene.getStylesheets().add("Resources/style.css");

		currentSim.getRoot().getChildren().add(buttons);

	}
	
	public void setSimOption(SimulationOptional sim){
		 simOption = sim;
	}
	
	public void startystart(){ //starts simulation
		currentSim = simOption.getSimulation();
		myStage.setTitle(currentSim.getTitle());
		myScene = currentSim.init();
		myStage.setHeight(currentSim.getSceneSize() + BUTTONHEIGHT + BUTTONPADDING);
		addButtons(myResources, animation);
		myStage.setScene(myScene);
	}
	
	public static void main(String[] args) throws MalformedURLException {

		launch(args);
	}
}