
public class Default {

	public static final String PROBCATCH = ".75";
	public static final String NUMALIVE = "1";
	public static final String FISHBREED = "1";
	public static final String SHARKBREED = "0";
	public static final String SHARKDIE = "7";
	public static final String POPULATION = "1";
	public static final String FISHPERCENT = "1.0";
	public static final String POPSIZE = "1";
	public static final String SATISFACTION = "1.0";
	public static final String PERCENTONE = "1.0";
	
	public Default() {
	}
	
	public static String getDefault(String param) {
		switch(param){
			case "probcatch":
				return PROBCATCH;
			case "numalive":
				return NUMALIVE;
			case "fishbreed":
				return FISHBREED;
			case "sharkbreed":
				return SHARKBREED;
			case "sharkdie":
				return SHARKDIE;
			case "population":
				return POPULATION;
			case "fishpercent":
				return FISHPERCENT;
			case "popsize":
				return POPSIZE;
			case "satisfaction":
				return SATISFACTION;
			case "percentone":
				return PERCENTONE;
		}
		return "";
		
	}

}
