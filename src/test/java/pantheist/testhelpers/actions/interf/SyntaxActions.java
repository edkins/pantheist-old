package pantheist.testhelpers.actions.interf;

import java.util.List;

import pantheist.testhelpers.model.Information;

public interface SyntaxActions
{
	void createLiteralToken(String syntaxId, String value);

	void createDocRoot(String syntaxId, String rootNodeId);

	void createDocWhitespace(String syntaxId, List<String> whitespaceNodeIds);

	void deleteNode(String syntaxId, String nodeId);

	Information describeNode(String syntaxId, String nodeId);

	Information describeDocRoot(String syntaxId);

	Information describeDocWhitespace(String syntaxId);

	void assertNodeIsGone(String syntaxId, String nodeId);
}
