package pantheist.api.syntax.backend;

import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

public interface SyntaxBackend
{
	/**
	 * @return a list of all the known syntax structures, sorted alphabetically.
	 */
	ListSyntaxResponse listSyntax();

	/**
	 * Creates a new empty syntax object with the given identifier.
	 *
	 * @param id
	 * @throws AlreadyPresentException
	 *             if the given named resource already exists
	 */
	void createSyntax(String id) throws AlreadyPresentException;

	/**
	 * Deletes the syntax object with the given identifier.
	 *
	 * @param id
	 * @throws NotFoundException
	 *             if no resource of the correct type exists with that
	 *             identifier.
	 */
	void deleteSyntax(String id) throws NotFoundException;
}
