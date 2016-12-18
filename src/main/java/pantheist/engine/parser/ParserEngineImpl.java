package pantheist.engine.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Terminals;

import pantheist.api.generic.store.ResourceStore;
import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxTokenType;

final class ParserEngineImpl implements ParserEngine
{
	private final ResourceStore store;

	@Inject
	ParserEngineImpl(final ResourceStore store)
	{
		this.store = checkNotNull(store);
	}

	@Override
	public Parser<?> jparsecForSyntax(final Syntax syntax)
	{
		checkNotNull(syntax);
		return new JparsecThing(syntax).asJparsec();
	}

	private class JparsecThing
	{
		private final Syntax syntax;

		JparsecThing(final Syntax syntax)
		{
			this.syntax = checkNotNull(syntax);
		}

		private List<String> literalTokens()
		{
			return syntax.tokens()
					.entrySet()
					.stream()
					.filter(e -> e.getValue().type().equals(SyntaxTokenType.literal))
					.map(e -> e.getKey())
					.collect(Collectors.toList());
		}

		Parser<?> asJparsec()
		{
			final String[] literalTokens = new ArrayList<>(literalTokens()).toArray(new String[0]);
			final Terminals literalTerminals = Terminals.caseSensitive(new String[0], literalTokens);
			return literalTerminals.tokenizer();
		}
	}

}
