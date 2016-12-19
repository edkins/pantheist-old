package pantheist.api.generic.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

/**
 * I'm not sure this is a very efficient way of going about it, but it's fun.
 *
 * Build a mutable map object from a mutable java object, one property at a
 * time.
 *
 * It's assumed that the object's properties are nullable, and if they're null
 * this map presents they're not present.
 *
 * You can even specify different types for each property!
 */
public class IntolerantMapBuilder<T>
{
	private final T obj;
	private final SortedMap<String, PropertyEntry<T, ?>> properties;

	private IntolerantMapBuilder(final T obj)
	{
		this.obj = checkNotNull(obj);
		this.properties = new TreeMap<>();
	}

	public static <T> IntolerantMapBuilder<T> wrap(final T obj)
	{
		return new IntolerantMapBuilder<>(obj);
	}

	public <U> IntolerantMapBuilder<T> with(
			final String name,
			final Class<U> clazz,
			final Function<T, U> getter,
			final BiConsumer<T, U> setter)
	{
		checkNotNullOrEmpty(name);
		if (properties.containsKey(name))
		{
			throw new IllegalStateException("Already have property " + name);
		}

		properties.put(name, new PropertyEntry<>(obj, clazz, getter, setter));
		return this;
	}

	public IntolerantMap build()
	{
		return new IntolerantMapClass<>(properties);
	}

	private static class IntolerantMapClass<T> implements IntolerantMap
	{
		private final SortedMap<String, PropertyEntry<T, ?>> properties;

		IntolerantMapClass(final SortedMap<String, PropertyEntry<T, ?>> properties)
		{
			this.properties = checkNotNull(properties);
		}

		@Override
		public List<String> sortedKeyList()
		{
			return properties.keySet()
					.stream()
					.filter(this::containsKey)
					.collect(Collectors.toList());
		}

		@Override
		public List<Entry<String, Object>> sortedEntryList()
		{
			return properties.keySet()
					.stream()
					.filter(this::containsKey)
					.map(key -> new IntolerantEntryImpl(key, get(key)))
					.collect(Collectors.toList());
		}

		@Override
		public void put(final String key, final Object value)
		{
			checkNotNull(value);
			putNullable(key, value);
		}

		private void putNullable(final String key, @Nullable final Object value)
		{
			checkNotNullOrEmpty(key);
			if (!properties.containsKey(key))
			{
				throw new IllegalArgumentException("Key not allowed: " + key);
			}
			properties.get(key).setData(value);
		}

		@Override
		public Object get(final String key)
		{
			if (!properties.containsKey(key))
			{
				return null;
			}
			return properties.get(key).getData();
		}

		@Override
		public Object remove(final String key)
		{
			// Set to null and hope for the best (since we don't have separate remover functions)
			final Object result = get(key);
			putNullable(key, null);
			return result;
		}

		@Override
		public boolean containsKey(final String key)
		{
			return get(key) != null;
		}

	}

	private static class PropertyEntry<T, U>
	{
		private final T obj;
		private final Class<U> clazz;
		private final Function<T, U> getter;
		private final BiConsumer<T, U> setter;

		PropertyEntry(final T obj, final Class<U> clazz, final Function<T, U> getter, final BiConsumer<T, U> setter)
		{
			this.obj = checkNotNull(obj);
			this.clazz = checkNotNull(clazz);
			this.getter = checkNotNull(getter);
			this.setter = checkNotNull(setter);
		}

		public Object getData()
		{
			return getter.apply(obj);
		}

		public void setData(@Nullable final Object data)
		{
			setter.accept(obj, clazz.cast(data));
		}
	}
}
