public class SimulationOptional {
	private Simulation simulation;
	private Exception exception;
	public SimulationOptional(Simulation sim, Exception except) {
		simulation = sim;
		exception = except;
		// TODO Auto-generated constructor stub
	}
	
	public boolean hasException() {
		return exception != null;
	}
	
	public String getExceptionMessage() {
		return exception.getMessage();
	}
	
	public Simulation getSimulation() {
		return simulation;
	}

}
