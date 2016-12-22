package pantheist.testhelpers.ui.pan;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Optional;

import pantheist.common.util.MutableOptional;
import pantheist.testhelpers.ui.generic.ContainerWithText;
import pantheist.testhelpers.ui.generic.InterpretedTable;

final class DeletableTableImpl implements DeletableTable
{
	private final InterpretedTable table;

	DeletableTableImpl(final InterpretedTable table)
	{
		this.table = checkNotNull(table);
	}

	@Override
	public ContainerWithText cell(final String rowIdentifier, final String columnIdentifier)
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

	@Override
	public List<String> rowIdentifiers()
	{
		return table.rowIdentifiers();
	}

	@Override
	public Optional<String> findCheckedRow(final String columnIdentifier)
	{
		final MutableOptional<String> result = MutableOptional.empty();
		for (final String rowId : rowIdentifiers())
		{
			if (cell(rowId, columnIdentifier).inputRadio().choose().isChecked())
			{
				result.add(rowId);
			}
		}
		return result.value();
	}
}
