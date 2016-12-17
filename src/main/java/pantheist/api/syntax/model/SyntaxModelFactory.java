package pantheist.api.syntax.model;

import java.util.List;

import com.google.inject.assistedinject.Assisted;

public interface SyntaxModelFactory
{
	ListSyntaxResponse listSyntaxResponse(List<SyntaxMetadata> resources);

	SyntaxMetadata syntaxMetadata(@Assisted("path") String path, @Assisted("name") String name);
}
