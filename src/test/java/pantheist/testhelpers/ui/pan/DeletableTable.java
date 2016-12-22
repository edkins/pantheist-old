package pantheist.testhelpers.ui.pan;

import java.util.List;
import java.util.Optional;

import pantheist.testhelpers.ui.generic.ContainerWithText;

public interface DeletableTable
{
	ContainerWithText cell(String rowIdentifier, String columnIdentifier);

	void assertNoRow(String rowIdentifier);

	boolean hasRow(String rowIdentifier);

	void deleteRow(String rowIdentifier);

	List<String> rowIdentifiers();

	/**
	 * Return the row identifier that has a checkmark here, or empty if none
	 * are.
	 *
	 * @param columnIdentifier
	 *            column identifier
	 * @return row identifier or empty
	 * @throws IllegalStateException
	 *             if multiple rows are checked.
	 */
	Optional<String> findCheckedRow(String columnIdentifier);
}
