package pantheist.testhelpers.ui.pan;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.TextEntry;

/**
 * Represents the main panel when it is pointing to a resource type e.g. Syntax.
 */
public interface ResourceTypePanel
{
	/**
	 * Asserts that the main elements are displayed correctly.
	 */
	void assertDisplayed();

	/**
	 * @return text box specifying the name of a resource we want to create
	 */
	TextEntry nameToCreate();

	/**
	 * @return button that creates a new resource
	 */
	ClickableText createButton();
}
