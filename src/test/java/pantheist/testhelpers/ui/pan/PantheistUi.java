package pantheist.testhelpers.ui.pan;

/**
 * Main portal into the Pantheist UI. This exposes a few interfaces representing
 * different parts of the UI which may or may not be visible at any one time.
 */
public interface PantheistUi
{
	Sidebar sidebar();

	ResourceTypePanel resourceTypePanel();

	ResourcePanel resourcePanel();
}
