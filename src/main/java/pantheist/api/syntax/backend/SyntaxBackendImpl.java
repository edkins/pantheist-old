package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import pantheist.api.syntax.model.ListNodeResponse;
import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxMetadata;
import pantheist.api.syntax.model.SyntaxModelFactory;
import pantheist.api.syntax.model.SyntaxNode;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

final class SyntaxBackendImpl implements SyntaxBackend
{
	private final SyntaxModelFactory modelFactory;

	// State
	private final Map<String, Syntax> syntax;

	@Inject
	SyntaxBackendImpl(final SyntaxModelFactory modelFactory)
	{
		this.modelFactory = checkNotNull(modelFactory);

		this.syntax = new HashMap<>();
	}

	@Override
	public synchronized ListSyntaxResponse listSyntax()
	{
		final SortedSet<String> sortedKeys = new TreeSet<>(syntax.keySet());
		final List<SyntaxMetadata> list = sortedKeys
				.stream()
				.map(this::getMetadata)
				.collect(Collectors.toList());
		return this.modelFactory.listSyntaxResponse(list);
	}

	private SyntaxMetadata getMetadata(final String id)
	{
		final Syntax s = syntax.get(id);
		return modelFactory.syntaxMetadata(s.path(), s.name());
	}

	@Override
	public synchronized void createSyntax(final String id) throws AlreadyPresentException
	{
		if (syntax.containsKey(id))
		{
			throw new AlreadyPresentException(id);
		}
		syntax.put(id, emptySyntax(id));
	}

	private Syntax emptySyntax(final String id)
	{
		return modelFactory.syntax("/syntax/" + id, id, id, ImmutableList.of());
	}

	@Override
	public synchronized void deleteSyntax(final String id) throws NotFoundException
	{
		if (!syntax.containsKey(id))
		{
			throw new NotFoundException(id);
		}
		syntax.remove(id);
	}

	@Override
	public synchronized ListNodeResponse listNodes(final String syn) throws NotFoundException
	{
		if (!syntax.containsKey(syn))
		{
			throw new NotFoundException(syn);
		}
		final List<SyntaxNode> nodes = syntax.get(syn).nodes();
		return modelFactory.listNodeResponse(nodes);
	}

}
