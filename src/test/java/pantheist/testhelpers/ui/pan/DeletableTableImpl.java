package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import pantheist.testhelpers.ui.generic.InterpretedTable;
import pantheist.testhelpers.ui.generic.TableCell;

final class DeletableTableImpl implements DeletableTable
{
	private final InterpretedTable table;

	DeletableTableImpl(final InterpretedTable table)
	{
		this.table = checkNotNull(table);
	}

	@Override
	public TableCell cell(final String rowIdentifier, final String columnIdentifier)
	{
		return table.cell(rowIdentifier, columnIdentifier);
	}

	@Override
	public void assertNoRow(final String rowIdentifier)
	{
		table.assertNoRow(rowIdentifier);
	}

	@Override
	public boolean hasRow(final String rowIdentifier)
	{
		return table.hasRow(rowIdentifier);
	}

	@Override
	public void deleteRow(final String rowIdentifier)
	{
		table.row(rowIdentifier).inputButton().withValue("Del").click();
	}
}
