package model;

/**
 * Holds all the feed infromtation
 * 
 * @author Sam
 *
 */
public class Feed implements Comparable<Feed> {

	public int id;
	public int categoryId;
	public String title;
	public String url;
	public int unread;

	@Override
	public int compareTo(Feed fi) {
		return title.compareToIgnoreCase(fi.title);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Feed) {
			Feed other = (Feed) o;
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
