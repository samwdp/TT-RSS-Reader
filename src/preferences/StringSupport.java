package preferences;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Sam
 *
 */
public class StringSupport {

	
	public static <T> Set<String> convertListToString(Collection<T> values, int maxCount) {
		Set<String> ret = new HashSet<>();
		if (values == null || values.isEmpty()) return ret;

		StringBuilder sb = new StringBuilder();
		int count = 0;

		for (T t : values) {
			sb.append(t);

			if (count == maxCount) {
				ret.add(sb.substring(0, sb.length() - 1));
				sb = new StringBuilder();
				count = 0;
			} else {
				sb.append(",");
				count++;
			}
		}

		if (sb.length() > 0) ret.add(sb.substring(0, sb.length() - 1));

		return ret;
	}

	public static String[] setToArray(Set<String> set) {
		String[] ret = new String[set.size()];
		int i = 0;
		for (String s : set) {
			ret[i++] = s;
		}
		return ret;
	}

}