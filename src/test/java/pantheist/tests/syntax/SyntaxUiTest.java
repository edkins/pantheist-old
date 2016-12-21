package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import pantheist.testhelpers.selenium.ApiRule;
import pantheist.testhelpers.selenium.Interaction;
import pantheist.testhelpers.session.MainRule;

/**
 * Tests that only make sense for the UI.
 */
public class SyntaxUiTest extends SyntaxResourceTest
{
	@ClassRule
	public static final ApiRule apiRule = Interaction.hidden();

	@Rule
	public MainRule sessionRule = MainRule.forNewTest(apiRule);

	public SyntaxUiTest()
	{
		super(apiRule);
	}

	@Test
	public void resourceTypePrettyNames() throws Exception
	{
		assertEquals(sessionRule.ui().sidebar().resourceType("syntax").text(), "Syntax");
	}
}
