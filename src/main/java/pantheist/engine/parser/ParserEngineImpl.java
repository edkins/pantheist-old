package pantheist.engine.parser;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parser.Reference;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Token;
import org.codehaus.jparsec.TokenMap;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import pantheist.api.syntax.model.SingleCharMatchers;
import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxDocProperty;
import pantheist.api.syntax.model.SyntaxNode;
import pantheist.common.except.OtherPreconditions;
import pantheist.common.util.Make;

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
			return nodeId;
		}

		@Override
		public Artifact map(final Token token)
		{
			final Artifact artifact = (Artifact) token.value();
			if (nodeId.equals(artifact.nodeId()))
			{
				return artifact;
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
		private final List<Parser<Artifact>> listOfTokenParsers;

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

		Optional<List<String>> whitespaceNodeIds()
		{
			final SyntaxDocProperty ws = syntax.doc().whitespace();
			if (ws == null)
			{
				return Optional.empty();
			}
			return Optional.of(ws.children());
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

			Parser<Artifact> tokenizer = null;
			Parser<Artifact> parser = null;

			switch (sn.type()) {
			case literal:
				tokenizer = Scanners.string(sn.value()).retn(TokenArtifact.of(nodeId));
				break;
			case single_character:
				tokenizer = singleCharacterMatcher(sn.children(), sn.exceptions())
						.source()
						.map(SingleCharacterArtifact.with(nodeId));
				break;
			case glued_zero_or_more:
				tokenizer = childParser(ParseLevel.CHARACTER, sn)
						.many()
						.map(children -> SequenceArtifact.of(nodeId, children));
				break;
			case glued_one_or_more:
				tokenizer = childParser(ParseLevel.CHARACTER, sn)
						.many1()
						.map(children -> SequenceArtifact.of(nodeId, children));
				break;
			case glued_sequence:
				tokenizer = seq(nodeId, childParsers(ParseLevel.CHARACTER, sn));
				break;
			case zero_or_more:
				checkParserLevel(level);
				parser = childParser(level, sn)
						.many()
						.map(children -> SequenceArtifact.of(nodeId, children));
				break;
			case one_or_more:
				checkParserLevel(level);
				parser = childParser(level, sn)
						.many1()
						.map(children -> SequenceArtifact.of(nodeId, children));
				break;
			case sequence:
				checkParserLevel(level);
				parser = seq(nodeId, childParsers(level, sn));
				break;
			case choice:
				parser = Parsers.or(childParsers(level, sn));
				break;
			default:
				throw new UnsupportedOperationException("Unsupported syntax node type: " + sn.type());
			}

			switch (level) {
			case CHARACTER:
				if (parser == null)
				{
					checkNotNull(tokenizer);
					parser = tokenizer;
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

		private void checkParserLevel(final ParseLevel level)
		{
			if (!level.equals(ParseLevel.TOKEN))
			{
				throw new IllegalStateException("Separated sequence cannot be contained inside glued sequence");
			}
		}

		private List<Parser<Artifact>> childParsers(final ParseLevel level, final SyntaxNode sn)
		{
			final List<Parser<Artifact>> childParsers = Lists.transform(sn.children(), id -> recurse(id, level));
			return childParsers;
		}

		private Parser<Artifact> childParser(final ParseLevel level, final SyntaxNode sn)
		{
			return OtherPreconditions.singletonValue(childParsers(level, sn));
		}

		private Parser<Void> singleCharacterMatcher(final List<String> options, final List<String> exceptions)
		{
			final List<CharMatcher> matchers = Lists.transform(options, SingleCharMatchers::fromString);
			final List<CharMatcher> exceptionMatchers = Lists.transform(exceptions, SingleCharMatchers::fromString);
			return Scanners.isChar(c -> matchers.stream().anyMatch(m -> m.matches(c))
					&& !exceptionMatchers.stream().anyMatch(m -> m.matches(c)));
		}

		private Parser<Artifact> seq(final String nodeId, final List<Parser<Artifact>> parsers)
		{
			Parser<List<Artifact>> result = Parsers.constant(ImmutableList.of());
			for (final Parser<Artifact> p : parsers)
			{
				result = Parsers.sequence(result, p, Make::list);
			}
			return result.map(children -> SequenceArtifact.of(nodeId, children));
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
			final Parser<Artifact> tokenizer = Parsers.or(listOfTokenParsers);

			visitedTokens.clear();
			listOfTokenParsers.clear();

			final Parser<Void> delim;
			if (whitespaceNodeIds().isPresent())
			{
				final List<Parser<Void>> listOfWhitespaceParsers = new ArrayList<>();
				for (final String whitespaceNodeId : whitespaceNodeIds().get())
				{
					final Parser<Artifact> p = recurse(whitespaceNodeId, ParseLevel.CHARACTER);
					listOfWhitespaceParsers.add(p.retn(null));
				}
				delim = Parsers.or(listOfWhitespaceParsers);
			}
			else
			{
				// If no whitespace is specified, delimiter is assumed to be the empty string.
				delim = Parsers.always();
			}

			return tokenLevelRoot.from(tokenizer, delim);
		}
	}

	private static interface Artifact
	{
		public String nodeId();
	}

	private static class SingleCharacterArtifact implements Artifact
	{
		final String nodeId;
		private final char ch;

		private SingleCharacterArtifact(final String nodeId, final char ch)
		{
			this.nodeId = checkNotNullOrEmpty(nodeId);
			this.ch = ch;
		}

		static org.codehaus.jparsec.functors.Map<String, Artifact> with(final String nodeId)
		{
			return str -> {
				if (str.length() != 1)
				{
					throw new IllegalStateException("Expecting single character to have been matched");
				}
				return new SingleCharacterArtifact(nodeId, str.charAt(0));
			};
		}

		@Override
		public String toString()
		{
			return nodeId + "{" + ch + "}";
		}

		@Override
		public String nodeId()
		{
			return nodeId;
		}
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
			return nodeId;
		}

		@Override
		public String nodeId()
		{
			return nodeId;
		}
	}

	private static class SequenceArtifact implements Artifact
	{
		final String nodeId;
		final List<Artifact> children;

		private SequenceArtifact(final String nodeId, final List<Artifact> children)
		{
			this.nodeId = checkNotNullOrEmpty(nodeId);
			this.children = OtherPreconditions.copyOfNotNull(children);
		}

		static Artifact of(final String nodeId, final List<Artifact> children)
		{
			return new SequenceArtifact(nodeId, children);
		}

		@Override
		public String toString()
		{
			final StringBuilder sb = new StringBuilder();
			sb.append(nodeId).append("{");
			boolean first = true;
			for (final Artifact child : children)
			{
				if (!first)
				{
					sb.append(" ");
				}
				first = false;
				sb.append(child);
			}
			return sb.append("}").toString();
		}

		@Override
		public String nodeId()
		{
			return nodeId;
		}
	}

}
