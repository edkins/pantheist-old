package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.model.Information;
import pantheist.testhelpers.selenium.ApiRule;
import pantheist.testhelpers.selenium.Interaction;
import pantheist.testhelpers.session.MainRule;

public class SyntaxResourceTest
{
	private static final String LITERAL_TOKEN = "tok";

	private static final String SYNTAX = "syntax";

	private static final String SYNTAX_ID = "cool-syntax";

	@ClassRule
	public static final ApiRule apiRule = Interaction.hidden();

	@Rule
	public final MainRule sessionRule = MainRule.forNewTest(apiRule);

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

		assertEquals(ImmutableList.of("animal", "zoo"), act.listResourceIdsOfType(SYNTAX));
	}

	@Test
	public void weirdResourceNames_canBeCreated() throws Exception
	{
		act.createResource(SYNTAX, " ");
		act.createResource(SYNTAX, ".");
		act.createResource(SYNTAX, "..");
		act.createResource(SYNTAX, "a..");
		act.createResource(SYNTAX, "...");
		act.createResource(SYNTAX, "/");
		act.createResource(SYNTAX, "%");
		act.createResource(SYNTAX, "%20");
		act.createResource(SYNTAX, "+");
		act.createResource(SYNTAX, "http://");
		act.createResource(SYNTAX, "?");
		act.createResource(SYNTAX, "'");
		act.createResource(SYNTAX, "\"");
		act.createResource(SYNTAX, "\\");
		act.createResource(SYNTAX, "\\\\");
		act.createResource(SYNTAX, "<br>");
		act.createResource(SYNTAX, "#");
		assertEquals(
				ImmutableList.of(" ", "\"", "#", "%", "%20", "'",
						"+", ".", "..", "...", "/", "<br>", "?", "\\", "\\\\", "a..", "http://"),
				act.listResourceIdsOfType(SYNTAX));
	}

	@Test
	public void syntax_createLiteralToken() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().createLiteralToken(SYNTAX_ID, LITERAL_TOKEN);
		final Information node = act.syntax().describeNode(SYNTAX_ID, LITERAL_TOKEN);
		node.field("type").assertString("literal");
		node.field("value").assertString(LITERAL_TOKEN);
		node.field("children").assertEmpty();
	}

	@Test
	public void syntax_deleteToken() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().createLiteralToken(SYNTAX_ID, LITERAL_TOKEN);
		act.syntax().deleteNode(SYNTAX_ID, LITERAL_TOKEN);
		act.syntax().assertNodeIsGone(SYNTAX_ID, LITERAL_TOKEN);
	}
}
