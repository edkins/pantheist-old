package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pantheist.testhelpers.session.MainRule;
import pantheist.testhelpers.ui.pan.ResourcePanel;
import pantheist.testhelpers.ui.pan.ResourceTypePanel;
import pantheist.testhelpers.ui.pan.Sidebar;

public class SyntaxResourceTest
{
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
		assertEquals(sb.resourceType("syntax").text(), "Syntax");
	}

	@Test
	public void syntax_showsInSidebar() throws Exception
	{
		createSyntax();
		sb.resource("syntax", SYNTAX_ID).assertText(SYNTAX_ID);
	}

	@Test
	public void syntax_canBeDeleted() throws Exception
	{
		createSyntax();
		sb.resource("syntax", SYNTAX_ID).click();
		rp.assertDisplayed();
		rp.deleteButton().click();
		sb.resource("syntax", SYNTAX_ID).assertGone();
	}

	private void createSyntax()
	{
		sb.resourceType("syntax").click();
		rt.assertDisplayed();
		rt.nameToCreate().fillOut(SYNTAX_ID);
		rt.createButton().click();
	}
}
