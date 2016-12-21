package pantheist.testhelpers.model;

/**
 * Some piece of information obtained from either the API or UI.
 *
 * I'm using this instead of strongly typed things because the UI doesn't really
 * give type information, and it may not be easy to distinguish between e.g. an
 * empty list and a totally missing value
 */
public interface Information
{
	/**
	 * Assuming this is structured information, return a particular field from
	 * it.
	 *
	 * @param name
	 * @return information for that field
	 * @throws IllegalStateException
	 *             if this is not a structure
	 */
	Information field(String name);

	/**
	 * Assert that the information is equal to the given (nonempty) string
	 * value.
	 *
	 * @param value
	 *            nonempty string value to check against
	 */
	void assertString(String value);

	/**
	 * Assert that this information is equal to the given list of string values.
	 *
	 * @param values
	 */
	void assertStringList(String... values);

	/**
	 * Assert that the information is empty or missing.
	 */
	void assertEmpty();
}
