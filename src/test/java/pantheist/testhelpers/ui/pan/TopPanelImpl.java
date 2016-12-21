package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.ContainerElement;

public class TopPanelImpl implements TopPanel
{
	private final ContainerElement el;

	private TopPanelImpl(final ContainerElement el)
	{
		this.el = checkNotNull(el);
	}

	static TopPanel from(final ContainerElement el)
	{
		return new TopPanelImpl(el);
	}

	@Override
	public ClickableText home()
	{
		return el.a().withId("top-link-home").choose();
	}

	@Override
	public ClickableText doc()
	{
		return el.a().withId("top-link-doc").choose();
	}

}
