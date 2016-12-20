package pantheist.testhelpers.ui.pan;

import pantheist.testhelpers.ui.generic.ClickableText;

/**
 * Represents the sidebar which lists resources.
 */
public interface Sidebar
{
	ClickableText resourceType(String resourceType);

	ClickableText resource(String resourceType, String resourceId);
}
