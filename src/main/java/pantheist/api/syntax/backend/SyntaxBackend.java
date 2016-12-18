package pantheist.api.syntax.backend;

import pantheist.api.syntax.model.TryOutTextReport;
import pantheist.common.except.NotFoundException;

public interface SyntaxBackend
{
	/**
	 * Try the given text out against the given syntax, and return a report of
	 * what happened.
	 *
	 * @param syntaxId
	 * @param text
	 * @return
	 */
	TryOutTextReport tryOutText(String syntaxId, String text) throws NotFoundException;
}
