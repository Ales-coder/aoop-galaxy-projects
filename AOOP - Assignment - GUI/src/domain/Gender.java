package domain;

public enum Gender { MALE("M", "Gender producing the smaller reproductive cell"), 
					 FEMALE("F", "Gender producing the larger reproductive cell"), 
					 OTHER("O", "Catch-all for situations where the reproductive structure is unclear"), 
					 UNKNOWN("U", "We have'nt got the foggiest");
	private String abbreviation;
	private String description;
	
	private Gender(String abbreviation, String description) {
		this.abbreviation = abbreviation;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public String getAbbreviation() {
		return abbreviation;
	}
	
	public static Gender parse(String description) {
		if (description.toUpperCase().equals("M")) {
			return Gender.MALE;
		}
		if (description.toUpperCase().equals("F")) {
			return Gender.FEMALE;
		}
		if (description.toUpperCase().equals("O")) {
			return Gender.OTHER;
		}
		if (description.toUpperCase().equals("U")) {
			return Gender.UNKNOWN;
		}
		return null;
	}
}
