package pantheist.testhelpers.ui.pan;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.ContainerElement;

/**
 * Represents the sidebar which lists resources.
 */
public interface Sidebar
{
	ClickableText resourceType(String resourceType);

	ClickableText resource(String resourceType, String resourceId);

	ContainerElement hack();
}
