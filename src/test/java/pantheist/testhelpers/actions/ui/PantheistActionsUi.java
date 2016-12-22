package pantheist.testhelpers.actions.ui;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;

import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.actions.interf.SyntaxActions;
import pantheist.testhelpers.model.Information;
import pantheist.testhelpers.model.InformationBuilder;
import pantheist.testhelpers.model.Informations;
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

	@Override
	public void deleteNode(final String syntaxId, final String nodeId)
	{
		rp.syntaxNodes().deleteRow(nodeId);
	}

	@Override
	public void assertNodeIsGone(final String syntaxId, final String nodeId)
	{
		wantResource("syntax", syntaxId);
		rp.syntaxNodes().assertNoRow(nodeId);
	}

	private void checkNoSpaces(final String id)
	{
		checkNotNullOrEmpty(id);
		if (id.contains(" "))
		{
			throw new IllegalArgumentException("Id cannot contain spaces in the UI: " + id);
		}
	}

	private String spaceSeparated(final List<String> ids)
	{
		checkNotNull(ids);
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final String id : ids)
		{
			checkNoSpaces(id);
			if (!first)
			{
				sb.append(' ');
			}
			first = false;
			sb.append(id);
		}
		return sb.toString();
	}

	@Override
	public void setDocRoot(final String syntaxId, final String nodeId)
	{
		wantResource("syntax", syntaxId);
		rp.syntaxNodes().cell(nodeId, "root").inputRadio().choose().click();
	}

	@Override
	public void setDocDelim(final String syntaxId, final String nodeId)
	{
		wantResource("syntax", syntaxId);
		rp.syntaxNodes().cell(nodeId, "delim").inputRadio().choose().click();
	}

	@Override
	public Information docRootNode(final String syntaxId)
	{
		wantResource("syntax", syntaxId);
		return Informations.optionalString(rp.syntaxNodes().findCheckedRow("root"));
	}

	@Override
	public Information docDelimNode(final String syntaxId)
	{
		wantResource("syntax", syntaxId);
		return Informations.optionalString(rp.syntaxNodes().findCheckedRow("delim"));
	}

	@Override
	public void createSingleCharacterMatcher(final String syntaxId, final String nodeId,
			final List<String> options, final List<String> exceptions)
	{
		wantResource("syntax", syntaxId);
		rp.syntaxCreateType().selectByText("Single character matcher");
		rp.syntaxCreateName().fillOut(nodeId);
		rp.syntaxCreateDetail().fillOut(spaceSeparated(options));
		rp.syntaxCreateFurtherDetail().fillOut(spaceSeparated(exceptions));
		rp.syntaxCreateButton().click();
	}

	@Override
	public void createZeroOrMoreNodeSeparated(final String syntaxId, final String nodeId, final String child)
	{
		wantResource("syntax", syntaxId);
		checkNoSpaces(child);
		rp.syntaxCreateType().selectByText("Zero or more tokens (separated)");
		rp.syntaxCreateName().fillOut(nodeId);
		rp.syntaxCreateDetail().fillOut(child);
		rp.syntaxCreateButton().click();
	}

	@Override
	public void createOneOrMoreNodeSeparated(final String syntaxId, final String nodeId, final String child)
	{
		wantResource("syntax", syntaxId);
		checkNoSpaces(child);
		rp.syntaxCreateType().selectByText("One or more tokens (separated)");
		rp.syntaxCreateName().fillOut(nodeId);
		rp.syntaxCreateDetail().fillOut(child);
		rp.syntaxCreateButton().click();
	}

	@Override
	public void createSequenceNodeSeparated(final String syntaxId, final String nodeId, final List<String> children)
	{
		wantResource("syntax", syntaxId);
		rp.syntaxCreateType().selectByText("Sequence of tokens (separated)");
		rp.syntaxCreateName().fillOut(nodeId);
		rp.syntaxCreateDetail().fillOut(spaceSeparated(children));
		rp.syntaxCreateButton().click();
	}

	@Override
	public void createChoiceNode(final String syntaxId, final String nodeId, final List<String> children)
	{
		wantResource("syntax", syntaxId);
		rp.syntaxCreateType().selectByText("Choice between tokens");
		rp.syntaxCreateName().fillOut(nodeId);
		rp.syntaxCreateDetail().fillOut(spaceSeparated(children));
		rp.syntaxCreateButton().click();
	}

	@Override
	public Information tryOutSyntax(final String syntaxId, final String document)
	{
		wantResource("syntax", syntaxId);
		rp.allowTimeToStabilize();
		rp.trySyntaxText().fillOut(document);
		rp.allowTimeToStabilize();
		rp.trySyntaxButton().click();
		rp.allowTimeToStabilize();
		return rp.trySyntaxResult().interpretDirectly();
	}

	@Override
	public void createZeroOrMoreNodeGlued(final String syntaxId, final String nodeId, final String child)
	{
		wantResource("syntax", syntaxId);
		checkNoSpaces(child);
		rp.syntaxCreateType().selectByText("Zero or more tokens (glued)");
		rp.syntaxCreateName().fillOut(nodeId);
		rp.syntaxCreateDetail().fillOut(child);
		rp.syntaxCreateButton().click();
	}

	@Override
	public void createOneOrMoreNodeGlued(final String syntaxId, final String nodeId, final String child)
	{
		wantResource("syntax", syntaxId);
		checkNoSpaces(child);
		rp.syntaxCreateType().selectByText("One or more tokens (glued)");
		rp.syntaxCreateName().fillOut(nodeId);
		rp.syntaxCreateDetail().fillOut(child);
		rp.syntaxCreateButton().click();
	}

	@Override
	public void createSequenceNodeGlued(final String syntaxId, final String nodeId, final List<String> children)
	{
		wantResource("syntax", syntaxId);
		rp.syntaxCreateType().selectByText("Sequence of tokens (glued)");
		rp.syntaxCreateName().fillOut(nodeId);
		rp.syntaxCreateDetail().fillOut(spaceSeparated(children));
		rp.syntaxCreateButton().click();
	}

	@Override
	public void createInfixlOperator(final String syntaxId,
			final String operator,
			final int level,
			final String containedIn)
	{
		wantResource("syntax", syntaxId);
		checkNoSpaces(containedIn);
		rp.syntaxCreateType().selectByText("Left-associative infix operator");
		rp.syntaxCreateName().fillOut(operator);
		rp.syntaxCreateDetail().fillOut(String.valueOf(level));
		rp.syntaxCreateFurtherDetail().fillOut(containedIn);
		rp.syntaxCreateButton().click();
	}

}
