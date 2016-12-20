package pantheist.testhelpers.ui.generic;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.DisabledElementException;
import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * Represents a web element that can be clicked to invoke some kind of action.
 *
 * Examples:
 *
 * - links
 *
 * - buttons
 */
public interface Clickable extends VisualElement
{
	/**
	 * Click this element.
	 *
	 * @throws CannotFindElementException
	 *             if the element does not exist or is invisible
	 * @throws MultipleElementException
	 *             if multiple elements match this css path
	 * @throws DisabledElementException
	 *             if it's a visible but disabled input button
	 */
	void click();
}
