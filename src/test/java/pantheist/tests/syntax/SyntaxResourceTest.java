package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.session.MainRule;
import pantheist.testhelpers.session.TestMode;

public class SyntaxResourceTest
{
	private static final String SYNTAX = "syntax";

	private static final String SYNTAX_ID = "cool-syntax";

	@Rule
	public MainRule sessionRule = MainRule.forNewTest(TestMode.API);

	private PantheistActions act;

	@Before
	public void setup()
	{
		act = sessionRule.actions();
	}

	@Test
	public void syntax_canBeCreated() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.assertResourceExists(SYNTAX, SYNTAX_ID);
	}

	@Test
	public void syntax_canBeDeleted() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.deleteResource(SYNTAX, SYNTAX_ID);
		act.assertResourceDoesNotExist(SYNTAX, SYNTAX_ID);
	}

	@Test
	public void syntax_alphabetical() throws Exception
	{
		act.createResource(SYNTAX, "zoo");
		act.createResource(SYNTAX, "animal");

		assertEquals(act.listResourceIdsOfType(SYNTAX), ImmutableList.of("animal", "zoo"));
	}

}
