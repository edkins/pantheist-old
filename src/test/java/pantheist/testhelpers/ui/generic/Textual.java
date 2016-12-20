package pantheist.testhelpers.ui.generic;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.IncorrectTextException;
import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * Represents an element that has some text.
 */
public interface Textual
{
	/**
	 * Returns the text.
	 *
	 * It's undefined what exactly will happen if this element contains markup
	 * or other sub-elements instead of just plain text.
	 *
	 * @return The text contained in this element, which may be empty.
	 * @throws CannotFindElementException
	 *             if the element does not exist or is invisible
	 * @throws MultipleElementException
	 *             if multiple elements match this css path
	 */
	String text();

	/**
	 * Assert that the text is what it should be. This must be a non-empty
	 * value.
	 *
	 * This will retry if something disruptive happened recently.
	 *
	 * @param text
	 *            expected text
	 * @throws IncorrectTextException
	 *             if the text is wrong.
	 */
	void assertText(String text);
}
