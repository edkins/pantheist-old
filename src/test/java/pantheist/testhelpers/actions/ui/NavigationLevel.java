package pantheist.testhelpers.actions.ui;

public enum NavigationLevel
{
	ROOT, RESOURCE_TYPE, RESOURCE;

	public boolean canSeeSidebar()
	{
		switch (this) {
		case ROOT:
		case RESOURCE_TYPE:
		case RESOURCE:
			return true;
		default:
			return false;
		}
	}
}
