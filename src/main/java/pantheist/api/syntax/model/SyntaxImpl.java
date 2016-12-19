package pantheist.api.syntax.model;

import java.util.SortedMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.api.generic.model.TypedMap;
import pantheist.api.generic.model.TypedMapBuilder;
import pantheist.api.generic.model.TypedMapImpl;
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
	public TypedMap components(final String componentType) throws NotFoundException
	{
		switch (componentType) {
		case "node":
			return TypedMapImpl.of(node, SyntaxNode.class);
		case "doc":
			return TypedMapBuilder.wrap(doc)
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
