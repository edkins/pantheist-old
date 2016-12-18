package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.base.Throwables;

import pantheist.api.syntax.model.ListComponentResponse;
import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.api.syntax.model.ListedComponent;
import pantheist.api.syntax.model.PutComponentRequest;
import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxMetadata;
import pantheist.api.syntax.model.SyntaxModelFactory;
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
		return modelFactory.syntaxMetadata(syn.path(), syn.id(), syn.name());
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
		return modelFactory.syntax(syntaxPath(id), id, id, ComponentType.emptyMap(() -> new TreeMap<>()));
	}

	private static String urlEncode(final String thing)
	{
		try
		{
			return URLEncoder.encode(thing, "utf-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			throw Throwables.propagate(e);
		}
	}

	private String syntaxPath(final String id)
	{
		return "/syntax/" + urlEncode(id);
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
	public synchronized ListComponentResponse listComponents(final String syntaxId, final ComponentType componentType)
			throws NotFoundException
	{
		if (!syntax.containsKey(syntaxId))
		{
			throw new NotFoundException(syntaxId);
		}

		final List<ListedComponent> nodes = syntax
				.get(syntaxId)
				.components()
				.get(componentType)
				.entrySet()
				.stream()
				.map(e -> toListedComponent(syntaxId, componentType, e))
				.collect(Collectors.toList());
		return modelFactory.listComponentResponse(nodes);
	}

	private ListedComponent toListedComponent(final String syntaxId,
			final ComponentType componentType,
			final Entry<String, Object> entry)
	{
		final String componentId = entry.getKey();
		final Object data = entry.getValue();
		final String path = componentPath(syntaxId, componentType, componentId);
		return modelFactory.listedComponent(path, componentId, data);
	}

	@Override
	public synchronized Object getComponent(final String syntaxId,
			final ComponentType componentType,
			final String componentId) throws NotFoundException
	{
		final Optional<Object> result = findComponent(syntaxId, componentType, componentId);
		if (!result.isPresent())
		{
			throw new NotFoundException(componentId);
		}
		return result.get();
	}

	private Optional<Object> findComponent(final String syntaxId,
			final ComponentType componentType,
			final String componentId) throws NotFoundException
	{
		if (!syntax.containsKey(syntaxId))
		{
			throw new NotFoundException(syntaxId);
		}
		return Optional.ofNullable(syntax.get(syntaxId).components().get(componentType).get(componentId));
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
	public synchronized <T> void putComponent(final String syntaxId,
			final ComponentType componentType,
			final String componentId,
			final PutComponentRequest<T> request)
			throws NotFoundException, AlreadyPresentException
	{
		checkPresence(findComponent(syntaxId, componentType, componentId), request.updateExisting(), componentId);

		final Syntax newSyntax = syntax.get(syntaxId).withComponent(componentType, componentId, request.data());
		syntax.put(syntaxId, newSyntax);
	}

	private String componentPath(final String syntaxId, final ComponentType componentType, final String componentId)
	{
		return syntaxPath(syntaxId) + "/" + componentType.toString() + "/" + urlEncode(componentId);
	}

	@Override
	public synchronized void deleteComponent(final String syntaxId,
			final ComponentType componentType,
			final String componentId) throws NotFoundException
	{
		if (!findComponent(syntaxId, componentType, componentId).isPresent())
		{
			throw new NotFoundException(componentId);
		}

		final Syntax newSyntax = syntax.get(syntaxId).withoutComponent(componentType, componentId);
		syntax.put(syntaxId, newSyntax);
	}

}
