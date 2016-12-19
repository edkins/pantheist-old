package pantheist.api.generic.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedMap;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

public class TypedMapImpl<T> implements TypedMap
{
	private final SortedMap<String, T> map;
	private final Class<T> clazz;

	private TypedMapImpl(final SortedMap<String, T> map, final Class<T> clazz)
	{
		this.map = checkNotNull(map);
		this.clazz = checkNotNull(clazz);
	}

	public static <T> TypedMap of(final SortedMap<String, T> map, final Class<T> clazz)
	{
		return new TypedMapImpl<>(map, clazz);
	}

	@Override
	public List<String> sortedKeyList()
	{
		return ImmutableList.copyOf(map.keySet());
	}

	@Override
	public void put(final String key, final Object value)
	{
		checkNotNull(value);
		map.put(key, clazz.cast(value));
	}

	@Override
	public Object get(final String key)
	{
		return map.get(key);
	}

	@Override
	public List<Entry<String, Object>> sortedEntryList()
	{
		return map.entrySet().stream()
				.map(e -> new TypedMapEntryImpl(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

	@Override
	public Object remove(final String key)
	{
		return map.remove(key);
	}

	@Override
	public boolean containsKey(final String key)
	{
		return map.containsKey(key);
	}

	@Override
	public Optional<Class<?>> typeOf(final String key)
	{
		return Optional.of(clazz);
	}
}
