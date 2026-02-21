package domain.galaxy;

import util.MathUtil;

public final class Coordinate {
	private double distance; 
	private double longitude;  
	private double latitude;  
	
	public Coordinate(double distance, double longitude, double latitude) {
		super();
		this.distance = distance;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Coordinate() {
		
	}
	public double getDistance() {
		return distance;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
    public double calculateDistance(Coordinate other) {
    	return MathUtil.calculateDistance(this.getDistance(), this.getLongitude(), this.getLatitude(), other.getDistance(), other.getLongitude(), other.getLatitude());
    }

	@Override
	public int hashCode() {
		return (int) (this.longHashCode() % Integer.MAX_VALUE);
	}

	public long  longHashCode() {
		final long prime = 3590935123997492563l;
		long result = 1;
		long temp;
		temp = Double.doubleToLongBits(distance);
		result = prime * result + (long) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (long) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (long) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (Double.doubleToLongBits(distance) != Double.doubleToLongBits(other.distance))
			return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Coordinate [distance=" + distance + ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}
}
