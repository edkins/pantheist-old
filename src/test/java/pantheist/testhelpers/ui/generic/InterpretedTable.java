package pantheist.testhelpers.ui.generic;

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
	TableCell cell(String rowIdentifier, String columnIdentifier);

	/**
	 * Asserts that there is no row with the given identifier.
	 *
	 * @param rowIdentifier
	 */
	void assertNoRow(String rowIdentifier);

	/**
	 * Return a given row as a container. Useful for finding things where you
	 * can describe the element and know which row it's in but not which column.
	 * 
	 * @param rowIdentifier
	 * @return
	 */
	ContainerElement row(String rowIdentifier);
}
