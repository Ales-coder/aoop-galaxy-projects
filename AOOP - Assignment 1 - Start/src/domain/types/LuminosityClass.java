package domain.types;

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
}
