package pantheist.testhelpers.actions.interf;

import pantheist.testhelpers.model.Information;

public interface SyntaxActions
{

	void createLiteralToken(String syntaxId, String value);

	Information describeNode(String syntaxId, String nodeId);
}
