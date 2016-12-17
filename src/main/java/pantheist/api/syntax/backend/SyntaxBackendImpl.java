package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.api.syntax.model.SyntaxMetadata;
import pantheist.api.syntax.model.SyntaxModelFactory;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

final class SyntaxBackendImpl implements SyntaxBackend
{
	private final SyntaxModelFactory modelFactory;

	// State
	private final SortedSet<String> stuff;

	@Inject
	SyntaxBackendImpl(final SyntaxModelFactory modelFactory)
	{
		this.modelFactory = checkNotNull(modelFactory);

		this.stuff = new TreeSet<>();
	}

	@Override
	public synchronized ListSyntaxResponse listSyntax()
	{
		final List<SyntaxMetadata> list = stuff.stream().map(id -> modelFactory.syntaxMetadata("/syntax/" + id, id))
				.collect(Collectors.toList());
		return this.modelFactory.listSyntaxResponse(list);
	}

	@Override
	public synchronized void createSyntax(final String id) throws AlreadyPresentException
	{
		if (stuff.contains(id))
		{
			throw new AlreadyPresentException(id);
		}
		stuff.add(id);
	}

	@Override
	public synchronized void deleteSyntax(final String id) throws NotFoundException
	{
		if (!stuff.contains(id))
		{
			throw new NotFoundException(id);
		}
		stuff.remove(id);
	}

}
