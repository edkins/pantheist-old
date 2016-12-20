package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import pantheist.testhelpers.session.MainRule;
import pantheist.testhelpers.ui.pan.PantheistUi;

public class SyntaxResourceTest
{
	@Rule
	public MainRule sessionRule = MainRule.forNewTest();

	@Test
	public void resourceTypePrettyNames() throws Exception
	{
		final PantheistUi ui = sessionRule.ui();
		ui.sidebar().hack().a().choose().dump();
		assertEquals(ui.sidebar().resourceType("syntax").text(), "Syntax");
	}
}
