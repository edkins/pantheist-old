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

	public static <T> List<T> nullOrEmptyList(final List<T> list)
	{
		if (list == null || list.isEmpty())
		{
			return ImmutableList.of();
		}
		throw new IllegalArgumentException("List is not null or empty");
	}

	public static <T> List<T> copyOfSingleton(final List<T> list)
	{
		if (list == null || list.size() != 1)
		{
			throw new IllegalArgumentException("List is not singleton");
		}
		return ImmutableList.copyOf(list);
	}

	public static <T> List<T> copyOfTwoOrMore(final List<T> list)
	{
		if (list == null || list.size() < 2)
		{
			throw new IllegalArgumentException("List does not contain at least 2 elements");
		}
		return ImmutableList.copyOf(list);
	}
}
