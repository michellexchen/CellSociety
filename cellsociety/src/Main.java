import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.stage.Popup;
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

	private final int MILLISECOND_DELAY = 1000 / 2;
	private final double SPEED = .5;
	private final int BUTTONHEIGHT = 50;
	private final int BUTTONPADDING = 40;
	private final int SPLASHSIZE = 400;
	private final int SIZE = 500;

	private String address1;
	private String address2;
	private ResourceBundle myResources;

	private Stage myStage;
	private Scene myScene;
	private static Popup popup;
	private Simulation currentSim;
	private SimulationOptional simOption;
	private Timeline animation;
	private Group splashGroup = new Group();
	private Scene splashScene;

	/**
	 * This method is responsible for starting the simulation by setting up the
	 * stage for the screen and first displaying the menu screen It is also
	 * responsible for setting up the simulation timeline
	 * 
	 */
	@Override
	public void start(Stage gameStage) {

		myResources = ResourceBundle.getBundle("Resources/English");

		myStage = gameStage;
		splashScene = splashScene();
		myStage.setScene(splashScene);
		myStage.setTitle("Simulations Home Screen");
		myStage.show();

		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> currentSim.step());
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
	}

	/**
	 * This method is responsible for adding a GUI to the simulation scene once
	 * a simulation has been chosen. It handles user input to control the
	 * simulation timeline (whether it's playing, stopped, or its speed is
	 * changed)
	 */
	private void addButtons() {
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
			myStage.setScene(splashScene);
			animation.stop();
			animation.setRate(1);
			address1 = null;
			address2 = null;
		});

		HBox buttons = new HBox(start, stop, pause, step, speedUp, slowDown, switchSim);
		buttons.setPrefSize(currentSim.getSceneSize(), BUTTONHEIGHT);
		buttons.setLayoutY(currentSim.getSceneSize());
		buttons.getStyleClass().add("hbox");
		myScene.getStylesheets().add("Resources/style.css");

		currentSim.getRoot().getChildren().add(buttons);

	}

	/**
	 * This method is responsible for creating the scene for the menu screen that allows the user to select a simulation.
	 * @return the Scene containing the menu screen
	 */
	private Scene splashScene() {
		Scene splash = new Scene(splashGroup, SIZE, SIZE);

		splash.setFill(Color.GRAY);
		ArrayList<String> options = new ArrayList<String>();
		options.add("Fire");
		options.add("Segregation");
		options.add("Predator");
		options.add("Life");
		
		ArrayList<String> configs = new ArrayList<String>();
		configs.add("1");
		configs.add("2");
		splash.setFill(Color.SLATEBLUE);

		VBox menu = new VBox();
		menu.setPrefSize(SPLASHSIZE, SPLASHSIZE);
		menu.setLayoutX((SIZE - SPLASHSIZE) / 2);
		menu.setLayoutY((SIZE - SPLASHSIZE) / 2);
		Text welcome = new Text(myResources.getString("Select"));
		menu.getChildren().add(welcome);

		ComboBox sims = new ComboBox();
		sims.setPromptText("Simulation");
		sims.getItems().addAll(options);
		sims.setOnAction(e -> {
			 address1 = sims.getSelectionModel().getSelectedItem().toString();
		});
		
		ComboBox number = new ComboBox();
		number.setPromptText("Configuration");
		number.getItems().addAll(configs);
		number.setOnAction(e -> {
			address2 = sims.getSelectionModel().getSelectedItem().toString();
		});
		
		Button start = new Button("Start");
		start.setMinWidth(115);
		start.setOnMouseClicked(e -> {
			if(address1 != null){
				simOption = new XMLReader("./src/XML/" + address1+ "XML.txt").getSimulation();
				if(!simOption.hasException()){
					currentSim = simOption.getSimulation();
					myStage.setTitle(currentSim.getTitle());
					myScene = currentSim.init();
					myStage.setHeight(currentSim.getSceneSize() + BUTTONHEIGHT + BUTTONPADDING);
					addButtons();
					myStage.setScene(myScene);
				}
				else {
					String errorMessage = simOption.getExceptionMessage();
					handleError(errorMessage);

				} 	
			}
		});

		menu.getChildren().add(sims);
		menu.getChildren().add(number);
		menu.getChildren().add(start);
		menu.getStyleClass().add("hbox");
		splash.getStylesheets().add("Resources/style.css");
		splashGroup.getChildren().add(menu);
		return splash;
	}

	/**
	 * This method is responsible for displaying a pop-up error message when
	 * there is faulty user input such that an XML file can't be read to
	 * initialize a simulation
	 * 
	 * @param errorMessage
	 *            the error message to be displayed to the user
	 */
	public void handleError(String errorMessage) {
		Text msg = new Text(myResources.getString("Error"));
		Button ok = new Button(myResources.getString("OK"));
		ok.setMinWidth(375);

		VBox popUpVBox = new VBox();
		popUpVBox.getChildren().add(msg);
		popUpVBox.getChildren().add(ok);

		popup = new Popup();
		popup.setAutoFix(false);
		popup.setHideOnEscape(true);
		popup.getContent().addAll(popUpVBox);
		popup.setX(530);
		popup.setY(260);

		ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				popup.hide();
			}
		});

		popup.show(myStage);

	}

	public static void main(String[] args) throws MalformedURLException {

		launch(args);
	}
}
