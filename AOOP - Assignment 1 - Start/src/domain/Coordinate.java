package domain;

import util.MathUtil;

public final class Coordinate {
    private final float distance;   
    private final float longitude; 
    private final float latitude;   

    public Coordinate(float distance, float longitude, float latitude) {
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public float getDistance() { return distance; }
    public float getLongitude() { return longitude; }
    public float getLatitude() { return latitude; }


    public double calculateDistance(Coordinate other) {
        return MathUtil.distance(this, other);
    }
}