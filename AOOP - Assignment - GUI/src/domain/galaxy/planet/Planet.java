package domain.galaxy.planet;

import java.math.BigInteger;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import domain.galaxy.Body;
import domain.galaxy.Coordinate;
import domain.galaxy.Star;
import domain.galaxy.StarType;

public class Planet extends Body implements Comparable<Planet> {
	private Star star;
	private int planetIndex;
	private PlanetType type;
	private float averageDistanceToStar; 
	private float tilt = 0; 
	private float excentricity; 
	
	public Planet(String designation, String name, PlanetType type, Star star, int planetIndex, float averageDistanceToStar, float tilt,float excentricity) {
		super(designation, name);
		this.planetIndex = planetIndex;
		this.type = type;
		this.star = star;
		this.averageDistanceToStar = averageDistanceToStar;
		this.tilt = tilt;
		this.excentricity = excentricity;
	}
	
	public Planet() {
		
	}
	
	@Override
	public int hashCode() {
		return (int) (this.longHashCode() % Integer.MAX_VALUE);
	}
	
	public long longHashCode() {
		long prime = 2031055211850435299l;
		long result = super.hashCode();
		result = prime * result + (long) Float.floatToIntBits(averageDistanceToStar);
		result = prime * result + (long) Float.floatToIntBits(excentricity);
		result = prime * result + ((star == null) ? 0 : star.longHashCode());
		result = prime * result + (long) Float.floatToIntBits(tilt);
		result = prime * result + ((type == null) ? 0 : (long) type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		Planet other = (Planet) obj;
		if (this.getDesignation().equals(other.getDesignation())) {
			return true;
		}
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (planetIndex != other.planetIndex)
			return false;
		if (star == null) {
			if (other.star != null)
				return false;
		} else if (!star.equals(other.star))
			return false;
		return true;
	}

	public PlanetType getType() {
		return type;
	}

	public void setType(PlanetType type) {
		this.type = type;
	}

	public Star getStar() {
		return star;
	}

	public void setStar(Star star) {
		this.star = star;
	}

	public float getAverageDistanceToStar() {
		return averageDistanceToStar;
	}

	public float getTilt() {
		return tilt;
	}

	public float getExcentricity() {
		return excentricity;
	}
	
	@Override
	public Coordinate getCoordinate() {
		return star.getCoordinate();
	}
	
	public int getIndex() {
		return planetIndex;
	}

	@Override
	public String toString() {
		return "Planet [designation=" + this.getDesignation() + ", type=" + type + ", starType=" + star.getType() + ", averageDistanceToStar="
				+ averageDistanceToStar + "]";
	}

	@Override
	public int compareTo(Planet o) {
		if (!this.star.equals(o.star)) {
			return this.star.compareTo(o.star);
		}
		else {
			return this.planetIndex - o.planetIndex;
		}
	}
	
	public static SortedSet<Planet> generate(Star star) {
		SortedSet<Planet> result = new TreeSet<>();
		if (star.getType() == StarType.W) {
			return result;
		}
		BigInteger seed = BigInteger.valueOf(star.longHashCode()).multiply(BigInteger.valueOf((long) star.getGoldilockZoneStart())).multiply(BigInteger.valueOf((long) star.getGoldilockZoneEnd()));
		seed = seed.mod(BigInteger.valueOf(Long.MAX_VALUE));
		Random r = new Random(seed.longValue());
		int numberOfPlanets = 0;
		if (star.getType().getNumberOfPlanets() > 0) {
			numberOfPlanets = star.getType().getNumberOfPlanets() + (r.nextInt(2 * star.getType().getVariationOfPlanets())) - star.getType().getVariationOfPlanets();
		}
		for (int i=1; i<= numberOfPlanets; i++) {
			Planet p = generate(star, i, numberOfPlanets);
			if (p != null) {
				result.add(p);
			}
		}
		return result;
	}
	
	private static Planet generate(Star star, int index, int numberOfPlanets) {
		if ((star.getType() == StarType.W) || (star.getType() == StarType.H) || (star.getType() == StarType.N) || (star.getType() == StarType.P)) {
			return null;
		}

		if (	(star.getDesignation().equals("SAGI-1000000000000000001") || 
				star.getDesignation().equals("SOLA-6592602058205295101") || 
				star.getDesignation().equals("COUN-1000000000000000001"))) {
			return null;
		}
		
		BigInteger seed = BigInteger.valueOf(star.longHashCode()).multiply(BigInteger.valueOf(index));
		seed = seed.mod(BigInteger.valueOf(Integer.MAX_VALUE));
		Random r = new Random(seed.longValue());
		String designation = star.getDesignation() + "-" + index;
		float tilt = 0f;
		for (int i=0; i<9; i++) {
			tilt += r.nextFloat() * 5f;
		}
		float distance = calculateAverageDistanceToStar(star, index, r);
		float excentricity = (float) ((distance / (star.getType().getMaxDistanceOfPlanets()) * 45f) * r.nextDouble());
		PlanetType type = determinePlanetType(star, index, r, numberOfPlanets);
		if ((type == PlanetType.M) && (tilt > 35f)) {
			type = PlanetType.H;
		}
		else if ((type == PlanetType.H) && (tilt > 35f)) {
			type = PlanetType.L;
		}
		Planet result = new Planet(designation, null, type, star, index, distance, tilt, excentricity);
		
		if (star.getDesignation().startsWith("SAGI-10000000000000000")) {
			if ((type == PlanetType.D) || (type == PlanetType.H) || (type == PlanetType.K) || (type == PlanetType.L) || (type == PlanetType.N) || (type == PlanetType.Y)) {
				result.setType(PlanetType.M);
			}
		}
		return result;
	}
	
	private static PlanetType determinePlanetType(Star star, int index, Random r, int numberOfPlanets) {
		float distance = calculateAverageDistanceToStar(star, index, r);
		if ((distance > star.getGoldilockZoneStart()) && (distance < star.getGoldilockZoneEnd())) {
			if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
				if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
					if (star.getSister() == null) {
						return PlanetType.L;					
					}
					else {
						return PlanetType.N;
					}
				}
				else if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
					if (star.getSister() != null) {
						if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
							return PlanetType.K;
						}
						else {
							return PlanetType.L;
						}
					}
					else {
						double dieRoll = r.nextDouble() * star.getType().getChanceOfHabitable();
						if (dieRoll > 0.7) {
							return PlanetType.M;
						}
						else if(dieRoll > 0.4) {
							return PlanetType.L;
						}
						else if(dieRoll > 0.2) {
							return PlanetType.K;
						}
						else {
							return PlanetType.H;
						}
					}
				}
				else if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
					if (star.getSister() == null) {
						return PlanetType.H;					
					}
					else {
						return PlanetType.M;
					}
				}
				else if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
					return PlanetType.M;
				}
				else {
					return PlanetType.N;
				}
			}
			else {
				if (star.getSister() == null) {
					return PlanetType.N;					
				}
				else {
					return PlanetType.D;
				}
			}
		}
		else if ((distance > star.getGoldilockZoneEnd()) && (distance < star.getGoldilockZoneEnd() * 2)) {
			if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
				return PlanetType.K;
			}
			else if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
				return PlanetType.H;
			}
			else {
				if (star.getSister() == null) {
					return PlanetType.N;					
				}
				else {
					return PlanetType.D;
				}
			}
		}
		else if (distance < star.getGoldilockZoneStart()) {
			return PlanetType.Y;
		}
		else {
			if ((index == numberOfPlanets) && (r.nextDouble() > 0.5d)) {
				return PlanetType.N;
			}
			else if (r.nextDouble() > 0.5d) {
				return PlanetType.J;
			}
			else {
				return PlanetType.T;
			}
		}
	}
	
	private static float calculateAverageDistanceToStar(Star s, int index, Random r) {
		int numberOfPlanets = s.getType().getNumberOfPlanets();
		int variation = (int) (((double) s.getType().getVariationOfPlanets()) * r.nextDouble());
		if (r.nextDouble() > 0.5) {
			numberOfPlanets += variation;
		}
		else {
			numberOfPlanets -= variation;
		}
		int total = 0;
		for (int i=1; i<= numberOfPlanets; i++) {
			total += i;
		}
		float step = (float) (s.getType().getMaxDistanceOfPlanets() / (float) total);
		float distance = step * index;
		return distance;
	}
}
