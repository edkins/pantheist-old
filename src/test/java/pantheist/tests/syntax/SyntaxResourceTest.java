package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import pantheist.testhelpers.session.MainRule;
import pantheist.testhelpers.ui.pan.ResourcePanel;
import pantheist.testhelpers.ui.pan.ResourceTypePanel;
import pantheist.testhelpers.ui.pan.Sidebar;

public class SyntaxResourceTest
{
	private static final String SYNTAX = "syntax";

	private static final String SYNTAX_ID = "cool-syntax";

	@Rule
	public MainRule sessionRule = MainRule.forNewTest();

	private Sidebar sb;
	private ResourceTypePanel rt;
	private ResourcePanel rp;

	@Before
	public void setup()
	{
		sb = sessionRule.ui().sidebar();
		rt = sessionRule.ui().resourceTypePanel();
		rp = sessionRule.ui().resourcePanel();
	}

	@Test
	public void resourceTypePrettyNames() throws Exception
	{
		assertEquals(sb.resourceType(SYNTAX).text(), "Syntax");
	}

	@Test
	public void syntax_showsInSidebar() throws Exception
	{
		createSyntax();
		sb.resource(SYNTAX, SYNTAX_ID).assertText(SYNTAX_ID);
	}

	@Test
	public void syntax_canBeDeleted() throws Exception
	{
		createSyntax();
		sb.resource(SYNTAX, SYNTAX_ID).click();
		rp.assertDisplayed();
		rp.deleteButton().click();
		sb.resource(SYNTAX, SYNTAX_ID).assertGone();
	}

	@Test
	public void syntax_alphabetical() throws Exception
	{
		createSyntax("zoo");
		createSyntax("animal");
		assertEquals(sb.allResourcesOfType(SYNTAX).dataAttrs("resource-id"), ImmutableList.of("animal", "zoo"));
	}

	private void createSyntax()
	{
		createSyntax(SYNTAX_ID);
	}

	private void createSyntax(final String syntaxId)
	{
		sb.resourceType(SYNTAX).click();
		rt.assertDisplayed();
		rt.nameToCreate().fillOut(syntaxId);
		rt.createButton().click();
		sb.resource(SYNTAX, syntaxId).assertVisible();
	}
}
