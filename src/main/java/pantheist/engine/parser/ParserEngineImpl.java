package pantheist.engine.parser;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;

import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxNode;

final class ParserEngineImpl implements ParserEngine
{
	@Inject
	ParserEngineImpl()
	{
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

		Optional<Parser<Token>> tokenParser(final String name, final SyntaxNode st)
		{
			switch (st.type()) {
			case literal:
				return Optional.of(Scanners.string(st.value()).retn(TokenLiteral.of(name)));
			case regex:
				return Optional.of(Scanners
						.pattern(new RegexPattern(st.value()), name)
						.source()
						.map(value -> TokenSrc.of(name, value)));
			default:
				return Optional.empty();
			}
		}

		Parser<?> asJparsec()
		{
			final List<Parser<Token>> listOfTokenParsers = syntax.nodes()
					.entrySet()
					.stream()
					.map(e -> tokenParser(e.getKey(), e.getValue()))
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList());

			final Parser<Token> tokenizer = Parsers.or(listOfTokenParsers);
			return tokenizer;
		}
	}

	private static class RegexPattern extends org.codehaus.jparsec.pattern.Pattern
	{
		private final Pattern pattern;

		RegexPattern(final String regex)
		{
			this.pattern = Pattern.compile("^" + regex);
		}

		@Override
		public int match(final CharSequence src, final int begin, final int end)
		{
			final Matcher matcher = pattern.matcher(src.subSequence(begin, end));
			if (matcher.matches())
			{
				return matcher.end();
			}
			else
			{
				return MISMATCH;
			}
		}

	}

	private static interface Token
	{
	}

	private static class TokenLiteral implements Token
	{
		final String value;

		private TokenLiteral(final String value)
		{
			this.value = checkNotNullOrEmpty(value);
		}

		static Token of(final String value)
		{
			return new TokenLiteral(value);
		}

		@Override
		public String toString()
		{
			return "lit[:" + value + ":]";
		}
	}

	private static class TokenSrc implements Token
	{
		final String name;
		final String value;

		private TokenSrc(final String name, final String value)
		{
			this.name = checkNotNullOrEmpty(name);
			this.value = checkNotNullOrEmpty(value);
		}

		static Token of(final String name, final String value)
		{
			return new TokenSrc(name, value);
		}

		@Override
		public String toString()
		{
			return name + "[:" + value + ":]";
		}
	}
}
