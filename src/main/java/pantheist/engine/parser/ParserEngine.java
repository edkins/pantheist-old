package pantheist.engine.parser;

import org.codehaus.jparsec.Parser;

import pantheist.api.syntax.model.Syntax;

public interface ParserEngine
{
	Parser<?> jparsecForSyntax(Syntax syntax);
}
