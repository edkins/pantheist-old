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
import com.google.common.collect.Lists;

import pantheist.api.generic.model.ListResourceResponse;
import pantheist.api.generic.model.ResourceMetadata;
import pantheist.common.util.Escapers;
import pantheist.testhelpers.actions.interf.PantheistActions;

public class PantheistActionsApi implements PantheistActions
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

	public static PantheistActions from(final URL baseUrl)
	{
		try
		{
			return new PantheistActionsApi(ClientBuilder.newClient(), baseUrl.toURI(), new ObjectMapper());
		}
		catch (final URISyntaxException e)
		{
			throw Throwables.propagate(e);
		}
	}

	private WebTarget target(final String resourceType)
	{
		return client.target(baseUri).path(resourceType);
	}

	private WebTarget target(final String resourceType, final String resourceId)
	{
		return target(resourceType).path(Escapers.url(resourceId));
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

}
