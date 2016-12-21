package pantheist.common.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class Make
{
	private Make()
	{
		throw new UnsupportedOperationException();
	}

	public static <T> ImmutableList<T> list(final List<T> xs, final T x)
	{
		return ImmutableList.<T>builder().addAll(xs).add(x).build();
	}

	public static <K, V> ImmutableMap<K, V> map(final Map<K, V> map, final K key, final V value)
	{
		checkNotNull(map);
		checkNotNull(key);
		checkNotNull(value);
		return ImmutableMap.<K, V>builder().putAll(map).put(key, value).build();
	}
}
