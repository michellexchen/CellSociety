import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Michelle
 *
 * This class reads in XML files and passes the user input to the simulations.
 * In the case of faulty user input, XMLReader utilizes the SimulationOptional class to throw an error message on the scene in the main class.
 * 
 */

public class XMLReader {
	
	private String file;

	public XMLReader(String filename) {
		file = filename;
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
	        String simulationType = doc.getDocumentElement().getNodeName();
	        Simulation simulation;
	        switch(simulationType){
	        case "predator":
	        	simulation = getPredator(doc);
	        	break;
	        case "fire":
	        	simulation = getFire(doc);
	        	break;
	        case "segregation":
	        	simulation = getSegregation(doc);
	        	break;
	        case "life":
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
	private Simulation getPredator(Document doc){
		Element root = doc.getDocumentElement();
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
