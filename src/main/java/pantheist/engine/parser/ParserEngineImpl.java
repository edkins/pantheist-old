package pantheist.engine.parser;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;

import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxToken;
import pantheist.api.syntax.model.SyntaxTokenType;

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

		private List<String> literalTokens()
		{
			return syntax.tokens()
					.entrySet()
					.stream()
					.filter(e -> e.getValue().type().equals(SyntaxTokenType.literal))
					.map(e -> e.getKey())
					.collect(Collectors.toList());
		}

		Parser<ParsedToken> tokenParser(final String name, final SyntaxToken st)
		{
			switch (st.type()) {
			case literal:
				return Scanners.string(name).retn(ParsedTokenLiteralImpl.of(name));
			case regex:
				return Scanners
						.pattern(new RegexPattern(st.value()), name)
						.source()
						.map(ParsedTokenSrcImpl::of);
			default:
				throw new IllegalArgumentException("Unrecognized token type: " + st.type());
			}
		}

		Parser<?> asJparsec()
		{
			final List<Parser<ParsedToken>> listOfTokenParsers = syntax.tokens()
					.entrySet()
					.stream()
					.map(e -> tokenParser(e.getKey(), e.getValue()))
					.collect(Collectors.toList());

			final Parser<ParsedToken> tokenizer = Parsers.or(listOfTokenParsers);
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

	private static interface ParsedToken
	{
	}

	private static class ParsedTokenLiteralImpl implements ParsedToken
	{
		final String value;

		private ParsedTokenLiteralImpl(final String value)
		{
			this.value = checkNotNullOrEmpty(value);
		}

		static ParsedToken of(final String value)
		{
			return new ParsedTokenLiteralImpl(value);
		}
	}

	private static class ParsedTokenSrcImpl implements ParsedToken
	{
		final String value;

		private ParsedTokenSrcImpl(final String value)
		{
			this.value = checkNotNullOrEmpty(value);
		}

		static ParsedToken of(final String value)
		{
			return new ParsedTokenSrcImpl(value);
		}
	}
}
