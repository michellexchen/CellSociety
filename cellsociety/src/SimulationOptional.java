/**
 * 
 * @author Michelle
 * 
 *  This class helps the XMLReader class protect itself against faulty user input (empty/incorrect values).
 *  If no simulation is returned, exception is set to null and the error message is called in the XMLReader class.
 *  Otherwise, the simulation is set by the XMLReader class and the exception is set to null.
 */

public class SimulationOptional {
	private Simulation simulation;
	private Exception exception;
	
	
//	•	no simulation type given
//	•	default values when parameter values are not given
//	•	invalid cell state values given
//	•	cell locations given that are outside the bounds of the grid's size
// • incorrect information
	
	public SimulationOptional(Simulation sim, Exception except) {
		simulation = sim;
		exception = except;
	}
	
	/**
	 * 
	 * @return
	 * Returns true if no simulation returned (ie XML file can't be read; exception is set to null)
	 * Returns false if simulation found
	 * 
	 */
	public boolean hasException() {
		return exception != null;
	}
	
	/**
	 * 
	 * @return
	 * Returns error message if no simulation found/XML file can't be read
	 */
	public String getExceptionMessage() {
		return exception.getMessage();
	}
	
	/**
	 * 
	 * @return
	 * Returns simulation
	 * If XML file is corrupt, simulation is set to null in the XML reader class
	 */
	public Simulation getSimulation() {
		return simulation;
	}

}
