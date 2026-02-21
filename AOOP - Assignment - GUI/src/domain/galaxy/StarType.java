package domain.galaxy;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import app.Settings;

public enum StarType { W("Wolf-Rayet star", 50000, 150000, Color.MAGENTA, 16f, 22f, 0.00000003, 0, 0, 0, 0, 0),
						C("Red Giant with more carbon than oxygen in its atmosphere", 3450, 5100, Color.RED, 0.08f, 1.04f, 0.0009, 5, 3, 15, 200, 0.001),
						S("Cool Red Giant with more carbon than oxygen in its atmosphere", 2400, 3450, Color.RED, 1.04f, 2.1f, 0.0004, 5, 3, 15, 200, 0.001),
						H("Black Hole, an ultradense body made from totally collapsed degenerate matter", 0, 1000000, Color.BLACK, 6f, 100000f, 0.00009, 0, 0, 0, 0, 0),
						N("Neutron Star, an ultradense body made from degenerate matter", 0, 1000000, Color.BLACK, 6f, 100000f, 0.00009, 0, 0, 0, 0, 0),
						P("Pulsar, a neutron star rotating very fast", 50000, 1000000, Color.BLACK, 6f, 100000f, 0.00009, 0, 0, 0, 0, 0),
						O("O Class", 30000, 60000, Color.BLUE, 16f, 22f, 0.00000003, 500, 60, 100, 100000, 0.01),
						B("B Class", 10000, 30000, Color.CYAN, 2.1f, 16f, 0.001, 300, 50, 12, 200, 0.1),
						A("A Class", 7500, 10000, Color.WHITE, 1.4f, 2.1f, 0.03, 200, 30, 1, 200, 0.3),
						F("F Class", 6000, 7500, Color.GREEN, 1.04f, 1.4f, 0.06, 50, 25, 0.7, 150, 0.5),
						G("G Class", 5200, 6000, Color.YELLOW, 0.8f, 1.04f, 0.076, 20, 10, 0.4, 120, 0.7),
						K("K Class", 3700, 5200, Color.ORANGE, 0.45f, 0.8f, 0.121, 20, 10, 0.2, 80, 0.5),
						M("M Class", 2400, 3700, Color.RED, 0.08f, 0.45f, 0.764, 20, 10, 0.1, 50, 0.1),
						D("White Dwarf, made from degenerate matter", 75000, 10000, Color.WHITE, 1.4f, 2.1f, 0.009, 5, 3, 0.05, 200, 0.001),
						R("Brown Dwarf, made from degenerate matter", 1000, 4, new Color(25, 25, 25), 1.4f, 2.1f, 0.009, 5, 3, 0.05, 200, 0.001);

	private final String description;
	private final int minTemperature;
	private final int maxTemperature;
	private final Color color;
	private final float minMass; 
	private final float maxMass; 
	private final double fraction;
	private final int numberOfPlanets;
	private final int variationOfPlanets;
	private final double minDistanceOfPlanets;
	private final double maxDistanceOfPlanets;
	private final double chanceOfHabitable;
	private static StarType[] distro = null;
	
	private StarType(String description, int minTemperature, int maxTemperature, Color color, float minMass,
			float maxMass, double fraction, int numberOfPlanets, int variationOfPlanets, double minDistanceOfPlanets,
			double maxDistanceOfPlanets, double chanceOfHabitable) {
		this.description = description;
		this.minTemperature = minTemperature;
		this.maxTemperature = maxTemperature;
		this.color = color;
		this.minMass = minMass;
		this.maxMass = maxMass;
		this.fraction = fraction;
		this.numberOfPlanets = numberOfPlanets;
		this.variationOfPlanets = variationOfPlanets;
		this.minDistanceOfPlanets = minDistanceOfPlanets;
		this.maxDistanceOfPlanets = maxDistanceOfPlanets;
		this.chanceOfHabitable = chanceOfHabitable;
	}
	
	public StarType getSisterType() {
		if ((this == W) || (this == H) || (this == N) || (this == P) || (this == D) || (this == R)) {
			return null;
		}
		fillDistro();
		Random r = Settings.instance().getRandom();
		StarType result = W;
		while (this.compareTo(result) > 0) {
			int dieRoll = (int) (r.nextDouble() * 1070d);
			result = distro[dieRoll];
		}
		return result;
	}
	
	private static void fillDistro() {
		if (distro == null) {
			distro = new StarType[1070];
			StarType[] allTypes = StarType.values();
			int index = 0;
			for (int i=7; i<allTypes.length; i++) {
				for (int j=0; j< (int) ((double) allTypes[i].getFraction() * 1000d); j++) {
					distro[index] = allTypes[i];
					index++;
				}
			}
		}
	}

	public String getDescription() {
		return description;
	}

	public int getMinTemperature() {
		return minTemperature;
	}

	public int getMaxTemperature() {
		return maxTemperature;
	}

	public Color getColor() {
		return color;
	}

	public float getMinMass() {
		return minMass;
	}
	
	public float getMaxMass() {
		return maxMass;
	}

	public double getFraction() {
		return fraction;
	}
	
	public int getNumberOfPlanets() {
		return numberOfPlanets;
	}

	public int getVariationOfPlanets() {
		return variationOfPlanets;
	}

	public double getMinDistanceOfPlanets() {
		return minDistanceOfPlanets;
	}

	public double getMaxDistanceOfPlanets() {
		return maxDistanceOfPlanets;
	}

	public double getChanceOfHabitable() {
		return chanceOfHabitable;
	}

	public static StarType parse(String description) {
		if (description.contains("W")) {
			return StarType.W;
		}
		if (description.contains("O")) {
			return StarType.O;
		}
		if (description.contains("B")) {
			return StarType.B;
		}
		if (description.contains("A")) {
			return StarType.A;
		}
		if (description.contains("F")) {
			return StarType.F;
		}
		if (description.contains("G")) {
			return StarType.G;
		}
		if (description.contains("K")) {
			return StarType.K;
		}
		if (description.contains("M")) {
			return StarType.M;
		}
		if (description.contains("D")) {
			return StarType.D;
		}
		if (description.contains("H")) {
			return StarType.H;
		}
		if (description.contains("C")) {
			return StarType.C;
		}
		if (description.contains("S")) {
			return StarType.S;
		}
		return StarType.M;
	}
	
	public static List<String> getDatabaseScript() {
		List<String> result = new LinkedList<>();
		result.add("CREATE TABLE star_type(type VARCHAR(255) PRIMARY KEY, description VARCHAR(255) NOT NULL,"
						+ " min_temperature INTEGER NOT NULL, max_temperature INTEGER NOT NULL,"
						+ " color_red INTEGER NOT NULL, color_green INTEGER NOT NULL, color_blue INTEGER NOT NULL,"
						+ " color_alpha INTEGER NOT NULL, min_mass DECIMAL(10,5) NOT NULL, max_mass DECIMAL(65,5) NOT NULL"
						+ " );");
		StarType[] types = StarType.values();
		for (StarType type: types) {
			result.add("INSERT INTO star_type(type, description, min_temperature, max_temperature, color_red, color_green, color_blue, color_alpha,min_mass, max_mass) "
					+ "VALUES (\"" + type.toString() + "\", \"" + 
								type.getDescription() + "\"," + 
								type.getMinTemperature() + "," + 
								type.getMaxTemperature() + "," + 
								type.getColor().getRed() + "," + 
								type.getColor().getGreen() + "," + 
								type.getColor().getBlue() + "," + 
								type.getColor().getAlpha() + "," + 
								type.getMinMass() + "," + 
								type.getMaxMass() + ""
					+ ");");
		}
		return result;		
	}
}
