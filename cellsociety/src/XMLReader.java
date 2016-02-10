import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
//import org.w3c.dom.Node;

import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * 
 * @author Michelle
 *
 * This class reads in XML files and passes the user input to the simulations.
 * In the case of faulty user input, XMLReader utilizes the SimulationOptional class to throw an error message on the scene in the main class.
 * 
 * XML files contain the following info:
 * --name of the simulation it represents, as well as a title for the simulation and the simulation's "author"
 * --settings for global configuration parameters specific to the simulation
 * --dimensions of the grid and the initial configuration of the states for the cells in the grid
 * 
 */

public class XMLReader {
	private Stage myStage;
	private String file;
	private Element root;

	public XMLReader() {
		file = chooseFile();
	}

	public String chooseFile(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open XML File");		
		fileChooser.getExtensionFilters().addAll( //xml file check
		        new ExtensionFilter("XML Files", "*.xml"));
		
		File file = fileChooser.showOpenDialog(null);

		String fileName = "";
		if (file != null) {
			fileName = file.getPath();
		}
		return fileName;
	}
	
	

	/**
	 * @return
	 * This method reads the XML file and calls a simulation specific method to return a simulation based on the information contained within the file.
	 * If the XML file contains faulty user input, the method will instead return an error message to be displayed on the Stage in the main method.
	 */
	
	public SimulationOptional getSimulation(){
		try{
			File inputFile = new File(file);
	        DocumentBuilderFactory dbFactory 
	            = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        
	        Document doc = dBuilder.parse(inputFile);
	        root = doc.getDocumentElement();	        
	        //refactor.. gets simulation name
	        Element simulation2 = (Element) doc.getElementsByTagName("simulation").item(0) ; 
	        String blah = simulation2.getElementsByTagName("name").item(0).getTextContent();
	        //System.out.println(blah);	 
	        
	        Element blah2 = (Element) doc.getElementsByTagName("simulation").item(1);
	        String b3 = blah2.getElementsByTagName("probcatch").item(0).getTextContent();
	        System.out.println("rly");
	        System.out.println(b3);
	        
	        Simulation simulation;
	        
	        switch(blah){
	        case "Predator":
	        	simulation = getPredator(root, doc);
	        	break;
	        case "Fire":
	        	simulation = getFire(doc);
	        	break;
	        case "Segregation":
	        	simulation = getSegregation(doc);
	        	break;
	        case "Life":
	        	simulation = getLife(doc);
	        	break;
	        default:
	        	return null;
	        }
	        return new SimulationOptional(simulation, null);
        }
		catch(Exception e){

			return new SimulationOptional(null, e);
		}
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the predator attribute is read and returns the predator simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include fishBreed, sharkBreed, sharkDie, population, percentFish, size, and numCells
	 * 
	 */
	private Simulation getPredator(Element root, Document doc){
        int fishBreed = Integer.parseInt(root.getAttribute("fishbreed"));
        int sharkBreed = Integer.parseInt(root.getAttribute("sharkbreed"));
        int sharkDie = Integer.parseInt(root.getAttribute("sharkdie"));
        int population = Integer.parseInt(root.getAttribute("population"));
        double percentFish = Double.parseDouble(root.getAttribute("fishpercent"));
		int size = Integer.parseInt(root.getAttribute("size"));
		int numCells = Integer.parseInt(root.getAttribute("numcells"));
        
        return new Predator(size,numCells,fishBreed,sharkBreed,sharkDie,population,percentFish);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the fire attribute is read and returns the fire simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include probCatch, size, and numCells
	 * 
	 */
	private Simulation getFire(Document doc){
		 Element root = doc.getDocumentElement();
		 Double probCatch = Double.parseDouble(root.getAttribute("probcatch"));
		 Integer size = Integer.parseInt(root.getAttribute("size"));
		 Integer numCells = Integer.parseInt(root.getAttribute("numcells"));
		 
		 return new Fire(size,numCells,probCatch);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the segregation attribute is read and returns the segregation simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include population, percent1, satisfaction, size, and numCells
	 * 
	 */
	private Simulation getSegregation(Document doc){
		Element root = doc.getDocumentElement();
        int population = Integer.parseInt(root.getAttribute("popsize"));
        double percent1 = Double.parseDouble(root.getAttribute("percentone"));
        double satisfaction = Double.parseDouble(root.getAttribute("satisfaction"));
		int size = Integer.parseInt(root.getAttribute("size"));
		int numCells = Integer.parseInt(root.getAttribute("numcells"));
		
		return new Segregation(size,numCells,population,percent1,satisfaction);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the life attribute is read and returns the life simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include numAlive, size, and numCells
	 * 
	 */
	private Simulation getLife(Document doc){
		Element root = doc.getDocumentElement();
		int numAlive = Integer.parseInt(root.getAttribute("numalive"));
		int size = Integer.parseInt(root.getAttribute("size"));
		int numCells = Integer.parseInt(root.getAttribute("numcells"));
		
		return new Life(size,numCells,numAlive);
	}
}
