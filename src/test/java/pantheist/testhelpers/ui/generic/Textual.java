package pantheist.testhelpers.ui.generic;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.EmptyTextException;
import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * Represents an element that has some text.
 */
public interface Textual
{
	/**
	 * Returns the text or fails if the text is empty.
	 *
	 * It's undefined what exactly will happen if this element contains markup
	 * or other sub-elements instead of just plain text.
	 *
	 * @return The non-empty text contained in this element.
	 * @throws CannotFindElementException
	 *             if the element does not exist or is invisible
	 * @throws MultipleElementException
	 *             if multiple elements match this css path
	 * @throws EmptyTextException
	 *             if it's visible but contains no actual text
	 */
	String text();
}
