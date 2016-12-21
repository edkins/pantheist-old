package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.ContainerElement;
import pantheist.testhelpers.ui.generic.ElementCollection;

final class SidebarImpl implements Sidebar
{
	private final ContainerElement el;

	private SidebarImpl(final ContainerElement el)
	{
		this.el = checkNotNull(el);
	}

	static Sidebar from(final ContainerElement el)
	{
		return new SidebarImpl(el);
	}

	@Override
	public void assertDisplayed()
	{
		el.assertVisible();
	}

	@Override
	public ClickableText resourceType(final String resourceType)
	{
		return el.a()
				.withData("level", "resourceType")
				.withData("resource-type", resourceType)
				.choose();
	}

	@Override
	public ClickableText resource(final String resourceType, final String resourceId)
	{
		return el.a()
				.withData("level", "resource")
				.withData("resource-type", resourceType)
				.withData("resource-id", resourceId)
				.choose();
	}

	@Override
	public ElementCollection allResourcesOfType(final String resourceType)
	{
		return el.a()
				.withData("level", "resource")
				.withData("resource-type", resourceType)
				.all();
	}
}
