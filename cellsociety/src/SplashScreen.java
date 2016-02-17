// This entire file is part of my masterpiece.
// Michelle Chen

/* This code creates the splash screens that allow the user to select what kind of simulation they want to see, as well as
 * specify aspects of the simulation. It was initially created out of fear that the main class was too messy and tried to 
 * do too much, but eventually turned into a much cleaner way for us to display the UI and read XML files to create simulations.
 * 
 * I think it shows development in my code; for the first assignment I didn't even know how to get a splashscreen up, let alone
 * display one after another. Class methods are short and method names clearly tell the user what the code is attempting to do. Variables
 * are all encapsulated and very few methods are public--in fact, most are private which is also indicative of better coding/design on my end.
 * This also shows off data abstraction by ensuring that fields are private and there are only getter and setter methods where absolutely needed.
 */


import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class SplashScreen {
	private VBox menu;
	private final int SPLASHSIZE = 400;
	private Group splashGroup;
	private ResourceBundle myResources = ResourceBundle.getBundle("Resources/English");
	private Button start;
	private Button upload;
	private Text welcome;
	private SimulationOptional simOption;
	private Scene splash;
	private VBox vb;
	private Text fill;
	private Label gridSize;
	private Label numCells;
	private Label gridType;
	private Label cellType;
	private TextField gridSizeDefault;
	private TextField numCellsDefault;
	private ComboBox gridOptions;
	private ComboBox cellOptions;
	private int gridSizeNum;
	private int numCellsNum;
	private Map myParams;
	private Text randomizeorNot;
	private Button random;
	private Button custom;
	

	public Scene SplashScreen(Main myMain, int SIZE) {
		setUp(SIZE);
		randomization(myMain);
		return formatting(splash, SIZE);
	}
	
	private void setUp(int SIZE){
		menu = new VBox();
		menu.setPrefSize(SPLASHSIZE, SPLASHSIZE);
		menu.setLayoutX((SIZE - SPLASHSIZE) / 2);
		menu.setLayoutY((SIZE - SPLASHSIZE) / 2);
		menu.getStyleClass().add("hbox");
		splashGroup = new Group();
		splashGroup.getChildren().add(menu);
	}
	
	private void randomization(Main myMain){ 
		randomizeorNot = new Text(myResources.getString("Welcome"));
		custom = new Button(myResources.getString("Custom"));
		random = new Button(myResources.getString("Random"));
		menu.getChildren().addAll(randomizeorNot, custom, random);

		random.setOnMouseClicked(e -> {
			menu.getChildren().removeAll(randomizeorNot, custom, random);
			enterParams();
			uploadParams(myMain);
		});

		custom.setOnMouseClicked(e->{
			menu.getChildren().removeAll(randomizeorNot, custom, random);
			uploadXML(myMain);
		});
	}
	
	private Scene formatting(Scene splash, int SIZE){
		splash = new Scene(splashGroup, SIZE, SIZE);
		splash.setFill(Color.SLATEBLUE);
		splash.getStylesheets().add("Resources/style.css");
		return splash;
	}
	
	private void uploadXML(Main myMain){
		menu.getChildren().removeAll(vb, welcome, upload, fill);
		welcome = new Text(myResources.getString("Select"));
		start = new Button(myResources.getString("Upload"));
		menu.getChildren().addAll(welcome, start);
		
		start.setOnMouseClicked(e -> {
				simOption = new XMLReader().getSimulation(myParams);
				myMain.setSimOption(simOption); 
				if (simOption == null) { 
					noSimulation();
					return;
				}
				
				try{
					myMain.startystart();
					} catch (Exception e2) {
						String errorMessage = simOption.getExceptionMessage();
						handleError(errorMessage);		
				}			
		});
	}
	
	private void enterParams(){ 
		fill = new Text(myResources.getString("FillFields"));
		menu.getChildren().add(fill);
		
		vb = new VBox();
		menu.getChildren().add(vb);
		vb.getStyleClass().add("hbox");
		
		gridSizeField();
		numCellsField();
		gridTypeDropdown();
		cellTypeDropdown();
	
	}
	
	private void gridSizeField(){
		gridSize = new Label(myResources.getString("gridSize"));
		gridSizeDefault = new TextField();
		gridSizeDefault.setMaxWidth(100);
		gridSizeDefault.setText("500");
		gridSizeDefault.setAlignment(Pos.CENTER);
		vb.getChildren().addAll(gridSize, gridSizeDefault);
	}
	
	private void numCellsField(){
		numCells = new Label(myResources.getString("numCells"));
		numCellsDefault = new TextField();
		numCellsDefault.setMaxWidth(100);
		numCellsDefault.setText("100");
		numCellsDefault.setAlignment(Pos.CENTER);
		vb.getChildren().addAll(numCells, numCellsDefault);
	}
	
	private void gridTypeDropdown(){
		gridType = new Label(myResources.getString("gridType"));
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "Finite",
			        "Toroidal"
			    );
		gridOptions = new ComboBox(options);
		gridOptions.getSelectionModel().select("Finite");
		gridOptions.setPrefWidth(100);
		vb.getChildren().addAll(gridType, gridOptions);
	}
	
	private void cellTypeDropdown(){
		cellType = new Label(myResources.getString("cellType"));
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "Square",
			        "Triangle"
			    );
		cellOptions = new ComboBox(options);
		cellOptions.getSelectionModel().select("Square");
		cellOptions.setPrefWidth(100);
		vb.getChildren().addAll(cellType, cellOptions);
	}
	

	private void uploadParams(Main myMain){
		upload = new Button(myResources.getString("OK"));
		upload.setMinWidth(115);
		menu.getChildren().add(upload);

		upload.setOnMouseClicked(e -> {
			try {
				gridSizeNum = Integer.parseInt(gridSizeDefault.getText());
				try{
					numCellsNum = Integer.parseInt(numCellsDefault.getText());
					setParams();
					uploadXML(myMain);
					
				} catch(Exception E) {
					paramErrorCell();
				}
			} catch(Exception E) {
				paramErrorGrid();
			}
		});
	}
	
	private void setParams(){
		myParams = new HashMap<String, String>();
		myParams.put("gridSize", gridSizeDefault.getText());
		myParams.put("numCells", numCellsDefault.getText());
		myParams.put("gridType", gridOptions.getValue().toString());
		myParams.put("cellType", cellOptions.getValue().toString());
	}
	
	public Map getParams(){
		return myParams;
	}
	
	private void paramErrorGrid(){
		Text msg = new Text(myResources.getString("paramGrid"));
		Button ok = new Button(myResources.getString("OK"));	
		clearError(msg, ok);
		pressedOK(ok, msg);
	}
	
	private void paramErrorCell(){
		Text msg = new Text(myResources.getString("paramCell"));
		Button ok = new Button(myResources.getString("OK"));	
		clearError(msg, ok);
		pressedOK(ok, msg);
	}
	
	private void clearError(Text msg, Button ok){
		menu.getChildren().addAll(msg, ok);
		menu.getChildren().removeAll(vb, welcome, fill);
	}
	
	private void pressedOK(Button ok, Text msg){ 
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				menu.getChildren().removeAll(msg, ok);
				menu.getChildren().addAll(fill, vb);
			}
		});
		
	}
	
	private void handleError(String errorMessage) {
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
	
	private void clearForError(Text msg, Text msg2, Button ok){ //clear screen to display error msg
		menu.getChildren().addAll(msg, msg2, ok);
		menu.getChildren().removeAll(welcome, start);
	}
	
	private void pressedOK(Button ok, Text msg, Text msg2){ //after user presses ok, display uploader
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				menu.getChildren().removeAll(msg, msg2, ok);
				menu.getChildren().addAll(welcome, start);
			}
		});
		
	}
}
