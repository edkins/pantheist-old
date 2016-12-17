package pantheist.common.except;

import java.util.List;

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

	public static <T> List<T> copyOfNotNull(final List<T> list)
	{
		if (list == null)
		{
			throw new NullPointerException("Argument is null");
		}
		return ImmutableList.copyOf(list);
	}
}
