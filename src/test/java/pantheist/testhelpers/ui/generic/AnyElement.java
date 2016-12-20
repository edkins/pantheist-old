package pantheist.testhelpers.ui.generic;

/**
 * Represents things you can do to any html DOM element.
 */
public interface AnyElement
{
	/**
	 * This won't do anything useful in an automated test. It just prints out
	 * some information about the element(s) matched by this css path.
	 *
	 * Useful for when you get stuck and want to see what's going on.
	 */
	void dump(String... attributes);
}
