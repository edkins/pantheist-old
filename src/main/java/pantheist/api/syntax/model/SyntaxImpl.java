package pantheist.api.syntax.model;

import java.util.SortedMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.api.generic.model.IntolerantMap;
import pantheist.api.generic.model.IntolerantMapBuilder;
import pantheist.api.generic.model.IntolerantMapImpl;
import pantheist.common.except.NotFoundException;
import pantheist.common.except.OtherPreconditions;

final class SyntaxImpl implements Syntax
{
	private final SortedMap<String, SyntaxNode> node;
	private final MutableSyntaxDocProperties doc;

	SyntaxImpl(@JsonProperty("node") final SortedMap<String, SyntaxNode> node,
			@JsonProperty("doc") final MutableSyntaxDocProperties doc)
	{
		this.node = OtherPreconditions.nullable(node);
		this.doc = MutableSyntaxDocPropertiesImpl.ofNullable(doc);
	}

	@Override
	public IntolerantMap components(final String componentType) throws NotFoundException
	{
		switch (componentType) {
		case "node":
			return IntolerantMapImpl.of(node, SyntaxNode.class);
		case "doc":
			return IntolerantMapBuilder.wrap(doc)
					.with("root", SyntaxDocProperty.class, SyntaxDocProperties::root,
							MutableSyntaxDocProperties::setRoot)
					.with("whitespace", SyntaxDocProperty.class, SyntaxDocProperties::whitespace,
							MutableSyntaxDocProperties::setWhitespace)
					.build();
		default:
			throw new NotFoundException(componentType);
		}
	}

	@Override
	public SortedMap<String, SyntaxNode> node()
	{
		return node;
	}

	@Override
	public SyntaxDocProperties doc()
	{
		return doc;
	}

}
