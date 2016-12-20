package pantheist.testhelpers.ui.generic;

import pantheist.testhelpers.ui.except.ElementStillPresentException;
import pantheist.testhelpers.ui.except.MultipleElementException;

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

	/**
	 * Asserts that the element does not exist at all, even in a hidden state.
	 * This will retry if something disruptive happened recently.
	 *
	 * @throws MultipleElementException
	 *             if multiple elements match this css path
	 * @throws ElementStillPresentException
	 *             if the element is still there.
	 */
	void assertGone();
}
