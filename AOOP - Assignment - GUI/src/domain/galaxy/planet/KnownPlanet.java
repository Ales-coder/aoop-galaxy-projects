package domain.galaxy.planet;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import domain.Culture;
import domain.Species;
import domain.galaxy.Star;
import util.IllegalOperationException;

public class KnownPlanet extends Planet implements Serializable {
	private static final long serialVersionUID = 1915169057063985758L;
	private double population;
	private String description;
	private TechnicalLevel technicalLevel;
	private Culture culture; 
	private Map<String, Integer> cities; 
	private Map<Species, Float> speciesPresent; 
	private Map<Climate, Float> climates; 
	
	public KnownPlanet(String designation, String name, PlanetType type, Star star, int planetIndex,
					   float averageDistanceToStar, float tilt, float excentricity,  double population,
					   String description, Culture culture, TechnicalLevel technicalLevel ) {
		this(designation, name, description, type, star, planetIndex, averageDistanceToStar, tilt,excentricity, population, technicalLevel);
		this.culture = culture;
	}
	
	public KnownPlanet(String designation, String name, PlanetType type, Star star, int planetIndex,
			   float averageDistanceToStar, float tilt, float excentricity, double population,
			   String description,  TechnicalLevel technicalLevel ) {
		this(designation, name, type, star, planetIndex, averageDistanceToStar, tilt, excentricity, population, description,  null, technicalLevel);
	}	

	public KnownPlanet(String designation, String name, String description, PlanetType type, Star star, int planetIndex, float averageDistanceToStar, float tilt,float excentricity, double population,  TechnicalLevel technicalLevel) {
		super(designation, name, type, star, planetIndex, averageDistanceToStar, tilt, excentricity);
		this.population = population;
		this.description = description;
		this.technicalLevel = technicalLevel;
		this.speciesPresent = new HashMap<>();
		this.cities = new HashMap<>();
		this.climates = new EnumMap<Climate, Float>(Climate.class);
	}
	
	public KnownPlanet(String designation, String name, PlanetType type, Star star, int planetIndex, float averageDistanceToStar, float tilt,float excentricity, double population, TechnicalLevel technicalLevel) {
		this(designation, name, null, type, star, planetIndex, averageDistanceToStar, tilt, excentricity,  population, technicalLevel);
		if ((this.getStar().getName() != null) && (this.getStar().getName().length() > 0)) {
			this.description = "Planet #" + this.getIndex() + " of star " + this.getStar().getName();
		}
		else {
			this.description = "Planet #" + this.getIndex() + " of star " + this.getStar().getDesignation();
		}
	}


	public void generate() throws IllegalOperationException {
		double population = -1d;
		TechnicalLevel technicalLevel = TechnicalLevel.NONE;
		this.population = population;
		this.technicalLevel = technicalLevel;

		Map<Climate, Float> climates = this.getType().generateClimateMap(this);
		Set<Climate> keySet = climates.keySet();
		for (Climate c: keySet) {
			this.addClimate(c, climates.get(c));
		}
	}

	public void addSpecies(Species species, float fraction) throws IllegalOperationException {
		if (speciesPresent == null) {
			speciesPresent = new HashMap<Species, Float>();
		}
		else if (speciesPresent.containsKey(species)) {
			throw new IllegalOperationException("Species already present in map.");
		}
		speciesPresent.put(species, fraction);
	}

	public void addClimate(Climate climate, float fraction) throws IllegalOperationException {
		if (fraction > 1.0f) {
			throw new IllegalOperationException("Fraction too high!");
		}
		if (fraction <= 0.0f) {
			throw new IllegalOperationException("Fraction cannot be negative!");
		}
		if (climates == null) {
			climates = new EnumMap<Climate, Float>(Climate.class);
			climates.put(Climate.OCEAN, 100f);
		}
		else if (climates.containsKey(climate)) {
			throw new IllegalOperationException("Climate already present in map.");
		}
		float total = 0;
		Set<Climate> keys = climates.keySet();
		for (Climate key: keys) {
			if (key != Climate.OCEAN) {
				total += climates.get(key);
			}
		}
		if ((total + fraction) > 1.0f) {
			throw new IllegalOperationException("Total fraction of climates would become more than 100%.");
		}
		float ocean = 1.0f - total;
		climates.remove(Climate.OCEAN);
		climates.put(climate,  fraction);
		climates.put(Climate.OCEAN, ocean);
	}
	
	public void deleteClimate(Climate climate) throws IllegalOperationException {
		if ((climates == null) ||  (!climates.containsKey(climate))) {
			throw new IllegalOperationException("Climate not present in map.");
		}
		if (climate == Climate.OCEAN) {
			throw new IllegalOperationException("You cannot remove the ocean.");
		}
		float total = 0;
		Set<Climate> keys = climates.keySet();
		for (Climate key: keys) {
			if (key != Climate.OCEAN) {
				total += climates.get(key);
			}
		}
		float ocean = 1.0f - total;
		climates.remove(climate);
		climates.put(Climate.OCEAN, ocean);
	}
	
	public void changeSpecies(Species species, float fraction) throws IllegalOperationException {
		if ((speciesPresent == null) || (!speciesPresent.containsKey(species))) {
			throw new IllegalOperationException("Species not present in map.");
		}
		else {
			speciesPresent.put(species, fraction);
		}
	}

	public void deleteSpecies(Species species) throws IllegalOperationException {
		if ((speciesPresent == null) ||  (!speciesPresent.containsKey(species))) {
			throw new IllegalOperationException("Species not present in map.");
		}
		else {
			speciesPresent.remove(species);
		}
	}

	public float getFraction(Species species) {
		if (speciesPresent == null) {
			return 0f;
		}
		else {
			Float result = speciesPresent.get(species);
			if (result == null) {
				return 0f;
			}
			else {
				return result;
			}
		}
	}
	
	public Map<Climate, Float> getClimates() {
		Map<Climate, Float> result = new HashMap<>();
		Set<Climate> keys = climates.keySet();
		for (Climate key: keys) {
			result.put(key,  climates.get(key));
		}
		return result;
	}
	
	public Map<String, Integer> getCities() {
		Map<String, Integer> result = new HashMap<>();
		Set<String> keys = cities.keySet();
		for (String key: keys) {
			result.put(key,  cities.get(key));
		}
		return result;
	}
	
	public int getCity(String city) {
		return cities.get(city);
	}
	
	public int addCity(String city, Integer population) {
		return cities.put(city, population);
	}
	
	public int removeCity(String city) {
		return cities.remove(city);
	}

	public double getPopulation() {
		return population;
	}

	public void setPopulation(double population) {
		this.population = population;
	}
	
	public TechnicalLevel getTechnicalLevel() {
		return technicalLevel;
	}

	public void setTechnicalLevel(TechnicalLevel technicalLevel) {
		this.technicalLevel = technicalLevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Culture getCulture() {
		return culture;
	}

	public void setCulture(Culture culture) {
		this.culture = culture;
	}
}
