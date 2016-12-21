package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import pantheist.testhelpers.selenium.ApiRule;
import pantheist.testhelpers.selenium.Interaction;

public class SyntaxApiTest extends SyntaxResourceTest
{
	@ClassRule
	public static final ApiRule apiRule = Interaction.api();

	public SyntaxApiTest()
	{
		super(apiRule);
	}

	private static final String SYNTAX = "syntax";

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
