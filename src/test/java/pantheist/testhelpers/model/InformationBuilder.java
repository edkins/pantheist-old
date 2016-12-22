package pantheist.testhelpers.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import com.google.common.collect.ImmutableMap;

import pantheist.common.util.Make;

public class InformationBuilder implements Information
{
	private final ImmutableMap<String, Information> map;

	private InformationBuilder(final ImmutableMap<String, Information> map)
	{
		this.map = checkNotNull(map);
	}

	public static InformationBuilder create()
	{
		return new InformationBuilder(ImmutableMap.of());
	}

	public InformationBuilder with(final String key, final Information value)
	{
		checkNotNullOrEmpty(key);
		checkNotNull(value);

		return new InformationBuilder(Make.map(map, key, value));
	}

	@Override
	public Information field(final String name)
	{
		checkNotNullOrEmpty(name);
		if (map.containsKey(name))
		{
			return map.get(name);
		}
		return Informations.empty();
	}

	@Override
	public void assertString(final String value)
	{
		fail("Is a map not a string");
	}

	@Override
	public void assertStringList(final String... values)
	{
		fail("Is a map not a string list");
	}

	@Override
	public void assertEmpty()
	{
		assertTrue("Map is not empty", map.isEmpty());
	}

	@Override
	public Information singleOrEmpty()
	{
		throw new IllegalStateException("Is a map not a list");
	}
}
