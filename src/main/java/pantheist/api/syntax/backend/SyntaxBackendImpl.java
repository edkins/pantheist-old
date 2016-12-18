package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import pantheist.api.syntax.model.SyntaxModelFactory;
import pantheist.api.syntax.model.SyntaxNode;
import pantheist.api.syntax.model.SyntaxToken;
import pantheist.common.except.NotFoundException;

final class SyntaxBackendImpl implements SyntaxBackend
{
	private final SyntaxModelFactory modelFactory;

	// State
	private final Map<String, SortedMap<String, Map<String, SortedMap<String, Object>>>> map;

	@Inject
	SyntaxBackendImpl(final SyntaxModelFactory modelFactory)
	{
		this.modelFactory = checkNotNull(modelFactory);

		this.map = new HashMap<>();

		for (final String resourceTypeId : listResourceTypeIds())
		{
			map.put(resourceTypeId, new TreeMap<>());
		}
	}

	@Override
	public ResourceTypeRef resourceType(final String resourceTypeId) throws NotFoundException
	{
		checkNotNullOrEmpty(resourceTypeId);
		if (!listResourceTypeIds().contains(resourceTypeId))
		{
			throw new NotFoundException("/" + resourceTypeId);
		}
		return new ResourceTypeRefImpl(this, resourceTypeId);
	}

	void checkDataType(final String resourceTypeId, final String componentTypeId, final Object data)
	{
		boolean ok = false;
		switch (resourceTypeId) {
		case "syntax":
			ok = checkSyntaxDataType(componentTypeId, data);
			break;
		}

		if (!ok)
		{
			throw new IllegalArgumentException(String.format("Data type %s is incorrect for %s//%s",
					data.getClass().getSimpleName(), resourceTypeId, componentTypeId));
		}
	}

	private boolean checkSyntaxDataType(final String componentTypeId, final Object data)
	{
		switch (componentTypeId) {
		case "node":
			return data instanceof SyntaxNode;
		case "token":
			return data instanceof SyntaxToken;
		default:
			return false;
		}
	}

	SyntaxModelFactory modelFactory()
	{
		return modelFactory;
	}

	private Collection<String> listResourceTypeIds()
	{
		return ImmutableList.of("syntax");
	}

	Collection<String> listComponentTypeIds(final String resourceTypeId)
	{
		switch (resourceTypeId) {
		case "syntax":
			return ImmutableList.of("node", "token");
		default:
			throw new IllegalArgumentException("Unrecognized resource type: " + resourceTypeId);
		}
	}

	public Map<String, SortedMap<String, Map<String, SortedMap<String, Object>>>> shareMap()
	{
		return map;
	}

}
