
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000 / 2;
	private final double SPEED = .5;
	private final int BUTTONHEIGHT = 50;
	private final int BUTTONPADDING = 40;
	private final int SPLASHSIZE = 300;
	private final int SIZE = 500;

	private Stage myStage;
	private Scene myScene;
	private Simulation currentSim;
	private Timeline animation;
	private Group splashGroup = new Group();
	private Scene splashScene;

	@Override
	public void start(Stage gameStage) {
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
		Button start = new Button("Start");
		start.setOnMouseClicked(e -> animation.play());

		Button stop = new Button("Stop");
		stop.setOnMouseClicked(e -> animation.stop());

		Button pause = new Button("Pause");
		pause.setOnMouseClicked(e -> animation.pause());

		Button step = new Button("Step");
		step.setOnMouseClicked(e -> currentSim.step());

		Button speedUp = new Button("Speed Up");
		speedUp.setOnMouseClicked(e -> animation.setRate(animation.getCurrentRate() + SPEED));

		Button slowDown = new Button("Slow Down");
		slowDown.setOnMouseClicked(e -> {
			if (animation.getCurrentRate() > SPEED) {
				animation.setRate(animation.getCurrentRate() - SPEED);
			}
		});

		Button switchSim = new Button("Switch");
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
		splash.setFill(Color.GRAY);
		ArrayList<Button> buttons = new ArrayList<Button>();
		buttons.add(new Button("Fire"));
		buttons.add(new Button("Segregation"));
		buttons.add(new Button("Predator"));
		buttons.add(new Button("Life"));

		VBox menu = new VBox();
		menu.setPrefSize(SPLASHSIZE, SPLASHSIZE);
		menu.setLayoutX((SIZE - SPLASHSIZE) / 2);
		menu.setLayoutY((SIZE - SPLASHSIZE) / 2);
		Text welcome = new Text("Select a simulation below!");
		menu.getChildren().add(welcome);

		for (Button temp : buttons) {
			menu.getChildren().add(temp);
			temp.setMinWidth(120);

			temp.setOnMouseClicked(e -> {
				currentSim = new XMLReader("./src/XML/" + temp.getText() + "XML.txt").getSimulation();
				myStage.setTitle(currentSim.getTitle());
				myScene = currentSim.init();
				myStage.setHeight(currentSim.getSceneSize() + BUTTONHEIGHT + BUTTONPADDING);
				addButtons();
				myStage.setScene(myScene);
			});
		}

		menu.getStyleClass().add("hbox");
		splash.getStylesheets().add("style.css");
		splashGroup.getChildren().add(menu);
		return splash;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
