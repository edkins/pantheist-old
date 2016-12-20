package pantheist.testhelpers.actions.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.ui.pan.PantheistUi;
import pantheist.testhelpers.ui.pan.ResourcePanel;
import pantheist.testhelpers.ui.pan.ResourceTypePanel;
import pantheist.testhelpers.ui.pan.Sidebar;

public class PantheistActionsUi implements PantheistActions
{
	private final Sidebar sb;
	private final ResourceTypePanel rt;
	private final ResourcePanel rp;

	private PantheistActionsUi(final PantheistUi ui)
	{
		checkNotNull(ui);
		sb = ui.sidebar();
		rt = ui.resourceTypePanel();
		rp = ui.resourcePanel();
	}

	public static PantheistActions from(final PantheistUi ui)
	{
		return new PantheistActionsUi(ui);
	}

	@Override
	public void createResource(final String resourceType, final String resourceId)
	{
		sb.resourceType(resourceType).click();
		rt.assertDisplayed();
		rt.nameToCreate().fillOut(resourceId);
		rt.createButton().click();
		sb.resource(resourceType, resourceId).assertVisible();
	}

	@Override
	public void assertResourceExists(final String resourceType, final String resourceId)
	{
		sb.resource(resourceType, resourceId).assertText(resourceId);
	}

	@Override
	public void deleteResource(final String resourceType, final String resourceId)
	{
		sb.resource(resourceType, resourceId).click();
		rp.assertDisplayed();
		rp.deleteButton().click();
		sb.resource(resourceType, resourceId).assertGone();
	}

	@Override
	public void assertResourceDoesNotExist(final String resourceType, final String resourceId)
	{
		sb.resource(resourceType, resourceId).assertGone();
	}

	@Override
	public List<String> listResourceIdsOfType(final String resourceType)
	{
		return sb.allResourcesOfType(resourceType).dataAttrs("resource-id");
	}

}
