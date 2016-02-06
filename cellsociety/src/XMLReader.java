import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLReader {
	
	private String file;

	public XMLReader(String filename) {
		file = filename;
	}
	
	private String getStringElement(Document doc, String tagname){
		return ((Element) doc.getElementsByTagName(tagname).item(0)).getTextContent();
	}
	
	public Simulation getSimulation(){
		try{
			File inputFile = new File(file);
	        DocumentBuilderFactory dbFactory 
	            = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(inputFile);
	        String simulationType = doc.getDocumentElement().getNodeName();
	        switch(simulationType){
	        case "predator":
	        	return getPredator(doc);
	        case "fire":
	        	return getFire(doc);
	        case "segregation":
	        	return getSegregation(doc);
	        case "life":
	        	return getLife(doc);
	        default:
	        	return null; // throw exception?
	        }
        }
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private Simulation getPredator(Document doc){
		Element root = doc.getDocumentElement();
        int fishBreed = Integer.parseInt(root.getAttribute("fishbreed"));
        int sharkBreed = Integer.parseInt(root.getAttribute("sharkbreed"));
        int sharkDie = Integer.parseInt(root.getAttribute("sharkdie"));
        int population = Integer.parseInt(root.getAttribute("population"));
        double percentFish = Double.parseDouble(root.getAttribute("fishpercent"));
        double percentShark = Double.parseDouble(root.getAttribute("sharkpercent"));
		int size = Integer.parseInt(root.getAttribute("size"));
		int numCells = Integer.parseInt(root.getAttribute("numcells"));
        
        return new Predator(size,numCells,fishBreed,sharkBreed,sharkDie,population,percentFish,percentShark);
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
        double percent2 = Double.parseDouble(root.getAttribute("percenttwo"));
        double satisfaction = Double.parseDouble(root.getAttribute("satisfaction"));
		int size = Integer.parseInt(root.getAttribute("size"));
		int numCells = Integer.parseInt(root.getAttribute("numcells"));
		
		return new Segregation(size,numCells,population,percent1,percent2,satisfaction);
	}
	
	private Simulation getLife(Document doc){
		Element root = doc.getDocumentElement();
		int numAlive = Integer.parseInt(root.getAttribute("numalive"));
		int size = Integer.parseInt(root.getAttribute("size"));
		int numCells = Integer.parseInt(root.getAttribute("numcells"));
		
		return new Life(size,numCells,numAlive);
	}
}
