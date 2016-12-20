package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pantheist.testhelpers.session.MainRule;
import pantheist.testhelpers.ui.pan.ResourceTypePanel;
import pantheist.testhelpers.ui.pan.Sidebar;

public class SyntaxResourceTest
{
	@Rule
	public MainRule sessionRule = MainRule.forNewTest();

	private Sidebar sb;
	private ResourceTypePanel rt;

	@Before
	public void setup()
	{
		sb = sessionRule.ui().sidebar();
		rt = sessionRule.ui().resourceTypePanel();
	}

	@Test
	public void resourceTypePrettyNames() throws Exception
	{
		assertEquals(sb.resourceType("syntax").text(), "Syntax");
	}

	@Test
	public void createSyntaxResource_showsInSidebar() throws Exception
	{
		sb.resourceType("syntax").click();
		rt.assertDisplayed();
		rt.nameToCreate().fillOut("cool-syntax");
		rt.createButton().click();
		sb.resource("syntax", "cool-syntax").assertText("cool-syntax");
	}
}
