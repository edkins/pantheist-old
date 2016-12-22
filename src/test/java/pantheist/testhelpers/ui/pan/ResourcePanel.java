package pantheist.testhelpers.ui.pan;

import pantheist.testhelpers.ui.generic.ClickableText;
import pantheist.testhelpers.ui.generic.Menu;
import pantheist.testhelpers.ui.generic.TextEntry;
import pantheist.testhelpers.ui.generic.Textual;

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

	DeletableTable syntaxNodes();

	TextEntry syntaxCreateDetail();

	TextEntry syntaxCreateExceptions();

	TextEntry trySyntaxText();

	ClickableText trySyntaxButton();

	Textual trySyntaxResult();

	void allowTimeToStabilize();
}
