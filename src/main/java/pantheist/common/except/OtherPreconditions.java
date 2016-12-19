package pantheist.common.except;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.collect.ImmutableList;

public class OtherPreconditions
{
	private OtherPreconditions()
	{
		throw new UnsupportedOperationException();
	}

	public static String checkNotNullOrEmpty(final String arg)
	{
		if (arg == null)
		{
			throw new NullPointerException("Argument is null");
		}
		if (arg.isEmpty())
		{
			throw new IllegalArgumentException("Argument is empty");
		}
		return arg;
	}

	public static <T> List<T> copyOfNotNull(final Collection<T> list)
	{
		if (list == null)
		{
			throw new NullPointerException("Argument is null");
		}
		return ImmutableList.copyOf(list);
	}

	public static <K, T> SortedMap<K, T> copyOfNotNullSorted(final SortedMap<K, T> map)
	{
		if (map == null)
		{
			throw new NullPointerException("Argument is null");
		}
		return new TreeMap<>(map);
	}

	/**
	 * @param list
	 *            must be null or empty
	 * @return an immutable empty list
	 * @throws IllegalArgumentException
	 *             if input list is not null or empty
	 */
	public static <T> List<T> nullOrEmptyList(final List<T> list)
	{
		if (list == null || list.isEmpty())
		{
			return ImmutableList.of();
		}
		throw new IllegalArgumentException("List is not null or empty");
	}

	/**
	 * @param list
	 *            must contain exactly one element
	 * @return an immutable copy of the input list
	 * @throws IllegalArgumentException
	 *             if input list is null, empty or has size greater than 1
	 */
	public static <T> List<T> copyOfSingleton(final List<T> list)
	{
		if (list == null || list.size() != 1)
		{
			throw new IllegalArgumentException("List is not singleton");
		}
		return ImmutableList.copyOf(list);
	}

	/**
	 * @param list
	 *            must contain at least one element
	 * @return an immutable copy of the input list
	 * @throws IllegalArgumentException
	 *             if input list is null or empty
	 */
	public static <T> List<T> copyOfOneOrMore(final List<T> list)
	{
		if (list == null || list.isEmpty())
		{
			throw new IllegalArgumentException("List does not contain at least 1 elements");
		}
		return ImmutableList.copyOf(list);
	}
}
