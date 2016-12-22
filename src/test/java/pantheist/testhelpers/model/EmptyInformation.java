package pantheist.testhelpers.model;

import static org.junit.Assert.fail;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

final class EmptyInformation implements Information
{
	public static Information empty()
	{
		return new EmptyInformation();
	}

	@Override
	public Information field(final String name)
	{
		throw new IllegalStateException("No fields because empty");
	}

	@Override
	public void assertString(final String value)
	{
		checkNotNullOrEmpty(value);
		fail("Not a string, instead empty");
	}

	@Override
	public void assertStringList(final String... values)
	{
		fail("Not a string list, instead empty");
	}

	@Override
	public void assertEmpty()
	{
		// ok
	}

	@Override
	public Information singleOrEmpty()
	{
		return this;
	}

}
