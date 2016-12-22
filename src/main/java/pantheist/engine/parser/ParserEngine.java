package pantheist.engine.parser;

import org.codehaus.jparsec.Parser;

import pantheist.api.syntax.model.Syntax;

public interface ParserEngine
{
	/**
	 *
	 * @param syntax
	 * @return
	 * @throws BadGrammarException
	 *             if the syntax cannot be processed properly
	 */
	Parser<?> jparsecForSyntax(Syntax syntax);
}
