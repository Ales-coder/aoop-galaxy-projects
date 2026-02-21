package domain.galaxy.planet;

import java.util.LinkedList;
import java.util.List;

public enum TechnicalLevel { NONE("No tool users"),
							STONE("Stone Age"), 
							BRONZE("Bronze Age"), 
							IRON("Iron Age"), 
							MEDIEVAL("Dark Age or Medieval"), 
							RENAISSANCE("Rennaisance"), 
							INDUSTRIAL("Steam Engines or oil based economies"), 
							ELECTRIFIED("Electricity is the main source of power"), 
							COMPUTERIZED("Society has advanced information systems based on semicondictors or alike materials"), 
							SPACEFARING("Able to regularly travel space, but only sublight speeds"), 
							GATEUSER("Civilization has found stargates and is able to pass through them."),
							FASTERTHANLIGHT("Have invented ways to travel faster than light"), 
							MOREADVANCED("Society is more advanced than council space in ways we do not fully understand");

	private final String description;

	private TechnicalLevel(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static TechnicalLevel parse(String description) {
		if (description.equals("NONE")) {
			return TechnicalLevel.NONE;
		}
		if (description.equals("STONE")) {
			return TechnicalLevel.STONE;
		}
		if (description.equals("BRONZE")) {
			return TechnicalLevel.BRONZE;
		}
		if (description.equals("IRON")) {
			return TechnicalLevel.IRON;
		}
		if (description.equals("MEDIEVAL")) {
			return TechnicalLevel.MEDIEVAL;
		}
		if (description.equals("RENAISSANCE")) {
			return TechnicalLevel.RENAISSANCE;
		}
		if (description.equals("ELECTRIFIED")) {
			return TechnicalLevel.ELECTRIFIED;
		}
		if (description.equals("COMPUTERIZED")) {
			return TechnicalLevel.COMPUTERIZED;
		}
		if (description.equals("SPACEFARING")) {
			return TechnicalLevel.SPACEFARING;
		}
		if (description.equals("GATEUSER")) {
			return TechnicalLevel.GATEUSER;
		}
		if (description.equals("FASTERTHANLIGHT")) {
			return TechnicalLevel.FASTERTHANLIGHT;
		}
		if (description.equals("MOREADVANCED")) {
			return TechnicalLevel.MOREADVANCED;
		}
		return null;
	}
	
	public static List<String> getDatabaseScript() {
		List<String> result = new LinkedList<>();
		result.add("CREATE TABLE technical_level(level VARCHAR(255) PRIMARY KEY, description VARCHAR(255) NOT NULL);");
		TechnicalLevel[] levels = TechnicalLevel.values();
		for (TechnicalLevel level: levels) {
			result.add("INSERT INTO technical_level(level, description) VALUES (\"" + level.toString() + "\", \"" + level.getDescription() + "\");");
		}
		return result;		
	}
}
