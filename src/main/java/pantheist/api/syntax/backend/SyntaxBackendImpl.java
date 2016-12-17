package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import pantheist.api.syntax.model.ListNodeResponse;
import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.api.syntax.model.PutNodeRequest;
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
	private final SortedMap<String, Syntax> syntax;

	@Inject
	SyntaxBackendImpl(final SyntaxModelFactory modelFactory)
	{
		this.modelFactory = checkNotNull(modelFactory);

		this.syntax = new TreeMap<>();
	}

	@Override
	public synchronized ListSyntaxResponse listSyntax()
	{
		final List<SyntaxMetadata> list = syntax.values()
				.stream()
				.map(this::getMetadata)
				.collect(Collectors.toList());
		return this.modelFactory.listSyntaxResponse(list);
	}

	private SyntaxMetadata getMetadata(final Syntax syn)
	{
		return modelFactory.syntaxMetadata(syn.path(), syn.name());
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
		return modelFactory.syntax(syntaxPath(id), id, id, new TreeMap<>());
	}

	private String syntaxPath(final String id)
	{
		return "/syntax/" + id;
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
	public synchronized ListNodeResponse listNodes(final String syntaxId) throws NotFoundException
	{
		if (!syntax.containsKey(syntaxId))
		{
			throw new NotFoundException(syntaxId);
		}

		final Collection<SyntaxNode> nodes = syntax.get(syntaxId).nodes().values();
		return modelFactory.listNodeResponse(nodes);
	}

	@Override
	public synchronized SyntaxNode getNode(final String syntaxId, final String nodeId) throws NotFoundException
	{
		final Optional<SyntaxNode> result = findNode(syntaxId, nodeId);
		if (!result.isPresent())
		{
			throw new NotFoundException(nodeId);
		}
		return result.get();
	}

	private Optional<SyntaxNode> findNode(final String syntaxId, final String nodeId) throws NotFoundException
	{
		if (!syntax.containsKey(syntaxId))
		{
			throw new NotFoundException(syntaxId);
		}
		return Optional.ofNullable(syntax.get(syntaxId).nodes().get(nodeId));
	}

	private static <T> void checkPresence(final Optional<T> optional, final boolean expected, final String id)
			throws NotFoundException, AlreadyPresentException
	{
		if (expected)
		{
			if (!optional.isPresent())
			{
				throw new NotFoundException(id);
			}
		}
		else
		{
			if (optional.isPresent())
			{
				throw new AlreadyPresentException(id);
			}
		}
	}

	@Override
	public synchronized void putNode(final String syntaxId, final String nodeId, final PutNodeRequest request)
			throws NotFoundException, AlreadyPresentException
	{
		final SyntaxNode newNode = nodeFromRequest(syntaxId, nodeId, request);

		checkPresence(findNode(syntaxId, nodeId), request.updateExisting(), nodeId);

		final Syntax newSyntax = syntax.get(syntaxId).withNode(newNode);
		syntax.put(syntaxId, newSyntax);
	}

	private SyntaxNode nodeFromRequest(final String syntaxId, final String nodeId, final PutNodeRequest request)
	{
		return modelFactory.node(nodePath(syntaxId, nodeId), nodeId, request.type(), request.children());
	}

	private String nodePath(final String syntaxId, final String nodeId)
	{
		return syntaxPath(syntaxId) + "/" + nodeId;
	}

	@Override
	public synchronized void deleteNode(final String syntaxId, final String nodeId) throws NotFoundException
	{
		if (!findNode(syntaxId, nodeId).isPresent())
		{
			throw new NotFoundException(nodeId);
		}

		final Syntax newSyntax = syntax.get(syntaxId).withoutNode(nodeId);
		syntax.put(syntaxId, newSyntax);
	}

}
