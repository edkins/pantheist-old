package pantheist.testhelpers.ui.pan;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.InterpretedTable;
import pantheist.testhelpers.ui.generic.Menu;
import pantheist.testhelpers.ui.generic.TextEntry;

/**
 * Represents the main panel when it is pointing to a particular resource.
 */
public interface ResourcePanel
{
	void assertDisplayed();

	ClickableText deleteButton();

	Menu syntaxCreateType();

	ClickableText syntaxCreateButton();

	TextEntry syntaxCreateName();

	InterpretedTable syntaxNodes();

	InterpretedTable syntaxDoc();

	TextEntry syntaxDocNodeList();
}
