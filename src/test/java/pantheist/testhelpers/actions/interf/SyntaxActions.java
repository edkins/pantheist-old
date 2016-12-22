package pantheist.testhelpers.actions.interf;

import java.util.List;

import pantheist.testhelpers.model.Information;

public interface SyntaxActions
{
	void createLiteralToken(String syntaxId, String value);

	void createSingleCharacterMatcher(String syntaxId, String nodeId, List<String> options, List<String> exception);

	void createZeroOrMoreNodeGlued(String syntaxId, String nodeId, String child);

	void createOneOrMoreNodeGlued(String syntaxId, String nodeId, String child);

	void createSequenceNodeGlued(String syntaxId, String nodeId, List<String> children);

	void createZeroOrMoreNodeSeparated(String syntaxId, String nodeId, String child);

	void createOneOrMoreNodeSeparated(String syntaxId, String nodeId, String child);

	void createSequenceNodeSeparated(String syntaxId, String nodeId, List<String> children);

	void createChoiceNode(String syntaxId, String nodeId, List<String> children);

	void setDocRoot(String syntaxId, String nodeId);

	void setDocDelim(String syntaxId, String nodeId);

	void deleteNode(String syntaxId, String nodeId);

	Information describeNode(String syntaxId, String nodeId);

	Information docRootNode(String syntaxId);

	Information docDelimNode(String syntaxId);

	void assertNodeIsGone(String syntaxId, String nodeId);

	Information tryOutSyntax(String syntaxId, String document);
}
