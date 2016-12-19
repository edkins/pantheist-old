package pantheist.api.generic.model;

import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

/**
 * A map which will throw exceptions if you try to store the wrong type of
 * object.
 */
public interface TypedMap
{
	/**
	 * @return immutable list of keys sorted into alphabetical order
	 */
	List<String> sortedKeyList();

	/**
	 * @return immutable list of key/value pairs sorted into alphabetical key
	 *         order
	 */
	List<Entry<String, Object>> sortedEntryList();

	/**
	 * Put the given value somewhere.
	 *
	 * @param key
	 * @param value
	 * @throws NullPointerException
	 *             if value is null
	 * @throws IllegalArgumentException
	 *             if the value is the wrong type
	 */
	void put(String key, Object value);

	/**
	 * Retrieve an object with a given key. Returns null if not present.
	 *
	 * @param key
	 * @return
	 */
	@Nullable
	Object get(String key);

	/**
	 * Remove an entry and return what was there (or null if there was nothing
	 * there).
	 *
	 * @param key
	 * @return
	 */
	@Nullable
	Object remove(String key);

	/**
	 * Return whether the key exists.
	 *
	 * @param key
	 * @return
	 */
	boolean containsKey(String key);
}
