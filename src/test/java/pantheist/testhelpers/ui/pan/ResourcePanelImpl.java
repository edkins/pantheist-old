package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.ContainerElement;

final class ResourcePanelImpl implements ResourcePanel
{
	private final ContainerElement el;

	private ResourcePanelImpl(final ContainerElement el)
	{
		this.el = checkNotNull(el);
	}

	static ResourcePanel from(final ContainerElement el)
	{
		return new ResourcePanelImpl(el);
	}

	@Override
	public void assertDisplayed()
	{
		deleteButton().assertText("Delete");
	}

	@Override
	public ClickableText deleteButton()
	{
		return el.inputButton().withValue("Delete").choose();
	}

}
