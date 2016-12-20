package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.ContainerElement;
import pantheist.testhelpers.ui.generic.TextEntry;

final class ResourceTypePanelImpl implements ResourceTypePanel
{
	private final ContainerElement el;

	private ResourceTypePanelImpl(final ContainerElement el)
	{
		this.el = checkNotNull(el);
	}

	static ResourceTypePanel from(final ContainerElement el)
	{
		return new ResourceTypePanelImpl(el);
	}

	@Override
	public void assertDisplayed()
	{
		nameToCreate().assertVisible();
		createButton().assertText("Create");
	}

	@Override
	public TextEntry nameToCreate()
	{
		return el.inputText().withId("name").choose();
	}

	@Override
	public ClickableText createButton()
	{
		return el.inputButton().choose();
	}

}
