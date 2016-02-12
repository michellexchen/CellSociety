import java.io.File;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Validator;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

//import javafx.scene.Node;
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
	private String file;
	private Document doc;
	
	private Validator validator;

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
			//reading file
			File inputFile = new File(file);
	        DocumentBuilderFactory dbFactory 
	            = DocumentBuilderFactory.newInstance();
	        dbFactory.setIgnoringElementContentWhitespace(true);
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();	        
	        doc = dBuilder.parse(inputFile);
	        
	        //simulation type
	        Element simulation2 = (Element) doc.getElementsByTagName("info").item(0) ;
	        String simType = simulation2.getElementsByTagName("name").item(0).getTextContent();	        
	        
	        //getting nodes
			NodeList listParam = doc.getElementsByTagName("parameters");
	        Element attributes = (Element) listParam.item(0);
	        
	        Simulation simulation;
	        
	        switch(simType){
	        case "Predator":
	        	simulation = getPredator(listParam, attributes);
	        	break;
	        case "Fire":
	        	simulation = getFire(listParam, attributes);
	        	break;
	        case "Segregation":
	        	simulation = getSegregation(listParam, attributes);
	        	break;
	        case "Life":
	        	simulation = getLife(listParam, attributes);
	        	break;
	        default: //IT'S HERE.. add return button OR create from here?
	        	return null;
	        }
	        return new SimulationOptional(simulation, null);
        }
		catch(Exception e){
			//System.out.println(e.getMessage());
			return new SimulationOptional(null, e);
		}
	}
	
	/**
	 * 
	 * @param attributes 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the predator attribute is read and returns the predator simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include fishBreed, sharkBreed, sharkDie, population, percentFish, size, and numCells
	 * 
	 */
	private Simulation getPredator(NodeList listParam, Element attributes){
	     
        Integer fishBreed = Integer.parseInt(attributes.getElementsByTagName("fishbreed").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer sharkBreed = Integer.parseInt(attributes.getElementsByTagName("sharkbreed").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer sharkDie = Integer.parseInt(attributes.getElementsByTagName("sharkdie").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer population = Integer.parseInt(attributes.getElementsByTagName("population").item(0).getChildNodes().item(0).getNodeValue().trim());
        Double percentFish = Double.parseDouble(attributes.getElementsByTagName("fishpercent").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer size = Integer.parseInt(attributes.getElementsByTagName("size").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer numCells = Integer.parseInt(attributes.getElementsByTagName("numcells").item(0).getChildNodes().item(0).getNodeValue().trim());
        
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
	private Simulation getFire(NodeList listParam, Element attributes){
        
        Double probCatch = Double.parseDouble(attributes.getElementsByTagName("probcatch").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer size = Integer.parseInt(attributes.getElementsByTagName("size").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer numCells = Integer.parseInt(attributes.getElementsByTagName("numcells").item(0).getChildNodes().item(0).getNodeValue().trim());
		 
		return new Fire(size, numCells, probCatch);
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
	private Simulation getSegregation(NodeList listParam, Element attributes){
        Integer population = Integer.parseInt(attributes.getElementsByTagName("popsize").item(0).getChildNodes().item(0).getNodeValue().trim());
        Double percent1 = Double.parseDouble(attributes.getElementsByTagName("percentone").item(0).getChildNodes().item(0).getNodeValue().trim());
        Double satisfaction = Double.parseDouble(attributes.getElementsByTagName("satisfaction").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer size = Integer.parseInt(attributes.getElementsByTagName("size").item(0).getChildNodes().item(0).getNodeValue().trim());
        Integer numCells = Integer.parseInt(attributes.getElementsByTagName("numcells").item(0).getChildNodes().item(0).getNodeValue().trim());
		
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
	private Simulation getLife(NodeList listParam, Element attributes){
		Integer numAlive = Integer.parseInt(attributes.getElementsByTagName("numalive").item(0).getChildNodes().item(0).getNodeValue().trim());		
		Integer size = Integer.parseInt(attributes.getElementsByTagName("size").item(0).getChildNodes().item(0).getNodeValue().trim());
	    Integer numCells = Integer.parseInt(attributes.getElementsByTagName("numcells").item(0).getChildNodes().item(0).getNodeValue().trim());
		
		return new Life(size,numCells,numAlive);
	}
}
