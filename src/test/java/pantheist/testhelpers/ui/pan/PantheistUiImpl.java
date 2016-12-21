package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import org.openqa.selenium.WebDriver;

import com.fasterxml.jackson.databind.ObjectMapper;

import pantheist.testhelpers.ui.generic.ContainerElement;
import pantheist.testhelpers.ui.generic.CssPath;

public class PantheistUiImpl implements PantheistUi
{
	private final ContainerElement el;

	private PantheistUiImpl(final ContainerElement el)
	{
		this.el = checkNotNull(el);
	}

	public static PantheistUi root(final WebDriver webDriver, final ObjectMapper objectMapper)
	{
		return new PantheistUiImpl(CssPath.root(webDriver, objectMapper));
	}

	@Override
	public Sidebar sidebar()
	{
		return SidebarImpl.from(el.div().withClass("sidebar").choose());
	}

	@Override
	public ResourceTypePanel resourceTypePanel()
	{
		return ResourceTypePanelImpl.from(el.div().withId("resourceTypePanel").choose());
	}

	@Override
	public ResourcePanel resourcePanel()
	{
		return ResourcePanelImpl.from(el.div().withId("resourcePanel").choose());
	}

	@Override
	public TopPanel topPanel()
	{
		return TopPanelImpl.from(el.div().withClass("header").choose());
	}
}
