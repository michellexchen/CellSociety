import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Validator;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

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
	
	private Integer gridSize;
	private Integer numCells;
	private Boolean gridType;
	private Boolean cellType;

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
	
	public SimulationOptional getSimulation(Map myParams){
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
	        
	        parseMap(myParams);
	        
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
			return new SimulationOptional(null, e);
		}
	}
	
	
	
	private void parseMap(Map myParams){
		 gridSize = Integer.parseInt((String) myParams.get("gridSize"));
	     numCells = Integer.parseInt((String) myParams.get("numCells"));
	     if (myParams.get("gridType") == "Finite") {
	    	 gridType = false;
	     } else {
	    	 gridType = true;
	     }
	     if (myParams.get("cellType") == "Square") {
	    	 cellType = false;
	     } else {
	    	 cellType = true;
	     }
	}
	
	private String getNodeValue(Element attributes, String tagName){ // THIS IS A CHECK FOR WHEN IT'S EMPTY. CHECK FOR WHEN DONT INCLUDE FOR WHEN ITS EMPTY
		if (attributes.getElementsByTagName(tagName).item(0).getChildNodes().getLength() == 0) {
			return Default.getDefault(tagName);
		}
		return attributes.getElementsByTagName(tagName).item(0).getChildNodes().item(0).getNodeValue().trim();
	}
	
	/**
	 * 
	 * @param attributes 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the predator attribute is read and returns the predator simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include fishBreed, sharkBreed, sharkDie, population, percentFish, gridSize, and numCells
	 * @throws Exception 
	 * 
	 */
	private Simulation getPredator(NodeList listParam, Element attributes) throws Exception{
        Integer fishBreed = Integer.parseInt(getNodeValue(attributes, "fishbreed"));
        Integer sharkBreed = Integer.parseInt(getNodeValue(attributes, "sharkbreed"));
        Integer sharkDie = Integer.parseInt(getNodeValue(attributes, "sharkdie"));
        Integer population = Integer.parseInt(getNodeValue(attributes, "population"));
        Double percentFish = Double.parseDouble(getNodeValue(attributes, "fishpercent"));
        
        if(population > numCells*numCells){
        	System.out.println("error!!!");
        	Exception e = new Exception();
        	throw new Exception("The population is too large for this grid!");
        }
        
        return new Predator(gridSize,numCells,fishBreed,sharkBreed,sharkDie,population,percentFish, gridType, cellType);
	}	
	
	
	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the fire attribute is read and returns the fire simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include probCatch, gridSize, and numCells
	 * 
	 */
	private Simulation getFire(NodeList listParam, Element attributes){
		System.out.println("i'm here");
//	    if (getNodeValue(attributes, "custom").toString() == "true") {
//	    	System.out.println("hehehahahohoh");
//	    }
        Double probCatch = Double.parseDouble(getNodeValue(attributes, "probcatch"));
		 
		return new Fire(gridSize, numCells, probCatch, gridType, cellType);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the segregation attribute is read and returns the segregation simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include population, percent1, satisfaction, gridSize, and numCells
	 * @throws Exception 
	 * 
	 */
	private Simulation getSegregation(NodeList listParam, Element attributes) throws Exception{

        Integer population = Integer.parseInt(getNodeValue(attributes, "popsize"));
        Double percent1 = Double.parseDouble(getNodeValue(attributes, "percentone"));
        Double satisfaction = Double.parseDouble(getNodeValue(attributes, "satisfaction"));
		if(population > gridSize*gridSize){
			throw new Exception("Population is too large for grid!");
		}
        
		return new Segregation(gridSize,numCells,population,percent1,satisfaction, gridType, cellType);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * 
	 * This method is called in getSimulation() when the life attribute is read and returns the life simulation based on user inputed attributes from the XML file
	 * Attributes included in the XML file include numAlive, gridSize, and numCells
	 * @throws Exception 
	 * 
	 */
	private Simulation getLife(NodeList listParam, Element attributes) throws Exception{
		Integer numAlive = Integer.parseInt(getNodeValue(attributes, "numalive"));		
		if(numAlive > gridSize*gridSize){
			throw new Exception("Population is too large for grid!");

		}
		return new Life(gridSize,numCells,numAlive, gridType, cellType);
	}
}
