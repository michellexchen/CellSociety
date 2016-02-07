import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLReader {
	
	private String file;

	public XMLReader(String filename) {
		file = filename;
	}
	
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
	
	private Simulation getFire(Document doc){
		 Element root = doc.getDocumentElement();
		 Double probCatch = Double.parseDouble(root.getAttribute("probcatch"));
		 Integer size = Integer.parseInt(root.getAttribute("size"));
		 Integer numCells = Integer.parseInt(root.getAttribute("numcells"));
		 
		 return new Fire(size,numCells,probCatch);
	}
	private Simulation getSegregation(Document doc){
		Element root = doc.getDocumentElement();
        int population = Integer.parseInt(root.getAttribute("popsize"));
        double percent1 = Double.parseDouble(root.getAttribute("percentone"));
        double satisfaction = Double.parseDouble(root.getAttribute("satisfaction"));
		int size = Integer.parseInt(root.getAttribute("size"));
		int numCells = Integer.parseInt(root.getAttribute("numcells"));
		
		return new Segregation(size,numCells,population,percent1,satisfaction);
	}
	
	private Simulation getLife(Document doc){
		Element root = doc.getDocumentElement();
		int numAlive = Integer.parseInt(root.getAttribute("numalive"));
		int size = Integer.parseInt(root.getAttribute("size"));
		int numCells = Integer.parseInt(root.getAttribute("numcells"));
		
		return new Life(size,numCells,numAlive);
	}
}
