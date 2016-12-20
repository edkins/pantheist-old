package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import pantheist.testhelpers.session.MainRule;
import pantheist.testhelpers.session.TestMode;

/**
 * Tests that only make sense for the UI.
 */
public class SyntaxUiTest
{
	@Rule
	public MainRule sessionRule = MainRule.forNewTest(TestMode.UI_INVISIBLE);

	@Test
	public void resourceTypePrettyNames() throws Exception
	{
		assertEquals(sessionRule.ui().sidebar().resourceType("syntax").text(), "Syntax");
	}

}
