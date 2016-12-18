package pantheist.api.syntax.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.annotation.Nullable;

import com.google.inject.assistedinject.Assisted;

import pantheist.api.syntax.backend.ComponentType;

public interface SyntaxModelFactory
{
	ListSyntaxResponse listSyntaxResponse(List<SyntaxMetadata> resources);

	SyntaxMetadata syntaxMetadata(@Assisted("path") String path,
			@Assisted("id") String id,
			@Assisted("name") String name);

	ListComponentResponse listComponentResponse(Collection<ListedComponent> components);

	ListedComponent listedComponent(@Assisted("path") String path,
			@Assisted("id") String id,
			@Assisted("data") Object data);

	ListTokenResponse listTokenResponse(Collection<SyntaxToken> nodes);

	SyntaxNode node(@Assisted("path") String path, @Assisted("id") String id, SyntaxNodeType type,
			List<String> children);

	SyntaxToken token(@Assisted("path") String path, @Assisted("id") String id, SyntaxTokenType type,
			@Nullable @Assisted("value") String value);

	Syntax syntax(@Assisted("path") String path,
			@Assisted("id") String id,
			@Assisted("name") String name,
			Map<ComponentType, SortedMap<String, Object>> components);
}
