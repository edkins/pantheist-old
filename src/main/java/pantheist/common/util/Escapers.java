package pantheist.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.common.base.Throwables;

public class Escapers
{
	private Escapers()
	{
		throw new UnsupportedOperationException();
	}

	public static String url(final String segment)
	{
		try
		{
			return URLEncoder.encode(segment, "utf-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			throw Throwables.propagate(e);
		}
	}
}
