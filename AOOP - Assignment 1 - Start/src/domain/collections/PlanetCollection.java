package domain.collections;

import domain.Body;
import domain.Planet;
import domain.Star;

import java.util.*;

public class PlanetCollection implements Map<Star, List<Body>>, Iterable<List<Body>> {


    private final Map<Star, Star> stars = new LinkedHashMap<>();


    public boolean addStar(Star star) {
        if (star == null) return false;
        stars.put(star, star);
        return true;
    }


    public boolean removeStar(Star star) {
        return stars.remove(star) != null;
    }


    @Override
    public List<Body> get(Object key) {
        if (!(key instanceof Star star)) return List.of();
        if (!stars.containsKey(star)) return List.of();

        List<Body> system = new ArrayList<>();
        system.add(star);
        system.addAll(Planet.generatePlanets(star));
        return system;
    }


    @Override
    public List<Body> put(Star key, List<Body> value) {
        if (key == null) return List.of();
        stars.put(key, key);
        return get(key);
    }

    @Override public int size() { return stars.size(); }
    @Override public boolean isEmpty() { return stars.isEmpty(); }
    @Override public boolean containsKey(Object key) { return stars.containsKey(key); }

    @Override
    public boolean containsValue(Object value) {

        return false;
    }

    @Override
    public List<Body> remove(Object key) {
        if (!(key instanceof Star star)) return List.of();
        stars.remove(star);
        return List.of();
    }

    @Override
    public void putAll(Map<? extends Star, ? extends List<Body>> m) {
        for (Star s : m.keySet()) {
            if (s != null) stars.put(s, s);
        }
    }

    @Override public void clear() { stars.clear(); }

    @Override
    public Set<Star> keySet() {
        return Collections.unmodifiableSet(stars.keySet());
    }

    @Override
    public Collection<List<Body>> values() {
        List<List<Body>> all = new ArrayList<>();
        for (Star s : stars.keySet()) all.add(get(s));
        return all;
    }

    @Override
    public Set<Entry<Star, List<Body>>> entrySet() {
        Set<Entry<Star, List<Body>>> set = new LinkedHashSet<>();
        for (Star s : stars.keySet()) {
            set.add(new AbstractMap.SimpleEntry<>(s, get(s)));
        }
        return set;
    }


    @Override
    public Iterator<List<Body>> iterator() {
        Iterator<Star> it = stars.keySet().iterator();
        return new Iterator<>() {
            @Override public boolean hasNext() { return it.hasNext(); }
            @Override public List<Body> next() { return get(it.next()); }
        };
    }
}