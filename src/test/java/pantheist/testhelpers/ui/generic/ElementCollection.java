package pantheist.testhelpers.ui.generic;

import java.util.List;

/**
 * Methods that make sense for a collection of elements.
 */
public interface ElementCollection
{
	/**
	 * @return the number of elements in this collection
	 */
	int count();

	/**
	 * @return the element text for each element in this collection
	 */
	List<String> texts();

	/**
	 * @param key
	 *            data attribute
	 * @return the given data attribute for each element in this collection
	 */
	List<String> dataAttrs(String key);
}
