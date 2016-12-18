package pantheist.api.syntax.model;

import java.util.List;

import javax.annotation.Nullable;

import com.google.inject.assistedinject.Assisted;

public interface SyntaxModelFactory
{
	SyntaxNode node(@Assisted("path") String path, @Assisted("id") String id, SyntaxNodeType type,
			List<String> children);

	SyntaxToken token(@Assisted("path") String path, @Assisted("id") String id, SyntaxTokenType type,
			@Nullable @Assisted("value") String value);

	TryOutTextReport tryOutTextReport(@Assisted("whatHappened") String whatHappened);
}
