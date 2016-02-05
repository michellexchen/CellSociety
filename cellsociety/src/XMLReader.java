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
	         int fishBreed = Integer.parseInt(getStringElement(doc,"fishbreed"));
	         int sharkBreed = Integer.parseInt(getStringElement(doc,"sharkbreed"));
	         int sharkDie = Integer.parseInt(getStringElement(doc,"sharkdie"));
	         int population = Integer.parseInt(getStringElement(doc,"population"));
	         double percentFish = Double.parseDouble(getStringElement(doc,"fishpercent"));
	         double percentShark = Double.parseDouble(getStringElement(doc,"sharkpercent"));
	         
	         return new Predator(fishBreed,sharkBreed,sharkDie,population,percentFish,percentShark);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
