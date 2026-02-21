package domain.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import domain.Culture;

public class CultureCollection implements Set<Culture> {
	private Set<Culture> cultures;
	private static CultureCollection internalObject = null;

	private CultureCollection() {
		super();
		this.cultures = new HashSet<>();
		readCultureList();
	}
	
	public static CultureCollection instance() {
		if (internalObject == null) {
			internalObject = new CultureCollection();
		}
		return internalObject;
	}

	@Override
	public int size() {
		return cultures.size();
	}

	@Override
	public boolean isEmpty() {
		return cultures.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return cultures.contains(o);
	}

	@Override
	public Iterator<Culture> iterator() {
		return cultures.iterator();
	}

	@Override
	public Object[] toArray() {
		return cultures.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return cultures.toArray(a);
	}

	@Override
	public boolean add(Culture e) {
		return cultures.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return cultures.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return cultures.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Culture> c) {
		return cultures.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return cultures.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return cultures.removeAll(c);
	}

	@Override
	public void clear() {
		cultures.clear();
		
	}
	
	public Culture get(String name) {
		Iterator<Culture> i = cultures.iterator();
		while (i.hasNext()) {
			Culture result = i.next();
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}
	
	public Culture get(Culture culture) {
		Iterator<Culture> i = cultures.iterator();
		while (i.hasNext()) {
			Culture result = i.next();
			if (result.equals(culture)) {
				return result;
			}
		}
		return null;
	}
	
	private void readCultureList() {
		
	}
}
