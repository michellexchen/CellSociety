import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SplashScreen {
	private VBox menu;
	private final int SPLASHSIZE = 400;
	private Group splashGroup;

	private ResourceBundle myResources = ResourceBundle.getBundle("Resources/English");
	private Button start;
	private Text welcome;
	private SimulationOptional simOption;
	private Scene splash;
	private boolean gotSim;
	private Simulation currentSim;

	/**
	 * This method is responsible for creating the scene for the menu screen that allows the user to select a simulation.
	 * @return the Scene containing the menu screen
	 */
	public Scene SplashScreen(Main myMain, int SIZE) {
		welcome(SIZE);
		start(myMain);		
		makeMenu(menu);
		addMenu();
		return formatting(splash, SIZE);
	}

	private Scene formatting(Scene splash, int SIZE){
		splash = new Scene(splashGroup, SIZE, SIZE);
		splash.setFill(Color.SLATEBLUE);
		splash.getStylesheets().add("Resources/style.css");
		return splash;
	}
	
	private void makeMenu(VBox menu){
		menu.getChildren().add(start);
		menu.getStyleClass().add("hbox");
	}
	
	private void addMenu(){
		splashGroup = new Group();
		splashGroup.getChildren().add(menu);
	}
	
	private void welcome(int SIZE){
		menu = new VBox();
		menu.setPrefSize(SPLASHSIZE, SPLASHSIZE);
		menu.setLayoutX((SIZE - SPLASHSIZE) / 2);
		menu.setLayoutY((SIZE - SPLASHSIZE) / 2);
		welcome = new Text(myResources.getString("Select"));
		menu.getChildren().add(welcome);
	}
	
	private void start(Main myMain){
		start = new Button(myResources.getString("Upload"));
		start.setMinWidth(115);
		start.setOnMouseClicked(e -> {
				simOption = new XMLReader().getSimulation();
				myMain.setSimOption(simOption); 
				if (simOption == null) { //if cant read simuation type
					noSimulation();
					return;
				}
				try{
					myMain.startystart();
					} catch (Exception e2) { //if xml file contents are bad
						String errorMessage = simOption.getExceptionMessage();
						handleError(errorMessage);						
				}
							
		});
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
		Text msg2 = new Text(myResources.getString("Error2"));
		Button ok = new Button(myResources.getString("OK"));		
		clearForError(msg, msg2, ok);
		pressedOK(ok, msg, msg2);
	}

	public void noSimulation() {
		Text msg = new Text(myResources.getString("SimError"));
		Text msg2 = new Text(myResources.getString("SimError2"));
		Button ok = new Button(myResources.getString("OK"));	
		clearForError(msg, msg2, ok);
		pressedOK(ok, msg, msg2);
		
	}
	
	public void clearForError(Text msg, Text msg2, Button ok){ //clear screen to display error msg
		menu.getChildren().addAll(msg, msg2, ok);
		menu.getChildren().removeAll(welcome, start);
	}
	
	public void pressedOK(Button ok, Text msg, Text msg2){ //after user presses ok, display uploader
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				menu.getChildren().removeAll(msg, msg2, ok);
				menu.getChildren().addAll(welcome, start);
			}
		});
		
	}
}
