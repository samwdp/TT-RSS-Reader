package model;

/**
 * Holds all the category information
 * 
 * @author Sam
 *
 */
public class Category implements Comparable<Category> {

	public int id;
	public String title;
	public int unread;

	public Category() {
	}

	public Category(int id, String title, int unread) {
		this.id = id;
		this.title = title;
		this.unread = unread;
	}

	@Override
	public int compareTo(Category ci) {
		// Sort by Id if Id is 0 or smaller, else sort by Title
		if (id <= 0 || ci.id <= 0) {
			Integer thisInt = id;
			Integer thatInt = ci.id;
			return thisInt.compareTo(thatInt);
		}
		return title.compareToIgnoreCase(ci.title);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Category) {
			Category other = (Category) o;
			return (id == other.id);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return id + "".hashCode();
	}

}
