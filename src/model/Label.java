package model;

/**
 * Holds all the label information
 * 
 * @author Sam
 *
 */
public class Label implements Comparable<Label> {

	public Integer id;
	public String caption;
	public boolean checked;
	public boolean checkedChanged = false;
	public String foregroundColor;
	public String backgroundColor;

	@Override
	public int compareTo(Label l) {
		return id.compareTo(l.id);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Label) {
			Label other = (Label) o;
			return (id.equals(other.id));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return id + "".hashCode();
	}

	@Override
	public String toString() {
		return caption + ";" + foregroundColor + ";" + backgroundColor;
	}

}
