package pantheist.testhelpers.ui.generic;

import java.util.List;

/**
 * Represents an element containing tabular data.
 *
 * This has already been set up with information on which row and column
 * contains the identifiers. (Usually the first column contains the row
 * identifier, and the first row contains the column identifer, but not
 * necessarily)
 */
public interface InterpretedTable
{
	/**
	 * Returns a particular cell, identified by values in the head column/row.
	 *
	 * @param rowIdentifier
	 *            value to search for in head column
	 * @param columnIdentifier
	 *            value to search for in head row
	 * @return cell within the table body
	 */
	ContainerWithText cell(String rowIdentifier, String columnIdentifier);

	/**
	 * Asserts that there is no row with the given identifier.
	 *
	 * @param rowIdentifier
	 */
	void assertNoRow(String rowIdentifier);

	/**
	 * @return whether the row exists or not.
	 */
	boolean hasRow(String rowIdentifier);

	/**
	 * Return a given row as a container. Useful for finding things where you
	 * can describe the element and know which row it's in but not which column.
	 *
	 * @param rowIdentifier
	 * @return
	 */
	ContainerElement row(String rowIdentifier);

	/**
	 * Return an ElementFinder for the given column.
	 *
	 * The column doesn't have a corresponding html element, so the current
	 * implementation would make it tricky to return a ContainerElement here.
	 * Instead it's assumed you're searching for all the td's.
	 *
	 * @param columnIdentifier
	 *            column identifier
	 * @return td element finder
	 */
	ElementFinder<? extends ContainerWithText> column(String columnIdentifier);

	/**
	 * @return a list of row identifiers, in the order that they appear in the
	 *         table.
	 */
	List<String> rowIdentifiers();
}
