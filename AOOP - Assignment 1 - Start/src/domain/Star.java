package domain;

import domain.types.LuminosityClass;
import domain.types.StarType;

public class Star extends Body {
	private final StarType type;
	private final int temperatureSequence;
	private final LuminosityClass luminosity;
	private final float absoluteMagnitude;
	private final Coordinate coordinate;
	private double goldilockZoneStart; 
	private double goldilockZoneEnd; 
	private Star sister = null;
	
	public Star(String designation, String name, StarType type, int temperatureSequence, LuminosityClass luminosity,
			float absoluteMagnitude, float distance, float longitude, float latitude) {
		this(designation, name, type, temperatureSequence, luminosity, absoluteMagnitude, new Coordinate(distance, longitude, latitude));
	}
	
	public Star(String designation, String name, StarType type, int temperatureSequence, LuminosityClass luminosity,
			float absoluteMagnitude, Coordinate coordinate) {
		super(designation, name);
		this.type = type;
		this.temperatureSequence = temperatureSequence;
		this.luminosity = luminosity;
		this.absoluteMagnitude = absoluteMagnitude;
		this.coordinate = coordinate;
		calculateGoldilockZone();
	}

	public StarType getType() {
		return type;
	}

	public int getTemperatureSequence() {
		return temperatureSequence;
	}

	public LuminosityClass getLuminosity() {
		return luminosity;
	}

	public float getAbsoluteMagnitude() {
		return absoluteMagnitude;
	}

	public double getGoldilockZoneStart() {
		return goldilockZoneStart;
	}

	public double getGoldilockZoneEnd() {
		return goldilockZoneEnd;
	}
	
	public  Coordinate getCoordinate() {
		return this.coordinate;
	}
	
	public Star getSister() {
		return sister;
	}

	public void setSister(Star sister) {
		this.sister = sister;
	}

	private void calculateGoldilockZone() {
		double bc = absoluteMagnitude + getBC();
		double l = Math.pow(10, ((bc - 4.72)) / -2.5);
		goldilockZoneStart = Math.sqrt(l/1.1) / 100d;
		goldilockZoneEnd = Math.sqrt(l/0.53) / 100d;
	}
	
	private double getBC() {
		if (type == StarType.W) return -256;
		if (type == StarType.O) return -16;
		if (type == StarType.B) return -4;
		if (type == StarType.B) return -2;
		if (type == StarType.A) return -0.3;
		if (type == StarType.G) return -0.15;
		if (type == StarType.K) return -0.4;
		if (type == StarType.M) return -2;
		if (type == StarType.D) return -4;
		if (type == StarType.C) return -16;
		if (type == StarType.S) return -256;
		return 0;
	}

	@Override
	public String toString() {
		return "Star [type=" + type + ", temperatureSequence=" + temperatureSequence + ", luminosity=" + luminosity
				+ ", absoluteMagnitude=" + absoluteMagnitude + ", coordinate=" + coordinate + ", goldilockZoneStart="
				+ goldilockZoneStart + ", goldilockZoneEnd=" + goldilockZoneEnd + "]";
	}
}
