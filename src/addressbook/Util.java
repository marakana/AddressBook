package addressbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
	private Util() {

	}

	public static <T extends Comparable<T>> int compare(T o1, T o2) {
		return o1 == null ? (o2 == null ? 0 : -1) : o2 == null ? 1 : o1
				.compareTo(o2);
	}

	public static <T> T getLast(List<T> list) {
		return list == null || list.isEmpty() ? null : list
				.get(list.size() - 1);
	}

	public static <T> List<T> asList(T element) {
		List<T> result = new ArrayList<T>(1);
		result.add(element);
		return result;
	}

	public static <K, V> Map<K, V> asMap(K key, V value) {
		Map<K, V> result = new HashMap<K, V>(1);
		result.put(key, value);
		return result;
	}

	public static <T> T getByType(Class<? extends T> type, T... elements) {
		if (elements != null) {
			for (T element : elements) {
				if (type.isAssignableFrom(element.getClass())) {
					return element;
				}
			}
		}
		return null;
	}
}
