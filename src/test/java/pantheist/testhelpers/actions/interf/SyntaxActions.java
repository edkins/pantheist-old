package pantheist.testhelpers.actions.interf;

import pantheist.testhelpers.model.Information;

public interface SyntaxActions
{
	void createLiteralToken(String syntaxId, String value);

	void deleteNode(String syntaxId, String nodeId);

	Information describeNode(String syntaxId, String nodeId);

	void assertNodeIsGone(String syntaxId, String nodeId);
}
