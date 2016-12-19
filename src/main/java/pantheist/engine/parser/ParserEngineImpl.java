package pantheist.engine.parser;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parser.Reference;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Token;
import org.codehaus.jparsec.TokenMap;

import com.google.common.collect.ImmutableList;

import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxDocProperty;
import pantheist.api.syntax.model.SyntaxNode;
import pantheist.common.except.OtherPreconditions;

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

	private static class TokenMapByNodeId implements TokenMap<Artifact>
	{
		private final String nodeId;

		private TokenMapByNodeId(final String nodeId)
		{
			this.nodeId = checkNotNullOrEmpty(nodeId);
		}

		static TokenMap<Artifact> of(final String nodeId)
		{
			return new TokenMapByNodeId(nodeId);
		}

		@Override
		public String toString()
		{
			return TokenArtifact.of(nodeId).toString();
		}

		@Override
		public Artifact map(final Token token)
		{
			checkNotNullOrEmpty((String) token.value());
			if (nodeId.equals(token.value()))
			{
				return TokenArtifact.of(nodeId);
			}
			else
			{
				return null;
			}
		}

	}

	private static enum ParseLevel
	{
		CHARACTER, TOKEN;
	}

	private class JparsecThing
	{
		private final Syntax syntax;

		// State
		private final Map<String, Parser<Artifact>> visitedTokens;
		private final List<Parser<String>> listOfTokenParsers;

		JparsecThing(final Syntax syntax)
		{
			this.syntax = checkNotNull(syntax);
			this.visitedTokens = new HashMap<>();
			this.listOfTokenParsers = new ArrayList<>();
		}

		String rootNodeId()
		{
			final SyntaxDocProperty root = checkNotNull(syntax.doc().root());
			return OtherPreconditions.singletonValue(root.children());
		}

		List<String> whitespaceNodeIds()
		{
			final SyntaxDocProperty ws = syntax.doc().whitespace();
			if (ws == null)
			{
				return ImmutableList.of();
			}
			return OtherPreconditions.nullOrEmptyList(ws.children());
		}

		private Parser<Artifact> recurse(final String nodeId, final ParseLevel level)
		{
			if (visitedTokens.containsKey(nodeId))
			{
				return visitedTokens.get(nodeId);
			}
			final Reference<Artifact> reference = Parser.newReference();
			visitedTokens.put(nodeId, reference.lazy());

			final SyntaxNode sn = lookup(nodeId);

			final Parser<String> tokenizer;
			Parser<Artifact> parser;

			switch (sn.type()) {
			case literal:
				tokenizer = Scanners.string(sn.value()).retn(nodeId);
				parser = null;
				break;
			case regex:
				tokenizer = Scanners.pattern(new RegexPattern(sn.value()), nodeId).retn(nodeId);
				parser = null;
				break;
			default:
				throw new UnsupportedOperationException("Unsupported syntax node type: " + sn.type());
			}

			switch (level) {
			case CHARACTER:
				if (parser == null)
				{
					checkNotNull(tokenizer);
					parser = tokenizer.map(TokenArtifact::of);
				}
				break;
			case TOKEN:
				if (parser == null)
				{
					checkNotNull(tokenizer);
					parser = Parsers.token(TokenMapByNodeId.of(nodeId));
					listOfTokenParsers.add(tokenizer);
				}
				break;
			}
			checkNotNull(parser);
			reference.set(parser);
			return parser;
		}

		private SyntaxNode lookup(final String nodeId)
		{
			final SyntaxNode result = syntax.node().get(nodeId);
			if (result == null)
			{
				throw new IllegalStateException("Node not found: " + nodeId);
			}
			return result;
		}

		Parser<?> asJparsec()
		{
			final Parser<Artifact> tokenLevelRoot = recurse(rootNodeId(), ParseLevel.TOKEN);
			final Parser<String> tokenizer = Parsers.or(listOfTokenParsers);

			visitedTokens.clear();
			listOfTokenParsers.clear();

			final List<Parser<Void>> listOfWhitespaceParsers = new ArrayList<>();
			for (final String whitespaceNodeId : whitespaceNodeIds())
			{
				final Parser<Artifact> p = recurse(whitespaceNodeId, ParseLevel.CHARACTER);
				listOfWhitespaceParsers.add(p.retn(null));
			}

			final Parser<Void> delim = Parsers.or(listOfWhitespaceParsers);
			return tokenLevelRoot.from(tokenizer, delim);
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

	private static interface Artifact
	{
	}

	private static class TokenArtifact implements Artifact
	{
		final String nodeId;

		private TokenArtifact(final String nodeId)
		{
			this.nodeId = checkNotNullOrEmpty(nodeId);
		}

		static Artifact of(final String nodeId)
		{
			return new TokenArtifact(nodeId);
		}

		@Override
		public String toString()
		{
			return "[:" + nodeId + ":]";
		}
	}

	private static class TokenLiteral implements Artifact
	{
		final String value;

		private TokenLiteral(final String value)
		{
			this.value = checkNotNullOrEmpty(value);
		}

		static Artifact of(final String value)
		{
			return new TokenLiteral(value);
		}

		@Override
		public String toString()
		{
			return "lit[:" + value + ":]";
		}
	}

	private static class TokenSrc implements Artifact
	{
		final String name;
		final String value;

		private TokenSrc(final String name, final String value)
		{
			this.name = checkNotNullOrEmpty(name);
			this.value = checkNotNullOrEmpty(value);
		}

		static Artifact of(final String name, final String value)
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
