package pantheist.testhelpers.ui.generic;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;
import java.util.Optional;

import pantheist.testhelpers.ui.except.CannotFindElementException;
import pantheist.testhelpers.ui.except.ElementStillPresentException;

final class InterpretedTableImpl implements InterpretedTable
{
	private final Column headColumn;
	private final Row headRow;
	private final TableContainer<? extends TableContainer<? extends ContainerWithText>> thead;
	private final TableContainer<? extends TableContainer<? extends ContainerWithText>> tbody;

	private InterpretedTableImpl(final TableContainer<? extends TableContainer<? extends ContainerWithText>> thead,
			final TableContainer<? extends TableContainer<? extends ContainerWithText>> tbody,
			final Column headColumn,
			final Row headRow)
	{
		this.thead = checkNotNull(thead);
		this.tbody = checkNotNull(tbody);
		this.headColumn = checkNotNull(headColumn);
		this.headRow = checkNotNull(headRow);
	}

	static InterpretedTable from(final TableContainer<? extends TableContainer<? extends ContainerWithText>> thead,
			final TableContainer<? extends TableContainer<? extends ContainerWithText>> tbody,
			final Column headColumn, final Row headRow)
	{
		return new InterpretedTableImpl(thead, tbody, headColumn, headRow);
	}

	private Optional<Integer> findRowIndex(final String rowIdentifier)
	{
		final int headColumnIndex = headColumnIndex(headRow());
		final int rowCount = tbody.childCount();
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
		{
			final ContainerWithText headCell = cellAt(rowIndex, headColumnIndex);
			if (headCell.hasText(rowIdentifier))
			{
				return Optional.of(rowIndex);
			}
		}
		return Optional.empty();
	}

	@Override
	public ContainerWithText cell(final String rowIdentifier, final String columnIdentifier)
	{
		checkNotNullOrEmpty(rowIdentifier);
		checkNotNullOrEmpty(columnIdentifier);
		final int columnIndex = indexOfTextInRow(headRow(), columnIdentifier);

		final int rowIndex = findRowIndex(rowIdentifier)
				.orElseThrow(() -> new CannotFindElementException("Cannot find row in table: " + rowIdentifier));

		return cellAt(rowIndex, columnIndex);
	}

	private ContainerWithText cellAt(final int rowIndex, final int colIndex)
	{
		return tbody.child(rowIndex).child(colIndex);
	}

	private TableContainer<? extends ContainerWithText> headRow()
	{
		if (!headRow.isFirstRow() || !headRow.inThead())
		{
			throw new UnsupportedOperationException("Currently only supports headRow being first row of thead");
		}
		return thead.child(0);
	}

	private int headColumnIndex(final TableContainer<? extends ContainerWithText> headRow)
	{
		if (headColumn.isFirstColumn())
		{
			return 0;
		}
		return indexOfTextInRow(headRow, headColumn.text());
	}

	private int indexOfTextInRow(final TableContainer<? extends ContainerWithText> headRow, final String textToFind)
	{
		final int colCount = headRow.childCount();
		for (int colIndex = 0; colIndex < colCount; colIndex++)
		{
			final ContainerWithText cell = headRow.child(colIndex);
			if (cell.hasText(textToFind))
			{
				return colIndex;
			}
		}
		throw new CannotFindElementException("Cannot find element in table row: " + textToFind);
	}

	@Override
	public void assertNoRow(final String rowIdentifier)
	{
		if (findRowIndex(rowIdentifier).isPresent())
		{
			throw new ElementStillPresentException("Row still present in table: " + rowIdentifier);
		}
	}

	@Override
	public ContainerElement row(final String rowIdentifier)
	{
		final int rowIndex = findRowIndex(rowIdentifier)
				.orElseThrow(() -> new CannotFindElementException("Cannot find row in table: " + rowIdentifier));

		return tbody.child(rowIndex);
	}

	@Override
	public boolean hasRow(final String rowIdentifier)
	{
		return findRowIndex(rowIdentifier).isPresent();
	}

	@Override
	public ElementFinder<? extends ContainerWithText> column(final String columnIdentifier)
	{
		final int columnIndex = indexOfTextInRow(headRow(), columnIdentifier);
		return tbody.column(columnIndex);
	}

	private ElementFinder<? extends ContainerWithText> identificationColumn()
	{
		return tbody.column(headColumnIndex(headRow()));
	}

	@Override
	public List<String> rowIdentifiers()
	{
		return identificationColumn().all().texts();
	}
}
