package pantheist.testhelpers.ui.generic;

/**
 * Represents a table where you haven't yet told it which row and column
 * contains the relevant headings.
 */
public interface ProtoTable
{
	InterpretedTable interpret(Column headColumn, Row headRow);
}
