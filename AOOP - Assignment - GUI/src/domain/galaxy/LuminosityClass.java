package domain.galaxy;

import java.util.LinkedList;
import java.util.List;

public enum LuminosityClass { O("Hypergiant"), 
								I("Supergiant"), 
								II("Bright giant"), 
								III("Regular giant"), 
								IV("Subgiant"), 
								V("Main-sequence star"),
								VI("Subdwarf"),
								VII("White dwarf"),
								VIII("Black Hole"),
								IX("Neutron Star");
	private final String description;

	private LuminosityClass(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public static LuminosityClass parse(String description) {
		if (description.startsWith("O")) {
			return LuminosityClass.O;
		}
		if (description.startsWith("VIII")) {
			return LuminosityClass.VIII;
		}
		if (description.startsWith("IX")) {
			return LuminosityClass.IX;
		}
		if (description.startsWith("VII")) {
			return LuminosityClass.VII;
		}
		if (description.startsWith("VI")) {
			return LuminosityClass.VI;
		}
		if (description.startsWith("V")) {
			return LuminosityClass.V;
		}
		if (description.startsWith("IV")) {
			return LuminosityClass.IV;
		}
		if (description.startsWith("III")) {
			return LuminosityClass.III;
		}
		if (description.startsWith("II")) {
			return LuminosityClass.II;
		}
		if (description.startsWith("I")) {
			return LuminosityClass.I;
		}
		return LuminosityClass.V;
	}
	
	public static List<String> getDatabaseScript() {
		List<String> result = new LinkedList<>();
		result.add("CREATE TABLE luminosity_class(class VARCHAR(255) PRIMARY KEY, description VARCHAR(255) NOT NULL);");
		LuminosityClass[] classes = LuminosityClass.values();
		for (LuminosityClass c: classes) {
			result.add("INSERT INTO luminosity_class(class, description) VALUES (\"" + c.toString() + "\", \"" + c.getDescription() + "\");");
		}
		return result;		
	}
}
