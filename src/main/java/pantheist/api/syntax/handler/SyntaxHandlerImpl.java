package pantheist.api.syntax.handler;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import pantheist.api.syntax.backend.SyntaxBackend;
import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.common.http.HttpHelper;

final class SyntaxHandlerImpl implements SyntaxHandler
{
	private static final Logger LOGGER = LogManager.getLogger(SyntaxHandlerImpl.class);
	private final SyntaxBackend backend;
	private final HttpHelper httpHelper;
	private final ObjectMapper objectMapper;

	@Inject
	SyntaxHandlerImpl(final SyntaxBackend backend, final HttpHelper httpHelper, final ObjectMapper objectMapper)
	{
		this.backend = checkNotNull(backend);
		this.httpHelper = checkNotNull(httpHelper);
		this.objectMapper = checkNotNull(objectMapper);
	}

	@Override
	public void handle(final HttpExchange exchange) throws IOException
	{
		final String[] parts = exchange.getRequestURI().getPath().split("/");
		if (parts.length == 1)
		{
			handleRoot(exchange);
		}
		else
		{
			this.httpHelper.notFound(exchange);
		}
	}

	private void handleRoot(final HttpExchange exchange) throws IOException
	{
		final ListSyntaxResponse response = this.backend.listSyntax();
		final OutputStream output = this.httpHelper.beginOk(exchange, "application/json");
		this.objectMapper.writeValue(output, response);
		exchange.close();
	}

}
