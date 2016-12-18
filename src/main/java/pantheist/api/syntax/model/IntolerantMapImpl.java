package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

public class IntolerantMapImpl<T> implements IntolerantMap
{
	private final SortedMap<String, T> map;
	private final Class<T> clazz;

	public IntolerantMapImpl(final SortedMap<String, T> map, final Class<T> clazz)
	{
		this.map = checkNotNull(map);
		this.clazz = checkNotNull(clazz);
	}

	@Override
	public List<String> sortedKeyList()
	{
		return ImmutableList.copyOf(map.keySet());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void put(final String key, final Object value)
	{
		checkNotNull(value);
		try
		{
			clazz.cast(value);
		}
		catch (final ClassCastException e)
		{
			throw new IllegalArgumentException(e);
		}
		map.put(key, (T) value);
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
				.map(e -> new IntolerantEntryImpl(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

	private static class IntolerantEntryImpl implements Entry<String, Object>
	{
		private final String key;
		private final Object value;

		IntolerantEntryImpl(final String key, final Object value)
		{
			this.key = checkNotNullOrEmpty(key);
			this.value = checkNotNull(value);
		}

		@Override
		public String getKey()
		{
			return key;
		}

		@Override
		public Object getValue()
		{
			return value;
		}

		@Override
		public Object setValue(final Object value)
		{
			throw new UnsupportedOperationException();
		}

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
}
