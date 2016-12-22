package pantheist.api.syntax.model;

import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import com.google.common.base.CharMatcher;

public final class SingleCharMatchers
{
	private SingleCharMatchers()
	{
		throw new UnsupportedOperationException();
	}

	public static CharMatcher fromString(final String str)
	{
		checkNotNullOrEmpty(str);
		if (str.length() == 1)
		{
			return CharMatcher.is(str.charAt(0));
		}

		switch (str) {
		case "space":
			return CharMatcher.is(' ');
		case "newline":
			return CharMatcher.is('\n');
		case "tab":
			return CharMatcher.is('\t');
		case "digit":
			return CharMatcher.inRange('0', '9');
		case "latin_lower":
			return CharMatcher.inRange('a', 'z');
		case "latin_upper":
			return CharMatcher.inRange('A', 'Z');
		case "visible_ascii":
			return CharMatcher.inRange('!', '~');
		default:
			throw new IllegalArgumentException("Unrecognized single character class: " + str);
		}
	}
}
