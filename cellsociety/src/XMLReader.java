import java.io.File;
import java.util.ArrayList;
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
	
	private String simType;
    private Simulation simulation;
    private NodeList listParam;
    private Element attributes;
    
	private List<String> columns;
	private NodeList colTag;

	public XMLReader() {
		file = chooseFile();
	}

	public String chooseFile(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open XML File");		
		fileChooser.getExtensionFilters().addAll(
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
	        simType = simulation2.getElementsByTagName("name").item(0).getTextContent();	        	        
	        //getting nodes
			listParam = doc.getElementsByTagName("parameters");
	        attributes = (Element) listParam.item(0);
	        if (attributes.getElementsByTagName("custom").getLength() == 0) {
	        	parseRandom(myParams);
		        return randomSim();
	        } else {
	        	parseCustom(attributes);
	        	return customSim();
	        }
		} catch(Exception e){	
			return new SimulationOptional(null, e);
		}			
	}
	


	private SimulationOptional randomSim() throws Exception{
		switch(simType){
        case "Predator":
        	simulation = getPredator(attributes);
        	break;
        case "Fire":
        	simulation = getFire(attributes);
        	break;
        case "Segregation":
        	simulation = getSegregation(attributes);
        	break;
        case "Life":
        	simulation = getLife(attributes);
        	break;
        case "Slime":
        	simulation = getSlime(attributes);
        	break;
        case "Ant":
        	simulation = getAntCustom(attributes);
        	break;
        default: 
        	return null;
        }
        return new SimulationOptional(simulation, null);
    }

	private SimulationOptional customSim() throws Exception{
		switch(simType){
        case "Predator":
        	simulation = getPredatorCustom(attributes);
        	break;
        case "Fire":
        	simulation = getFireCustom(attributes);
        	break;
        case "Segregation":
        	simulation = getSegregationCustom(attributes);
        	break;
        case "Life":
        	simulation = getLifeCustom(attributes);
        	break;
      case "Slime":
    	  simulation = getSlimeCustom(attributes);
        	break;
      case "Ant":
        	simulation = getAntCustom(attributes);
        	break;
        default: 
        	return null;
        }
        return new SimulationOptional(simulation, null);
    }

	
	private void parseRandom(Map myParams){
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
	
	private void parseCustom(Element attributes) {
		String isTor = getNodeValue(attributes, "gridtype");
		String isTri = getNodeValue(attributes, "celltype");
		gridSize = Integer.parseInt(getNodeValue(attributes, "gridsize"));

		if (attributes.getElementsByTagName("gridtype").getLength() > 0 && isTor.equals("toroidal")) {
				gridType = true;
	     } else {
	    	 gridType = false;
	     }
		
		if (attributes.getElementsByTagName("celltype").getLength() > 0 && isTri.equals("triangle")) {
			cellType = true;
		} else {
			cellType = false;
		}			
		
	}
	
	private String getNodeValue(Element attributes, String tagName){ // THIS IS A CHECK FOR WHEN IT'S EMPTY. CHECK FOR WHEN DONT INCLUDE FOR WHEN ITS EMPTY
		if (attributes.getElementsByTagName(tagName).item(0).getChildNodes().getLength() == 0) {
			return Default.getDefault(tagName);
		}
		return attributes.getElementsByTagName(tagName).item(0).getChildNodes().item(0).getNodeValue().trim();
	}
	
	private List<String> columns(Element attributes){
		colTag = doc.getElementsByTagName("col");
		columns = new ArrayList<String>();
		for (int i = 0; i < colTag.getLength(); i++) {
			Node node = colTag.item(i);
			Element eElement = (Element) node;
			columns.add(eElement.getAttribute("states"));
		}
		return columns;
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
	private Simulation getPredator(Element attributes)throws Exception{
		Integer fishBreed = Integer.parseInt(getNodeValue(attributes, "fishbreed"));
	    Integer sharkBreed = Integer.parseInt(getNodeValue(attributes, "sharkbreed"));
	    Integer sharkDie = Integer.parseInt(getNodeValue(attributes, "sharkdie"));
	    Integer population = Integer.parseInt(getNodeValue(attributes, "population"));
	    Double percentFish = Double.parseDouble(getNodeValue(attributes, "fishpercent"));        

        if(population > numCells*numCells){
        	Exception e = new Exception();
        	throw new Exception("The population is too large for this grid!");
        }
        
        return new Predator(gridSize,numCells,fishBreed,sharkBreed,sharkDie,population,percentFish, gridType, cellType);
	}	
		
	private Simulation getPredatorCustom(Element attributes)throws Exception{
		columns(attributes);
		Integer fishBreed = Integer.parseInt(getNodeValue(attributes, "fishbreed"));
	    Integer sharkBreed = Integer.parseInt(getNodeValue(attributes, "sharkbreed"));
	    Integer sharkDie = Integer.parseInt(getNodeValue(attributes, "sharkdie"));      
        
        return new Predator(columns, gridSize,fishBreed,sharkBreed,sharkDie, gridType, cellType);
	}	
	
	
	private Simulation getSlime(Element attributes) throws Exception{
		Integer numSlime = Integer.parseInt(getNodeValue(attributes, "numslime"));
        if(numSlime > numCells*numCells){
        	Exception e = new Exception();
        	throw new Exception("The population is too large for this grid!");
        }
        
        return new Slime(gridSize, numCells, gridType, cellType, numSlime);
	}
	
	private Simulation getSlimeCustom(Element attributes){
		columns(attributes);
		return new Slime(columns, gridSize, gridType, cellType);
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
	private Simulation getFire(Element attributes){
        Double probCatch = Double.parseDouble(getNodeValue(attributes, "probcatch"));
		return new Fire(gridSize, numCells, probCatch, gridType, cellType);
	}
	
	private Simulation getFireCustom(Element attributes) {
		columns(attributes);
        Double probCatch = Double.parseDouble(getNodeValue(attributes, "probcatch"));
		return new Fire(columns, gridSize, probCatch, gridType, cellType);
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

	private Simulation getSegregation( Element attributes) throws Exception{


        Integer population = Integer.parseInt(getNodeValue(attributes, "popsize"));
        Double percent1 = Double.parseDouble(getNodeValue(attributes, "percentone"));
        Double satisfaction = Double.parseDouble(getNodeValue(attributes, "satisfaction"));
		if(population > gridSize*gridSize){
			throw new Exception("Population is too large for grid!");
		}
        
		return new Segregation(gridSize,numCells,population,percent1,satisfaction, gridType, cellType);
	}
	
	
	
	private Simulation getSegregationCustom(Element attributes){
		columns(attributes);
        Double satisfaction = Double.parseDouble(getNodeValue(attributes, "satisfaction"));
		
		return new Segregation(columns, gridSize, satisfaction, gridType, cellType);
		
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

	private Simulation getLife(Element attributes) throws Exception{

		Integer numAlive = Integer.parseInt(getNodeValue(attributes, "numalive"));		
		if(numAlive > gridSize*gridSize){
			throw new Exception("Population is too large for grid!");

		}
		return new Life(gridSize,numCells,numAlive, gridType, cellType);
	}
	
	private Simulation getLifeCustom(Element attributes){
		columns(attributes);
		return new Life(columns, gridSize, gridType, cellType);
	}
	


	private Simulation getAntCustom(Element attributes) {
		columns(attributes);
		Integer maxAnts = Integer.parseInt(getNodeValue(attributes, "maxants"));
		Integer antLife = Integer.parseInt(getNodeValue(attributes, "antlife"));
		Integer antBreed = Integer.parseInt(getNodeValue(attributes, "antbreed"));
		Integer numNest = Integer.parseInt(getNodeValue(attributes, "numnest"));
		Double minPher = Double.parseDouble(getNodeValue(attributes, "minpher"));
		Double maxPher = Double.parseDouble(getNodeValue(attributes, "maxpher"));
		Double evaporation = Double.parseDouble(getNodeValue(attributes, "evaporation"));
		Double diffusion = Double.parseDouble(getNodeValue(attributes, "diffusion"));
		Double k = Double.parseDouble(getNodeValue(attributes, "k"));
		Double n = Double.parseDouble(getNodeValue(attributes, "n"));
		
		return new Ants(columns, gridSize, gridType, cellType, maxAnts, antLife, antBreed, numNest, minPher, maxPher, evaporation, diffusion, k, n);
	}
	
}
