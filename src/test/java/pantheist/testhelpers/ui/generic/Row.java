package pantheist.testhelpers.ui.generic;

public class Row
{
	public static final Row THEAD_FIRST = new Row(null);

	private final String id;

	private Row(final String id)
	{
		this.id = id;
	}

	public boolean isFirstRow()
	{
		return id == null;
	}

	public boolean inThead()
	{
		return true;
	}
}
