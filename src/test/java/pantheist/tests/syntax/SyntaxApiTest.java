package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.selenium.ApiRule;
import pantheist.testhelpers.selenium.Interaction;
import pantheist.testhelpers.session.MainRule;

public class SyntaxApiTest
{
	private static final String SYNTAX = "syntax";

	@ClassRule
	public static final ApiRule apiRule = Interaction.api();

	@Rule
	public MainRule sessionRule = MainRule.forNewTest(apiRule);

	private PantheistActions act;

	@Before
	public void setup()
	{
		act = sessionRule.actions();
	}

	/**
	 * UI doesn't support entering newline characters and I don't really care.
	 */
	@Test
	public void weirdResourceNames_withNewLines_canBeCreated() throws Exception
	{
		act.createResource(SYNTAX, "a\nb");
		act.createResource(SYNTAX, "\n");
		assertEquals(
				ImmutableList.of("\n", "a\nb"),
				act.listResourceIdsOfType(SYNTAX));
	}
}
