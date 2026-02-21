package util;

import domain.Body;
import domain.Coordinate;

public final class MathUtil {

    private MathUtil() {}


    public static <A extends Body, B extends Body> double distance(A a, B b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Bodies cannot be null");
        }
        return distance(a.getCoordinate(), b.getCoordinate());
    }


    public static double distance(Coordinate c1, Coordinate c2) {
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }

        double r1 = c1.getDistance();
        double lon1 = Math.toRadians(c1.getLongitude());
        double lat1 = Math.toRadians(c1.getLatitude());

        double x1 = r1 * Math.cos(lat1) * Math.cos(lon1);
        double y1 = r1 * Math.cos(lat1) * Math.sin(lon1);
        double z1 = r1 * Math.sin(lat1);

        double r2 = c2.getDistance();
        double lon2 = Math.toRadians(c2.getLongitude());
        double lat2 = Math.toRadians(c2.getLatitude());

        double x2 = r2 * Math.cos(lat2) * Math.cos(lon2);
        double y2 = r2 * Math.cos(lat2) * Math.sin(lon2);
        double z2 = r2 * Math.sin(lat2);

        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}