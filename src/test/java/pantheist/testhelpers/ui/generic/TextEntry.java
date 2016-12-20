package pantheist.testhelpers.ui.generic;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.DisabledElementException;
import pantheist.testhelpers.ui.except.IncorrectTextException;
import pantheist.testhelpers.ui.except.MultipleElementException;

/**
 * An element that supports entering text.
 *
 * This could be an input element with type="text" or a textarea.
 */
public interface TextEntry extends VisualElement
{
	/**
	 * Fills out the element with the given text, under the condition that it
	 * was empty to start with.
	 *
	 * Since the text will be sent as a sequence of keystrokes, it's undefined
	 * what happens if there are any unusual characters in there. New lines
	 * should be ok but I'm not sure about tabs.
	 *
	 * @param text
	 *            non-null string (can be empty though)
	 * @throws IncorrectTextException
	 *             if the text was not empty
	 * @throws CannotFindElementException
	 * @throws MultipleElementException
	 * @throws DisabledElementException
	 * @throws IllegalArgumentException
	 *             if text contains newline characters and this is a single-line
	 *             text input
	 */
	void fillOut(String text);
}
