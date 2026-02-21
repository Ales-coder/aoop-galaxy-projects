package domain.galaxy.planet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import app.Settings;

public enum PlanetType { D("Moon", "An uninhabitable planetoid, moon, or small planet with little to no atmosphere", Habitability.TERRAFORMING),
						H("Bear", "Generally uninhabitable for Humans due to gravity or climate, might be suitable for other species", Habitability.MARGINALLY),
						J("Saturn", "Gas Giant with no nuclear fusion", Habitability.NO),
						K("Mars", "Adaptable for Humans by use of artificial biospheres", Habitability.BIOSPHERE),
						L("Soggy", "Marginally habitable, with vegetation but usually no animal life", Habitability.MARGINALLY),
						M("Gaia", "Earth-like, with atmospheres containing oxygen. Largely habitable for humanoid life forms.", Habitability.YES),
						G("Dead", "This planet once was habitable, but for some reason is completely dead and sterile now", Habitability.BIOSPHERE),
						N("Cania", "Moon or small planet with atmosphere but very low temperatures", Habitability.BIOSPHERE),
						R("Bikini", "Moon orbiting within heavy radiation of a gas giant", Habitability.NO),
						T("Cronos", "Gas giant with some nuclear fusion at its core", Habitability.NO),
						Y("Demon", "A world with a toxic atmosphere and surface temperatures exceeding 500 Kelvin. Prone to thermic and/ or radiation discharges.", Habitability.NO);
	private final String nickname;
	private final String description;
	private final Habitability habitability;

	private PlanetType(String nickname, String description, Habitability habitability) {
		this.nickname = nickname;
		this.description = description;
		this.habitability = habitability;
	}

	public String getNickname() {
		return nickname;
	}

	public String getDescription() {
		return description;
	}

	public Habitability getHabitability() {
		return habitability;
	}
	
	public Map<Climate, Float> generateClimateMap(Planet p) {
		switch(this.toString()) {
			case "D": return getClimateD();
			case "H": return getClimateH(p);
			case "J": return getClimateJ();
			case "K": return getClimateK(p);
			case "L": return getClimateL();
			case "M": return getClimateM(p);
			case "N": return getClimateN();
			case "R": return getClimateR();
			case "T": return getClimateT();
			case "Y": return getClimateY();
			default: return null;
		}
	}
	
	private Map<Climate, Float> getClimateD() {
		Map<Climate, Float> result = new HashMap<>();
		result.put(Climate.SUBARCTIC, 100f);
		return result;
	}
	
	private Map<Climate, Float> getClimateH(Planet p) {
		Map<Climate, Float> result = new HashMap<>();
		float desert = Settings.instance().getRandom().nextFloat();
		result.put(Climate.DESERT, desert);
		if (p.getAverageDistanceToStar() < p.getStar().getGoldilockZoneStart()) {
			result.put(Climate.ARCTIC, Settings.instance().getRandom().nextFloat() * ((1f-desert) / 2));
			result.put(Climate.SUBARCTIC, Settings.instance().getRandom().nextFloat() * ((1f-desert) / 2));
		}
		else {
			result.put(Climate.FIERY, Settings.instance().getRandom().nextFloat() * ((1f-desert) / 2));
			result.put(Climate.SUPERFIERY, Settings.instance().getRandom().nextFloat() * ((1f-desert) / 2));
		}
		return result;
	}
	
	private Map<Climate, Float> getClimateJ() {
		Map<Climate, Float> result = new HashMap<>();
		result.put(Climate.CHEMICAL, 100f);
		return result;
	}
	
	private Map<Climate, Float> getClimateK(Planet p) {
		Map<Climate, Float> result = new HashMap<>();
		if (p.getAverageDistanceToStar() < p.getStar().getGoldilockZoneStart()) {
			result.put(Climate.ARCTIC, Settings.instance().getRandom().nextFloat() / 2f);
			result.put(Climate.SUBARCTIC, Settings.instance().getRandom().nextFloat() / 2f);
		}
		else {
			result.put(Climate.FIERY, 1f);
		}
		return result;
	}
	
	private Map<Climate, Float> getClimateL() {
		Map<Climate, Float> result = new HashMap<>();
		float desert = Settings.instance().getRandom().nextFloat() * 0.2f;
		result.put(Climate.DESERT, desert);
		float tropical = Settings.instance().getRandom().nextFloat() * 0.1f;
		result.put(Climate.TROPICAL, tropical);
		float chemical = Settings.instance().getRandom().nextFloat() * 0.05f;
		result.put(Climate.CHEMICAL, chemical);
		return result;
	}
	
	private Map<Climate, Float> getClimateM(Planet p) {
		Map<Climate, Float> result = new HashMap<>();
		
		if (p.getAverageDistanceToStar() < p.getStar().getGoldilockZoneStart()) {
			float arctic = Settings.instance().getRandom().nextFloat() * 0.1f;
			result.put(Climate.ARCTIC, arctic);
			float desert = Settings.instance().getRandom().nextFloat() * 0.1f;
			result.put(Climate.DESERT, desert);
			float temperate = Settings.instance().getRandom().nextFloat() * 0.2f;
			result.put(Climate.TEMPERATE, temperate);
			float tropical = Settings.instance().getRandom().nextFloat();
			if ((arctic + desert + tropical + temperate) > 1.0f) {
				result.put(Climate.TROPICAL, 1f-(arctic + desert + temperate));
			}
			else {
				result.put(Climate.TROPICAL, tropical);
			}
		}
		else if (p.getAverageDistanceToStar() > p.getStar().getGoldilockZoneEnd()) {
			result.put(Climate.ARCTIC, Settings.instance().getRandom().nextFloat() * 0.2f);
			result.put(Climate.DESERT, Settings.instance().getRandom().nextFloat() * 0.2f);
			result.put(Climate.TROPICAL, Settings.instance().getRandom().nextFloat() * 0.01f);
			result.put(Climate.TEMPERATE, Settings.instance().getRandom().nextFloat() * 0.5f);
		}
		else {
			result.put(Climate.ARCTIC, Settings.instance().getRandom().nextFloat() * 0.1f);
			result.put(Climate.DESERT, Settings.instance().getRandom().nextFloat() * 0.1f);
			result.put(Climate.TROPICAL, Settings.instance().getRandom().nextFloat() * 0.2f);
			result.put(Climate.TEMPERATE, Settings.instance().getRandom().nextFloat() * 0.5f);
		}
		return result;
	}
	
	private Map<Climate, Float> getClimateN() {
		Map<Climate, Float> result = new HashMap<>();
		result.put(Climate.SUBARCTIC, 100f);
		return result;
	}
	
	private Map<Climate, Float> getClimateR() {
		Map<Climate, Float> result = new HashMap<>();
		result.put(Climate.CHEMICAL, 100f);
		return result;
	}
	
	private Map<Climate, Float> getClimateT() {
		Map<Climate, Float> result = new HashMap<>();
		result.put(Climate.CHEMICAL, 100f);
		return result;
	}
	
	private Map<Climate, Float> getClimateY() {
		Map<Climate, Float> result = new HashMap<>();
		result.put(Climate.CHEMICAL, 100f);
		return result;
	}
	
	public static PlanetType parse(String description) {
		if (description.equals("D")) {
			return PlanetType.D;
		}
		if (description.equals("H")) {
			return PlanetType.H;
		}
		if (description.equals("J")) {
			return PlanetType.J;
		}
		if (description.equals("K")) {
			return PlanetType.K;
		}
		if (description.equals("L")) {
			return PlanetType.L;
		}
		if (description.equals("M")) {
			return PlanetType.M;
		}
		if (description.equals("N")) {
			return PlanetType.N;
		}
		if (description.equals("R")) {
			return PlanetType.R;
		}
		if (description.equals("T")) {
			return PlanetType.T;
		}
		if (description.equals("Y")) {
			return PlanetType.Y;
		}
		return null;
	}

	public enum Habitability { NO ("The planet is not habitable and cannot be made so by any known means."), 
								BIOSPHERE("Terraforming is impossible, but life can be sustained under an artificial biodome"), 
								TERRAFORMING ("Terraforming is possible, though probably expensive."), 
								MARGINALLY ("The planet is habitable, but will only be able to sustain a limited population."), 
								YES ("The planet is fully suitabable for habitation by humans and humanoid species without major modification."); 
		private final String description;
		
		private Habitability(String description) {
			this.description = description;
		}
		
		public String getDescription() {
			return this.description;
		}
	}
	
	public static List<String> getDatabaseScript() {
		List<String> result = new LinkedList<>();
		
		result.add("CREATE TABLE habitability(level VARCHAR(255) PRIMARY KEY, description VARCHAR(255) NOT NULL);");
		Habitability[] habits = Habitability.values();
		for (Habitability c: habits) {
			result.add("INSERT INTO habitability(level, description) VALUES (\"" + c.toString() + "\", \"" + c.getDescription() + "\");");
		}
		
		result.add("CREATE TABLE planet_type(type VARCHAR(255) PRIMARY KEY, nickname VARCHAR(255), description VARCHAR(255) NOT NULL, habitability VARCHAR(255) NOT NULL);");
		PlanetType[] types = PlanetType.values();
		for (PlanetType c: types) {
			result.add("INSERT INTO planet_type(type, nickname, description, habitability) VALUES (\"" + c.toString() + "\",\"" + 
											c.getNickname() + "\",\"" + 
											c.getDescription() + "\", \"" +
											c.getHabitability().toString() + "\");");
		}
		return result;		
	}
}
