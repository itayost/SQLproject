package test.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Set<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<T> data;

	public Set() {
		data = new ArrayList<T>();
	}

	public boolean add(T addMe) {
		if (!data.contains(addMe)) {
			data.add(addMe);
			return true;
		}
		return false;
	}

	public boolean add(T[] addArray) {
		int howManyAdded = 0;
		for (int i = 0; i < addArray.length; i++) {
			if (addArray[i] != null) {
				if (!data.contains(addArray[i])) {
					data.add(addArray[i]);
					howManyAdded++;
				}
			}
		}
		if (howManyAdded > 0) {
			return true;
		}
		return false;
	}

	public boolean set(int oldSet, T setMe) {
		if (data.size() > oldSet && oldSet >= 0) {
			if (!data.contains(setMe)) {
				data.set(oldSet, setMe);
				return true;
			}
		}
		return false;
	}

	public boolean delete(int oldSet) {
		if (data.size() > oldSet && oldSet >= 0) {
			data.remove(oldSet);
			return true;
		}
		return false;
	}

	public T getElement(int index) throws IndexOutOfBoundsException {
		if (!(data.size() > index && index >= 0)) {
			throw new IndexOutOfBoundsException();
		}
		return data.get(index);
	}

	public int getSize() {
		return data.size();
	}

	public int getIndex(T whereAmI) {
		return data.indexOf(whereAmI);
	}

	public String toString() {
		return "Set [data= \n" + data.toString() + "]";
	}

}
