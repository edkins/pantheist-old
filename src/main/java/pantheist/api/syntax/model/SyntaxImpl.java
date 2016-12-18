package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

import pantheist.api.syntax.backend.ComponentType;

final class SyntaxImpl implements Syntax
{
	private final String path;
	private final String id;
	private final String name;
	private final Map<ComponentType, SortedMap<String, Object>> components;

	@Inject
	SyntaxImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("id") @JsonProperty("id") final String id,
			@Assisted("name") @JsonProperty("name") final String name,
			@Assisted @JsonProperty("components") final Map<ComponentType, SortedMap<String, Object>> components)
	{
		this.path = checkNotNullOrEmpty(path);
		this.id = checkNotNullOrEmpty(id);
		this.name = checkNotNullOrEmpty(name);
		this.components = checkNotNull(components);
	}

	@Override
	public String path()
	{
		return path;
	}

	@Override
	public String id()
	{
		return id;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public Map<ComponentType, SortedMap<String, Object>> components()
	{
		return components;
	}

	@Override
	public <T> Syntax withComponent(final ComponentType componentType, final String componentId, final T data)
	{
		componentType.verifyType(data);

		final SortedMap<String, Object> newMap = new TreeMap<>(components.get(componentType));
		newMap.put(componentId, data);
		final Map<ComponentType, SortedMap<String, Object>> newComponents = new HashMap<>(components);
		newComponents.put(componentType, newMap);
		return new SyntaxImpl(path, id, name, newComponents);
	}

	@Override
	public Syntax withoutComponent(final ComponentType componentType, final String componentId)
	{
		final SortedMap<String, Object> newMap = new TreeMap<>(components.get(componentType));
		newMap.remove(componentId);
		final Map<ComponentType, SortedMap<String, Object>> newComponents = new HashMap<>(components);
		newComponents.put(componentType, newMap);
		return new SyntaxImpl(path, id, name, newComponents);
	}

}
