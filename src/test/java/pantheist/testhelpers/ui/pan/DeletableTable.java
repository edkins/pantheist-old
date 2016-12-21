package pantheist.testhelpers.ui.pan;

import pantheist.testhelpers.ui.generic.ContainerWithText;

public interface DeletableTable
{
	ContainerWithText cell(String rowIdentifier, String columnIdentifier);

	void assertNoRow(String rowIdentifier);

	boolean hasRow(String rowIdentifier);

	void deleteRow(String rowIdentifier);
}
