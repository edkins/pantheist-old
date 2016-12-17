package pantheist.api.syntax.backend;

import pantheist.api.syntax.model.ListSyntaxResponse;

public interface SyntaxBackend
{
	/**
	 * @return a list of all the known syntax structures, sorted alphabetically.
	 */
	ListSyntaxResponse listSyntax();
}
