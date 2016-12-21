package pantheist.testhelpers.ui.generic;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.DisabledElementException;
import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * Represents a menu such as an html select element.
 *
 * We don't expose each option as a separate element here but instead allow you
 * to click on a particular one.
 */
public interface Menu extends VisualElement
{
	/**
	 * Select an item from the menu identified by text.
	 *
	 * This counts as a disruptive event.
	 *
	 * @param text
	 *            the text to select
	 * @throws CannotFindElementException
	 * @throws MultipleElementException
	 *             if there are multiple menus. I'm not sure what will happen if
	 *             there are multiple items with the same text.
	 * @throws DisabledElementException
	 */
	void selectByText(String text);
}
