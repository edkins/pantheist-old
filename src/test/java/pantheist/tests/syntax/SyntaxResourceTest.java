package pantheist.tests.syntax;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.model.Information;
import pantheist.testhelpers.selenium.ApiRule;
import pantheist.testhelpers.session.MainRule;

public abstract class SyntaxResourceTest
{
	private static final String BACKSLASH_T = "\\t";

	private static final String BACKSLASH_N = "\\n";

	private static final String BACKSLASH = "\\";

	private static final String QUOTE = "\"";

	private static final String ESCAPED_QUOTE = "\\\"";

	private static final String ESCAPED_BACKSLASH = "\\\\";

	private static final String LITERAL_TOKEN = "tok";

	private static final String SYNTAX = "syntax";

	private static final String SYNTAX_ID = "cool-syntax";

	@Rule
	public final MainRule sessionRule;

	protected PantheistActions act;

	public SyntaxResourceTest(final ApiRule apiRule)
	{
		sessionRule = MainRule.forNewTest(apiRule);
	}

	@Before
	public void setup()
	{
		act = sessionRule.actions();
	}

	private String res(final String resourcePath) throws IOException
	{
		try (InputStream inputStream = SyntaxResourceTest.class.getResourceAsStream(resourcePath))
		{
			return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		}
	}

	private void assertSyntaxTree(final Information result, final String treeText)
	{
		result.assertString("It returned an object " + treeText);
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
		act.createResource(SYNTAX, QUOTE);
		act.createResource(SYNTAX, BACKSLASH);
		act.createResource(SYNTAX, ESCAPED_BACKSLASH);
		act.createResource(SYNTAX, "<br>");
		act.createResource(SYNTAX, "#");
		assertEquals(
				ImmutableList.of(" ", QUOTE, "#", "%", "%20", "'",
						"+", ".", "..", "...", "/", "<br>", "?", BACKSLASH, ESCAPED_BACKSLASH, "a..", "http://"),
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

	@Test
	public void syntax_createDocRoot() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().docRootNode(SYNTAX_ID).assertEmpty();
		act.syntax().createLiteralToken(SYNTAX_ID, LITERAL_TOKEN);
		act.syntax().setDocRoot(SYNTAX_ID, LITERAL_TOKEN);
		act.syntax().docRootNode(SYNTAX_ID).assertString(LITERAL_TOKEN);
	}

	@Test
	public void syntax_changeDocDelim_and_changeDocRoot() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().createLiteralToken(SYNTAX_ID, "-");
		act.syntax().createLiteralToken(SYNTAX_ID, "_");
		act.syntax().createLiteralToken(SYNTAX_ID, "x");
		act.syntax().setDocDelim(SYNTAX_ID, "-");
		act.syntax().setDocDelim(SYNTAX_ID, "_");
		act.syntax().setDocRoot(SYNTAX_ID, "x");
		act.syntax().createOneOrMoreNodeSeparated(SYNTAX_ID, "xs", "x");
		act.syntax().setDocRoot(SYNTAX_ID, "xs");
		final Information result = act.syntax().tryOutSyntax(SYNTAX_ID, "x_x");
		assertSyntaxTree(result, "xs{x x}");
	}

	@Test
	public void grammar_fruitExample_canParse() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().createLiteralToken(SYNTAX_ID, "apple");
		act.syntax().createLiteralToken(SYNTAX_ID, "pear");
		act.syntax().createLiteralToken(SYNTAX_ID, "orange");
		act.syntax().createLiteralToken(SYNTAX_ID, "purple");
		act.syntax().createChoiceNode(SYNTAX_ID, "fruit", ImmutableList.of("apple", "pear", "orange"));
		act.syntax().createChoiceNode(SYNTAX_ID, "colour", ImmutableList.of("purple", "orange"));
		act.syntax().createSequenceNodeSeparated(SYNTAX_ID, "colourFruit", ImmutableList.of("colour", "fruit"));
		act.syntax().createOneOrMoreNodeSeparated(SYNTAX_ID, "colourFruits", "colourFruit");

		act.syntax().createSingleCharacterMatcher(SYNTAX_ID, "space", ImmutableList.of("space"), ImmutableList.of());
		act.syntax().setDocDelim(SYNTAX_ID, "space");
		act.syntax().setDocRoot(SYNTAX_ID, "colourFruits");
		final Information result = act.syntax().tryOutSyntax(SYNTAX_ID, "orange apple purple orange");
		assertSyntaxTree(result, "colourFruits{colourFruit{orange apple} colourFruit{purple orange}}");
	}

	@Test
	public void grammar_zeroOrMore_canParseEmptyDocument() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().createLiteralToken(SYNTAX_ID, "*");
		act.syntax().createZeroOrMoreNodeSeparated(SYNTAX_ID, "stars", "*");

		act.syntax().setDocRoot(SYNTAX_ID, "stars");
		final Information result = act.syntax().tryOutSyntax(SYNTAX_ID, "");
		assertSyntaxTree(result, "stars{}");
	}

	@Test
	public void grammar_zeroOrMore_canParseNonemptyDocument() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().createLiteralToken(SYNTAX_ID, "*");
		act.syntax().createZeroOrMoreNodeSeparated(SYNTAX_ID, "stars", "*");

		act.syntax().setDocRoot(SYNTAX_ID, "stars");
		final Information result = act.syntax().tryOutSyntax(SYNTAX_ID, "****");
		assertSyntaxTree(result, "stars{* * * *}");
	}

	@Test
	public void grammar_quotedString_canParse() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().createLiteralToken(SYNTAX_ID, QUOTE);
		act.syntax().createLiteralToken(SYNTAX_ID, ESCAPED_BACKSLASH);
		act.syntax().createLiteralToken(SYNTAX_ID, ESCAPED_QUOTE);
		act.syntax().createLiteralToken(SYNTAX_ID, BACKSLASH_N);
		act.syntax().createLiteralToken(SYNTAX_ID, BACKSLASH_T);
		act.syntax().createSingleCharacterMatcher(SYNTAX_ID, "space",
				ImmutableList.of("space"), ImmutableList.of());
		act.syntax().createSingleCharacterMatcher(SYNTAX_ID, "safe",
				ImmutableList.of("visible_ascii"),
				ImmutableList.of(QUOTE, BACKSLASH));
		act.syntax().createChoiceNode(SYNTAX_ID, "char",
				ImmutableList.of("safe", "space", ESCAPED_QUOTE, ESCAPED_BACKSLASH, BACKSLASH_N, BACKSLASH_T));
		act.syntax().createZeroOrMoreNodeGlued(SYNTAX_ID, "chars", "char");
		act.syntax().createSequenceNodeGlued(SYNTAX_ID, "string", ImmutableList.of(QUOTE, "chars", QUOTE));
		act.syntax().setDocRoot(SYNTAX_ID, "string");
		final Information result = act.syntax().tryOutSyntax(SYNTAX_ID, res("/quoted-string.txt"));

		final String expected = String.join(" ", res("/quoted-string-ast.txt").split("\n"));
		assertSyntaxTree(result, expected);
	}

	@Test
	public void grammar_operatorPrecedence() throws Exception
	{
		act.createResource(SYNTAX, SYNTAX_ID);
		act.syntax().createSingleCharacterMatcher(SYNTAX_ID, "num", ImmutableList.of("digit"), ImmutableList.of());
		act.syntax().createLiteralToken(SYNTAX_ID, "+");
		act.syntax().createLiteralToken(SYNTAX_ID, "*");
		act.syntax().createChoiceNode(SYNTAX_ID, "expr", ImmutableList.of("num"));
		act.syntax().createInfixlOperator(SYNTAX_ID, "+", 3, "expr");
		act.syntax().createInfixlOperator(SYNTAX_ID, "*", 4, "expr");
		act.syntax().setDocRoot(SYNTAX_ID, "expr");
		final Information result = act.syntax().tryOutSyntax(SYNTAX_ID, "1*2+3*4");
		assertSyntaxTree(result, "+{*{num{1} num{2}} *{num{3} num{4}}}");
	}
}
