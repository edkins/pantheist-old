package pantheist.testhelpers.model;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;

import pantheist.common.util.Make;

public class InformationListBuilder implements Information
{
	private final ImmutableList<Information> list;

	private InformationListBuilder(final ImmutableList<Information> list)
	{
		this.list = checkNotNull(list);
	}

	public static InformationListBuilder create()
	{
		return new InformationListBuilder(ImmutableList.of());
	}

	public InformationListBuilder add(final Information item)
	{
		return new InformationListBuilder(Make.list(list, item));
	}

	@Override
	public Information field(final String name)
	{
		throw new IllegalStateException("Is a list not a map");
	}

	@Override
	public Information singleOrEmpty()
	{
		if (list.size() > 1)
		{
			throw new IllegalStateException(
					"List is supposed to be singleton but has " + list.size() + " elements");
		}
		else if (list.size() == 1)
		{
			return list.get(0);
		}
		else
		{
			return Informations.empty();
		}
	}

	@Override
	public void assertString(final String value)
	{
		throw new IllegalStateException("Is a list not a string");
	}

	@Override
	public void assertStringList(final String... values)
	{
		if (list.size() != values.length)
		{
			throw new IllegalStateException("List length should be " + values.length + " but actually " + list.size());
		}
		for (int i = 0; i < values.length; i++)
		{
			list.get(i).assertString(values[i]);
		}
	}

	@Override
	public void assertEmpty()
	{
		if (!list.isEmpty())
		{
			throw new IllegalStateException("List is not empty");
		}
	}
}
