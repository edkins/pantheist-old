package pantheist.api.syntax.model;

import java.util.List;

import com.google.inject.assistedinject.Assisted;

public interface SyntaxModelFactory
{
	ListSyntaxResponse listSyntaxResponse(List<SyntaxMetadata> resources);

	SyntaxMetadata syntaxMetadata(@Assisted("path") String path, @Assisted("name") String name);

	ListNodeResponse listNodeResponse(List<SyntaxNode> nodes);

	SyntaxNode node(@Assisted("path") String path, @Assisted("id") String id, SyntaxNodeType type,
			List<String> children);

	Syntax syntax(@Assisted("path") String path,
			@Assisted("id") String id,
			@Assisted("name") String name,
			List<SyntaxNode> nodes);
}
