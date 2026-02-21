package domain.collections;

import domain.Star;
import domain.types.StarType;

import java.util.*;

public class StarCollection implements Iterable<Star> {

    private final Set<Star> stars = new HashSet<>();

    public StarCollection(String csvPath) {
        List<Star> loaded = StarCsvLoader.load(csvPath);
        for (Star s : loaded) {
            add(s);
        }
    }

    public StarCollection(String csvPath, int limit) {
        List<Star> loaded = StarCsvLoader.load(csvPath, limit);
        int i = 0;
        for (Star s : loaded) {
            add(s);
            i++;
            if (limit > 0 && i % 5000 == 0) {
                System.out.println("Inserted into StarCollection: " + i + "/" + limit
                        + " | current unique stars: " + stars.size());
            }
            if (limit > 0 && i >= limit) break;
        }
        if (limit > 0) {
            System.out.println("Finished inserting into StarCollection. Unique stars: " + stars.size());
        }
    }

    public boolean add(Star newStar) {
        if (newStar == null) return false;


        if (stars.contains(newStar)) return false;


        StarRules.ValidationResult vr = StarRules.validateCandidate(newStar, stars);
        if (!vr.ok) return false;

        boolean added = stars.add(newStar);
        if (!added) return false;


        if (vr.partner != null) {
            newStar.setSister(vr.partner);
            vr.partner.setSister(newStar);
        }

        return true;
    }

    public boolean remove(Star star) {
        if (star == null) return false;

        Star sister = star.getSister();
        if (sister != null) {
            sister.setSister(null);
            star.setSister(null);
        }

        return stars.remove(star);
    }

    public int size() {
        return stars.size();
    }

    public Set<Star> getStarList(StarType type) {
        Set<Star> result = new HashSet<>();
        for (Star s : stars) {
            if (s.getType() == type) result.add(s);
        }
        return result;
    }

    public Star find(String designation) {
        if (designation == null) return null;
        for (Star s : stars) {
            if (designation.equalsIgnoreCase(s.getDesignation())) return s;
        }
        return null;
    }

    @Override
    public Iterator<Star> iterator() {
        return stars.iterator();
    }
}