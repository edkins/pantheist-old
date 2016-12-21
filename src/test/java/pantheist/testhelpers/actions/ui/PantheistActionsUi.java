package pantheist.testhelpers.actions.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.actions.interf.SyntaxActions;
import pantheist.testhelpers.model.Information;
import pantheist.testhelpers.model.InformationBuilder;
import pantheist.testhelpers.ui.pan.PantheistUi;
import pantheist.testhelpers.ui.pan.ResourcePanel;
import pantheist.testhelpers.ui.pan.ResourceTypePanel;
import pantheist.testhelpers.ui.pan.Sidebar;
import pantheist.testhelpers.ui.pan.TopPanel;

public class PantheistActionsUi implements PantheistActions, SyntaxActions
{
	private final TopPanel top;
	private final Sidebar sb;
	private final ResourceTypePanel rt;
	private final ResourcePanel rp;

	// State
	private NavigationLocation location;

	private PantheistActionsUi(final PantheistUi ui, final NavigationLocation location)
	{
		checkNotNull(ui);
		top = ui.topPanel();
		sb = ui.sidebar();
		rt = ui.resourceTypePanel();
		rp = ui.resourcePanel();
		this.location = checkNotNull(location);
	}

	public static PantheistActions from(final PantheistUi ui)
	{
		return new PantheistActionsUi(ui, NavigationLocation.root());
	}

	private void wantSidebar()
	{
		if (!location.canSeeSidebar())
		{
			top.home().click();
			location = NavigationLocation.root();
		}
		sb.assertDisplayed();
	}

	private void wantResource(final String resourceType, final String resourceId)
	{
		final NavigationLocation desired = NavigationLocation.resource(resourceType, resourceId);
		if (!location.equals(desired))
		{
			wantSidebar();
			sb.resource(resourceType, resourceId).click();
			location = desired;
		}
	}

	@Override
	public void createResource(final String resourceType, final String resourceId)
	{
		wantSidebar();
		sb.resourceType(resourceType).click();
		location = NavigationLocation.resourceType(resourceType);
		rt.assertDisplayed();
		rt.nameToCreate().fillOut(resourceId);
		rt.createButton().click();
		sb.resource(resourceType, resourceId).assertVisible();
	}

	@Override
	public void assertResourceExists(final String resourceType, final String resourceId)
	{
		wantSidebar();
		sb.resource(resourceType, resourceId).assertText(resourceId);
	}

	@Override
	public void deleteResource(final String resourceType, final String resourceId)
	{
		wantSidebar();
		sb.resource(resourceType, resourceId).click();
		rp.assertDisplayed();
		rp.deleteButton().click();
		sb.resource(resourceType, resourceId).assertGone();
		location = NavigationLocation.root();
	}

	@Override
	public void assertResourceDoesNotExist(final String resourceType, final String resourceId)
	{
		wantSidebar();
		sb.resource(resourceType, resourceId).assertGone();
	}

	@Override
	public List<String> listResourceIdsOfType(final String resourceType)
	{
		wantSidebar();
		return sb.allResourcesOfType(resourceType).dataAttrs("resource-id");
	}

	@Override
	public SyntaxActions syntax()
	{
		return this;
	}

	@Override
	public void createLiteralToken(final String syntaxId, final String value)
	{
		wantResource("syntax", syntaxId);
		rp.syntaxCreateType().selectByText("Literal token");
		rp.syntaxCreateName().fillOut(value);
		rp.syntaxCreateButton().click();
	}

	@Override
	public Information describeNode(final String syntaxId, final String nodeId)
	{
		wantResource("syntax", syntaxId);
		final Information id = rp.syntaxNodes().cell(nodeId, "id").interpretDirectly();
		final Information type = rp.syntaxNodes().cell(nodeId, "type").interpretDirectly();
		final Information value = rp.syntaxNodes().cell(nodeId, "value").interpretDirectly();
		final Information children = rp.syntaxNodes().cell(nodeId, "children").interpretAsJson();
		return InformationBuilder.create()
				.with("id", id)
				.with("type", type)
				.with("value", value)
				.with("children", children);
	}

}
