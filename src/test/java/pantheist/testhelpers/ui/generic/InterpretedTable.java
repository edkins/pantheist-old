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
}
