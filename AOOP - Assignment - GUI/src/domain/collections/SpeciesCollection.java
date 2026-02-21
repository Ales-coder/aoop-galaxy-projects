package domain.collections;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import domain.Species;
import io.CsvSpeciesHandler;

public class SpeciesCollection implements Set<Species> {

    private Set<Species> species;
    private static SpeciesCollection internalObject = null;

    private SpeciesCollection() throws IOException {
        super();
        this.species = new HashSet<>();

        this.species.addAll(CsvSpeciesHandler.readSpeciesfromCsv());
    }

    public static SpeciesCollection instance() throws IOException {
        if (internalObject == null) {
            internalObject = new SpeciesCollection();
        }
        return internalObject;
    }

    @Override
    public int size() {
        return species.size();
    }

    @Override
    public boolean isEmpty() {
        return species.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return species.contains(o);
    }

    @Override
    public Iterator<Species> iterator() {
        return species.iterator();
    }

    @Override
    public Object[] toArray() {
        return species.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return species.toArray(a);
    }

    @Override
    public boolean add(Species e) {
        return species.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return species.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return species.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Species> c) {
        return species.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return species.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return species.removeAll(c);
    }

    @Override
    public void clear() {
        species.clear();
    }
}