package app;

import domain.Body;
import domain.Planet;
import domain.Star;
import domain.collections.PlanetCollection;
import domain.collections.StarCollection;
import domain.types.PlanetType;

import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public static void main(String[] args) {

        String path = "data/teststars.csv"; 

        StarCollection starCollection = new StarCollection(path);
        PlanetCollection planetCollection = new PlanetCollection();

  
        for (Star s : starCollection) {
            planetCollection.addStar(s);
        }


        int removedFromPlanetCollection = 0;


        List<Star> planetKeys = new ArrayList<>(planetCollection.keySet());

        for (Star s : planetKeys) {
            List<Body> system = planetCollection.get(s);


            if (system.size() <= 1) {
                planetCollection.removeStar(s);
                removedFromPlanetCollection++;
                continue;
            }

            boolean hasNonRTY = false;

            for (int i = 1; i < system.size(); i++) {
                Planet p = (Planet) system.get(i);
                PlanetType t = p.getType();

                if (t != PlanetType.R && t != PlanetType.T && t != PlanetType.Y) {
                    hasNonRTY = true;
                    break;
                }
            }

            if (!hasNonRTY) {
                planetCollection.removeStar(s);
                removedFromPlanetCollection++;
            }
        }


        int removedFromStarCollection = 0;


        List<Star> starsSnapshot = new ArrayList<>();
        for (Star s : starCollection) starsSnapshot.add(s);

        for (Star s : starsSnapshot) {
            List<Planet> planets = Planet.generatePlanets(s);
            if (planets == null || planets.isEmpty()) {
                starCollection.remove(s);
                removedFromStarCollection++;
            }
        }


        System.out.println("=== Assignment 1c Statistics ===");
        System.out.println("Stars in StarCollection (after removal of no-planet stars): " + starCollection.size());
        System.out.println("Stars in PlanetCollection (after filtering): " + planetCollection.size());
        System.out.println("Removed from PlanetCollection (no planets or only R/T/Y): " + removedFromPlanetCollection);
        System.out.println("Removed from StarCollection (no planets): " + removedFromStarCollection);
    }
}