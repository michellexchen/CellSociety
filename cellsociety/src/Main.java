
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private final int MILLISECOND_DELAY = 1000/10;
	private Stage myStage;
	private final int SIZE = 800;

	private final int NUMCELLS = 100;
	//private Simulation currentSim = new Predator(3,6,2,200,0.75,0.25);//new Segregation(5000,0.25,0.75,0.50);
	private static Simulation currentSim;
	
	@Override
	public void start(Stage gameStage) {
		myStage  = gameStage;
		myStage.setTitle("Fire");
		
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
		 XMLParser xmlParser = new XMLParser("./cellsociety/src/FireXML");
		 Document doc = xmlParser.getDocument();
		 Element root = doc.getDocumentElement();
		 Double probCatch = Double.parseDouble(root.getAttribute("probCatch"));
		 Integer row = Integer.parseInt(root.getAttribute("rows"));
		 currentSim = new Fire(row, row, probCatch);
		 
		// currentSim = SimulationFactory.makeSimulation(filepath, simulationType);
		 launch(args);
     }
}
