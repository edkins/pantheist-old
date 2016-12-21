package pantheist.testhelpers.ui.generic;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

final class InterpretedTableImpl implements InterpretedTable
{
	private final Column headColumn;
	private final Row headRow;
	private final TableContainer<? extends TableContainer<? extends TableCell>> thead;
	private final TableContainer<? extends TableContainer<? extends TableCell>> tbody;

	private InterpretedTableImpl(final TableContainer<? extends TableContainer<? extends TableCell>> thead,
			final TableContainer<? extends TableContainer<? extends TableCell>> tbody,
			final Column headColumn,
			final Row headRow)
	{
		this.thead = checkNotNull(thead);
		this.tbody = checkNotNull(tbody);
		this.headColumn = checkNotNull(headColumn);
		this.headRow = checkNotNull(headRow);
	}

	static InterpretedTable from(final TableContainer<? extends TableContainer<? extends TableCell>> thead,
			final TableContainer<? extends TableContainer<? extends TableCell>> tbody,
			final Column headColumn, final Row headRow)
	{
		return new InterpretedTableImpl(thead, tbody, headColumn, headRow);
	}

	@Override
	public TableCell cell(final String rowIdentifier, final String columnIdentifier)
	{
		checkNotNullOrEmpty(rowIdentifier);
		checkNotNullOrEmpty(columnIdentifier);
		final TableContainer<? extends TableCell> headRow = headRow();
		final int headColumnIndex = headColumnIndex(headRow);
		final int columnIndex = indexOfTextInRow(headRow, columnIdentifier);

		for (int rowIndex = 0;; rowIndex++)
		{
			final TableCell headCell = cellAt(rowIndex, headColumnIndex);
			if (headCell.hasText(rowIdentifier))
			{
				return cellAt(rowIndex, columnIndex);
			}
			// if it can't find it we will fall off the bottom of the table and get a CannotFindElementException
		}
	}

	private TableCell cellAt(final int rowIndex, final int colIndex)
	{
		return tbody.child(rowIndex).child(colIndex);
	}

	private TableContainer<? extends TableCell> headRow()
	{
		if (!headRow.isFirstRow() || !headRow.inThead())
		{
			throw new UnsupportedOperationException("Currently only supports headRow being first row of thead");
		}
		return thead.child(0);
	}

	private int headColumnIndex(final TableContainer<? extends TableCell> headRow)
	{
		if (headColumn.isFirstColumn())
		{
			return 0;
		}
		return indexOfTextInRow(headRow, headColumn.text());
	}

	private int indexOfTextInRow(final TableContainer<? extends TableCell> headRow, final String textToFind)
	{
		for (int colIndex = 0;; colIndex++)
		{
			final TableCell cell = headRow.child(colIndex);
			if (cell.hasText(textToFind))
			{
				return colIndex;
			}
			// if it can't find it we will fall off the end of the table and get a CannotFindElementException
		}
	}
}
