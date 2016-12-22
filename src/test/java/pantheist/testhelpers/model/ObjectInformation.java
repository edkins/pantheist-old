package pantheist.testhelpers.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

final class ObjectInformation implements Information
{
	private final Object value;

	private ObjectInformation(final Object value)
	{
		this.value = checkNotNull(value);
	}

	static Information ofNullable(@Nullable final Object value)
	{
		if (value == null)
		{
			return EmptyInformation.empty();
		}
		return new ObjectInformation(value);
	}

	@Override
	public Information field(final String name)
	{
		checkNotNullOrEmpty(name);
		if (value instanceof Map)
		{
			return new ObjectInformation(((Map) value).get(name));
		}
		else
		{
			throw new IllegalStateException("Trying to access field of non-map " + value);
		}
	}

	@Override
	public void assertString(final String expectedValue)
	{
		checkNotNullOrEmpty(expectedValue);
		assertThat(value, is(expectedValue));
	}

	@Override
	public void assertStringList(final String... values)
	{
		assertEquals(ImmutableList.copyOf(values), value);
	}

	@Override
	public void assertEmpty()
	{
		assertThat(value, anyOf(is(""), is(ImmutableList.of()), is(ImmutableMap.of())));
	}

}
