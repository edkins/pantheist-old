package pantheist.testhelpers.ui.generic;

import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

public class Column
{
	public static final Column FIRST = new Column(null);

	private final String id;

	private Column(final String id)
	{
		this.id = id;
	}

	public static Column identifiedBy(final String id)
	{
		checkNotNullOrEmpty(id);
		return new Column(id);
	}

	public boolean isFirstColumn()
	{
		return id == null;
	}

	/**
	 * @return the text identifying this column
	 * @throws IllegalStateException
	 *             if not identified by text
	 */
	public String text()
	{
		if (id == null)
		{
			throw new IllegalStateException("Column not identified by text");
		}
		return id;
	}
}
