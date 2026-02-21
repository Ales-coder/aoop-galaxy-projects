package domain;

import domain.types.PlanetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Planet extends Body {

    private PlanetType type;
    private Star star;
    private final float averageDistanceToStar;
    private final float tilt;
    private final float excentricity;

    public Planet(String designation, String name, PlanetType type, Star star,
                  float averageDistanceToStar, float tilt, float excentricity) {
        super(designation, name);
        this.type = type;
        this.star = star;
        this.averageDistanceToStar = averageDistanceToStar;
        this.tilt = tilt;
        this.excentricity = excentricity;
    }


    public static Planet random(Star star, int index, float distance) {
        long seed = deriveSeed(star, index, distance);
        Random r = new Random(seed);

        String designation = star.getDesignation() + " " + index;

        float tilt = r.nextFloat() * 10f;
        float excentricity = (float) ((distance / star.getType().getMaxDistanceOfPlanets() * 45f) * r.nextDouble());

        PlanetType type;

        if (distance > star.getGoldilockZoneStart() && distance < star.getGoldilockZoneEnd()) {
            if (star.getType().getChanceOfHabitable() > r.nextDouble()) {
                if (star.getType().getChanceOfHabitable() > r.nextDouble()) type = PlanetType.M;
                else if (star.getType().getChanceOfHabitable() > r.nextDouble()) type = PlanetType.H;
                else if (star.getType().getChanceOfHabitable() > r.nextDouble()) type = PlanetType.K;
                else if (star.getType().getChanceOfHabitable() > r.nextDouble()) type = PlanetType.L;
                else type = PlanetType.N;
            } else {
                type = PlanetType.D;
            }
        }
        else if (distance > star.getGoldilockZoneEnd() && distance < star.getGoldilockZoneEnd() * 2) {
            type = (star.getType().getChanceOfHabitable() > r.nextDouble()) ? PlanetType.K : PlanetType.D;
        }
        else if (distance < star.getGoldilockZoneStart()) {
            type = PlanetType.Y;
        }
        else if (distance > star.getType().getMaxDistanceOfPlanets()) {
            type = PlanetType.N;
        }
        else {
            type = (r.nextDouble() > 0.5d) ? PlanetType.J : PlanetType.T;
        }

        return new Planet(designation, null, type, star, distance, tilt, excentricity);
    }


    private static long deriveSeed(Star star, int index, float distance) {
        int h = 17;
        h = 31 * h + star.getDesignation().hashCode();
        h = 31 * h + Float.floatToIntBits(star.getCoordinate().getDistance());
        h = 31 * h + Float.floatToIntBits(star.getCoordinate().getLongitude());
        h = 31 * h + Float.floatToIntBits(star.getCoordinate().getLatitude());
        h = 31 * h + index;
        h = 31 * h + Float.floatToIntBits(distance);
        return h;
    }


    public static List<Planet> generatePlanets(Star star) {

        int base = star.getType().getNumberOfPlanets();
        int var  = star.getType().getVariationOfPlanets();

        
        int h = 7;
        h = 31 * h + star.getDesignation().hashCode();
        h = 31 * h + Float.floatToIntBits(star.getCoordinate().getDistance());
        Random r = new Random(h);

        int n = base + (var > 0 ? r.nextInt(var + 1) : 0);
        if (n <= 0) return new ArrayList<>();

        double min = star.getType().getMinDistanceOfPlanets();
        double max = star.getType().getMaxDistanceOfPlanets();


        double start = max / Math.pow(2.0, Math.max(0, n - 1));
        if (start < min) start = min;

        List<Planet> planets = new ArrayList<>(n);
        double d = start;

        for (int i = 0; i < n; i++) {
            float dist = (float) Math.min(max, d);
            planets.add(Planet.random(star, i + 1, dist));
            d *= 2.0;
        }

        return planets;
    }

    public PlanetType getType() { return type; }
    public Star getStarRef() { return star; }

    public float getAverageDistanceToStar() { return averageDistanceToStar; }
    public float getTilt() { return tilt; }
    public float getExcentricity() { return excentricity; }

    @Override
    public Coordinate getCoordinate() {
        return star.getCoordinate();
    }
}