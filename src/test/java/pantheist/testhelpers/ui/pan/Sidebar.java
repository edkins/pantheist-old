package pantheist.testhelpers.ui.pan;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.ElementCollection;

/**
 * Represents the sidebar which lists resources.
 */
public interface Sidebar
{
	void assertDisplayed();

	ClickableText resourceType(String resourceType);

	ClickableText resource(String resourceType, String resourceId);

	ElementCollection allResourcesOfType(String resourceType);
}
