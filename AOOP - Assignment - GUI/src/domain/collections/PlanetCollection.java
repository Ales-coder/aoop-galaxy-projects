package domain.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import domain.galaxy.planet.Planet;
import domain.galaxy.planet.PlanetType;

public class PlanetCollection implements Set<Planet> {
	private Set<Planet> planets;
	private static PlanetCollection internalObject = null;

	private PlanetCollection() throws IOException {
		super();
		this.planets = new HashSet<>();
	}
	
	public static PlanetCollection instance() throws IOException {
		if (internalObject == null) {
			internalObject = new PlanetCollection();
		}
		return internalObject;
	}

	@Override
	public int size() {
		return planets.size();
	}

	@Override
	public boolean isEmpty() {
		return planets.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return planets.contains(o);
	}

	@Override
	public Iterator<Planet> iterator() {
		return planets.iterator();
	}

	@Override
	public Object[] toArray() {
		return planets.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return planets.toArray(a);
	}

	@Override
	public boolean add(Planet e) {
		return planets.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return planets.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return planets.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Planet> c) {
		return planets.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return planets.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return planets.removeAll(c);
	}

	@Override
	public void clear() {
		planets.clear();
		
	}
	
	public Planet find(String designation) {
		Planet planet = new Planet(designation, null, PlanetType.M, null, 1, 1f, 0f, 0.1f);
		Iterator<Planet> i = planets.iterator();
		while (i.hasNext()) {
			Planet result = i.next();
			if (result.equals(planet)) {
				return result;
			}
		}
		return null;
	}
	
	public Planet find(Planet planet) {
		Iterator<Planet> i = planets.iterator();
		while (i.hasNext()) {
			Planet result = i.next();
			if (result.equals(planet)) {
				return result;
			}
		}
		return null;
	}
}
