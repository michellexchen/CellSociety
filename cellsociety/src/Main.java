
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000 / 2;
	private final double SPEED = .5;
	private final int BUTTONHEIGHT = 50;
	private final int BUTTONPADDING = 40;
	private final int SPLASHSIZE = 400;
	private final int SIZE = 500;
	private ResourceBundle labels;
	private Stage myStage;
	private Scene myScene;
	private static Popup popup;
	private Simulation currentSim;
	private Timeline animation;
	private Group splashGroup = new Group();
	private Scene splashScene;

	@Override
	public void start(Stage gameStage) {
		labels = ResourceBundle.getBundle("labels", Locale.getDefault());
		
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

	private void addButtons() {
		Button start = new Button(labels.getString("Start"));
		start.setOnMouseClicked(e -> animation.play());

		Button stop = new Button(labels.getString("Stop"));
		stop.setOnMouseClicked(e -> animation.stop());

		Button pause = new Button(labels.getString("Pause"));
		pause.setOnMouseClicked(e -> animation.pause());

		Button step = new Button(labels.getString("Step"));
		step.setOnMouseClicked(e -> currentSim.step());

		Button speedUp = new Button(labels.getString("Speed"));
		speedUp.setOnMouseClicked(e -> animation.setRate(animation.getCurrentRate() + SPEED));

		Button slowDown = new Button(labels.getString("Slow"));
		slowDown.setOnMouseClicked(e -> {
			if (animation.getCurrentRate() > SPEED) {
				animation.setRate(animation.getCurrentRate() - SPEED);
			}
		});

		Button switchSim = new Button(labels.getString("Switch"));
		switchSim.setOnMouseClicked(e -> {
			myStage.setScene(splashScene);
			animation.stop();
			animation.setRate(1);
		});

		HBox buttons = new HBox(start, stop, pause, step, speedUp, slowDown, switchSim);
		buttons.setPrefSize(currentSim.getSceneSize(), BUTTONHEIGHT);
		buttons.setLayoutY(currentSim.getSceneSize());
		buttons.getStyleClass().add("hbox");
		myScene.getStylesheets().add("style.css");

		currentSim.getRoot().getChildren().add(buttons);

	}

	private Scene splashScene() {
		Scene splash = new Scene(splashGroup, SIZE, SIZE);
		splash.setFill(Color.SLATEBLUE);
		ArrayList<Button> buttons = new ArrayList<Button>();
		buttons.add(new Button(labels.getString("Fire")));
		buttons.add(new Button(labels.getString("Segregation")));
		buttons.add(new Button(labels.getString("Predator")));
		buttons.add(new Button(labels.getString("Life")));

		VBox menu = new VBox();
		menu.setPrefSize(SPLASHSIZE, SPLASHSIZE);
		menu.setLayoutX((SIZE - SPLASHSIZE) / 2);
		menu.setLayoutY((SIZE - SPLASHSIZE) / 2);
		Text welcome = new Text(labels.getString("Select"));
		menu.getChildren().add(welcome);

		for (Button temp : buttons) {
			menu.getChildren().add(temp);
			temp.setMinWidth(120);

			temp.setOnMouseClicked(e -> {
				SimulationOptional simOption = new XMLReader("cellsociety/src/XML/" + temp.getText() + "XML.txt").getSimulation();
				if (!simOption.hasException()) {
					currentSim = simOption.getSimulation();
					myStage.setTitle(currentSim.getTitle());
					myScene = currentSim.init();
					myStage.setHeight(currentSim.getSceneSize() + BUTTONHEIGHT + BUTTONPADDING);
					addButtons();
					myStage.setScene(myScene);
				} else {
					String errorMessage = simOption.getExceptionMessage();
					handleError(errorMessage);
				
				}
			});
		}
		menu.getStyleClass().add("hbox");
		splash.getStylesheets().add("style.css");
		splashGroup.getChildren().add(menu);
		return splash;
	}

	public void handleError(String errorMessage) {
		Text msg = new Text(labels.getString("Error"));
        Button ok = new Button(labels.getString("OK"));
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
