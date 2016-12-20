package pantheist.testhelpers.ui.pan;

import pantheist.testhelpers.ui.generic.ClickableText;

/**
 * Represents the main panel when it is pointing to a particular resource.
 */
public interface ResourcePanel
{
	void assertDisplayed();

	ClickableText deleteButton();
}
