package pantheist.common.util;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class OtherLists
{
	private OtherLists()
	{
		throw new UnsupportedOperationException();
	}

	public static <T> List<T> add(final List<T> xs, final T x)
	{
		return ImmutableList.<T>builder().addAll(xs).add(x).build();
	}
}
