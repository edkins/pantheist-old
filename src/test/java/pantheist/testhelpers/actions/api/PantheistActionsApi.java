package pantheist.testhelpers.actions.api;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import pantheist.api.generic.model.ListResourceResponse;
import pantheist.api.generic.model.ResourceMetadata;
import pantheist.common.util.Escapers;
import pantheist.testhelpers.actions.interf.PantheistActions;
import pantheist.testhelpers.actions.interf.SyntaxActions;
import pantheist.testhelpers.model.Information;
import pantheist.testhelpers.model.Informations;
import pantheist.testhelpers.model.JsonBuilder;

public class PantheistActionsApi implements PantheistActions, SyntaxActions
{
	private final Client client;
	private final URI baseUri;
	private final ObjectMapper objectMapper;

	private PantheistActionsApi(final Client client, final URI baseUri, final ObjectMapper objectMapper)
	{
		this.client = checkNotNull(client);
		this.baseUri = checkNotNull(baseUri);
		this.objectMapper = checkNotNull(objectMapper);
	}

	public static PantheistActions from(final URL baseUrl, final ObjectMapper objectMapper)
	{
		try
		{
			return new PantheistActionsApi(ClientBuilder.newClient(), baseUrl.toURI(), objectMapper);
		}
		catch (final URISyntaxException e)
		{
			throw Throwables.propagate(e);
		}
	}

	private JsonBuilder jb()
	{
		return JsonBuilder.from(objectMapper);
	}

	private WebTarget target(final String resourceType)
	{
		return client.target(baseUri).path(resourceType);
	}

	private WebTarget target(final String resourceType, final String resourceId)
	{
		final String escaped = Escapers.url(resourceId);
		return target(resourceType).path(escaped);
	}

	private WebTarget target(final String resourceType,
			final String resourceId,
			final String componentType,
			final String componentId)
	{
		return target(resourceType, resourceId).path(componentType).path(Escapers.url(componentId));
	}

	@Override
	public void createResource(final String resourceType, final String resourceId)
	{
		final Response response = target(resourceType, resourceId).request().put(Entity.json("{}"));
		expectNoContent(response);
	}

	private void expectNoContent(final Response response)
	{
		if (response.getStatus() != 204)
		{
			throw UnexpectedHttpStatusException.forResponse(response);
		}
	}

	private void expectNotFound(final Response response)
	{
		if (response.getStatus() != 404)
		{
			throw UnexpectedHttpStatusException.forResponse(response);
		}
	}

	private <T> T expectJson(final Response response, final Class<T> clazz)
	{
		if (response.getStatus() != 200)
		{
			throw UnexpectedHttpStatusException.forResponse(response);
		}
		final String json = response.readEntity(String.class);
		try
		{
			return objectMapper.readValue(json, clazz);
		}
		catch (final IOException e)
		{
			throw new ResponseParseException(json, e);
		}
	}

	private Information jsonInfo(final Response response)
	{
		if (response.getStatus() != 200)
		{
			throw UnexpectedHttpStatusException.forResponse(response);
		}
		final String json = response.readEntity(String.class);
		return Informations.jsonOrEmpty(objectMapper, json);
	}

	private Information jsonInfoOrEmpty(final Response response)
	{
		if (response.getStatus() == 404)
		{
			return Informations.empty();
		}
		else
		{
			return jsonInfo(response);
		}
	}

	@Override
	public void deleteResource(final String resourceType, final String resourceId)
	{
		final Response response = target(resourceType, resourceId).request().delete();
		expectNoContent(response);
	}

	@Override
	public void assertResourceExists(final String resourceType, final String resourceId)
	{
		checkNotNullOrEmpty(resourceId);
		assertThat(listResourceIdsOfType(resourceType), hasItem(resourceId));
	}

	@Override
	public void assertResourceDoesNotExist(final String resourceType, final String resourceId)
	{
		checkNotNullOrEmpty(resourceId);
		assertThat(listResourceIdsOfType(resourceType), not(hasItem(resourceId)));
	}

	@Override
	public List<String> listResourceIdsOfType(final String resourceType)
	{
		final Response response = target(resourceType).request().get();
		final List<ResourceMetadata> resources = expectJson(response, ListResourceResponse.class).resources();
		return Lists.transform(resources, ResourceMetadata::id);
	}

	@Override
	public SyntaxActions syntax()
	{
		return this;
	}

	@Override
	public void createLiteralToken(final String syntaxId, final String value)
	{
		final Entity<String> json = jb().with("type", "literal").with("value", value).toEntity();
		final Response response = target("syntax", syntaxId, "node", value).request().put(json);
		expectNoContent(response);
	}

	@Override
	public Information describeNode(final String syntaxId, final String nodeId)
	{
		final Response response = target("syntax", syntaxId, "node", nodeId).request().get();
		return jsonInfo(response);
	}

	@Override
	public void deleteNode(final String syntaxId, final String nodeId)
	{
		final Response response = target("syntax", syntaxId, "node", nodeId).request().delete();
		expectNoContent(response);
	}

	@Override
	public void assertNodeIsGone(final String syntaxId, final String nodeId)
	{
		final Response response = target("syntax", syntaxId, "node", nodeId).request().get();
		expectNotFound(response);
	}

	@Override
	public void createDocRoot(final String syntaxId, final String rootNodeId)
	{
		final Entity<String> json = jb().with("children", ImmutableList.of(rootNodeId)).toEntity();
		final Response response = target("syntax", syntaxId, "doc", "root").request().put(json);
		expectNoContent(response);
	}

	@Override
	public Information describeDocRoot(final String syntaxId)
	{
		final Response response = target("syntax", syntaxId, "doc", "root").request().get();
		return jsonInfoOrEmpty(response);
	}

	@Override
	public Information describeDocWhitespace(final String syntaxId)
	{
		final Response response = target("syntax", syntaxId, "doc", "whitespace").request().get();
		return jsonInfoOrEmpty(response);
	}

	@Override
	public void createDocWhitespace(final String syntaxId, final List<String> whitespaceNodeIds)
	{
		final Entity<String> json = jb().with("children", whitespaceNodeIds).toEntity();
		final Response response = target("syntax", syntaxId, "doc", "whitespace").request().put(json);
		expectNoContent(response);
	}

	@Override
	public void createRegexToken(final String syntaxId, final String nodeId, final String value)
	{
		final Entity<String> json = jb().with("type", "regex").with("value", value).toEntity();
		final Response response = target("syntax", syntaxId, "node", nodeId).request().put(json);
		expectNoContent(response);
	}

	@Override
	public void createZeroOrMoreNode(final String syntaxId, final String nodeId, final String child)
	{
		final Entity<String> json = jb()
				.with("type", "zero_or_more")
				.with("children", ImmutableList.of(child))
				.toEntity();
		final Response response = target("syntax", syntaxId, "node", nodeId).request().put(json);
		expectNoContent(response);
	}

	@Override
	public void createOneOrMoreNode(final String syntaxId, final String nodeId, final String child)
	{
		final Entity<String> json = jb()
				.with("type", "one_or_more")
				.with("children", ImmutableList.of(child))
				.toEntity();
		final Response response = target("syntax", syntaxId, "node", nodeId).request().put(json);
		expectNoContent(response);
	}

	@Override
	public void createSequenceNode(final String syntaxId, final String nodeId, final List<String> children)
	{
		final Entity<String> json = jb()
				.with("type", "sequence")
				.with("children", children)
				.toEntity();
		final Response response = target("syntax", syntaxId, "node", nodeId).request().put(json);
		expectNoContent(response);
	}

	@Override
	public void createChoiceNode(final String syntaxId, final String nodeId, final List<String> children)
	{
		final Entity<String> json = jb()
				.with("type", "choice")
				.with("children", children)
				.toEntity();
		final Response response = target("syntax", syntaxId, "node", nodeId).request().put(json);
		expectNoContent(response);
	}

	@Override
	public Information tryOutSyntax(final String syntaxId, final String document)
	{
		final Response response = target("syntax", syntaxId).path("try").request().post(Entity.text(document));
		return jsonInfo(response).field("whatHappened");
	}

}
