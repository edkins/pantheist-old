package pantheist.system.statics.handler;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;

import pantheist.common.except.NotFoundException;
import pantheist.common.http.HttpHelper;
import pantheist.system.statics.backend.InputStreamWithType;
import pantheist.system.statics.backend.StaticsBackend;

final class StaticsHandlerImpl implements StaticsHandler
{
	private static final Logger LOGGER = LogManager.getLogger(StaticsHandlerImpl.class);
	private final StaticsBackend backend;
	private final HttpHelper httpHelper;

	@Inject
	StaticsHandlerImpl(final StaticsBackend backend, final HttpHelper httpHelper)
	{
		this.backend = checkNotNull(backend);
		this.httpHelper = checkNotNull(httpHelper);
	}

	@Override
	public void handle(final HttpExchange exchange)
	{
		try
		{
			LOGGER.debug("static {} {}", exchange.getRequestMethod(), exchange.getRequestURI().getPath());
			if (exchange.getRequestMethod().equals("GET"))
			{
				try (final InputStreamWithType input = this.backend.serveStaticFile(exchange.getRequestURI().getPath()))
				{
					final OutputStream output = this.httpHelper.beginOk(exchange, input.contentType());
					IOUtils.copyLarge(input.input(), output);
					exchange.close();
				}
			}
			else
			{
				this.httpHelper.methodNotAllowed(exchange);
			}
		}
		catch (final NotFoundException e)
		{
			LOGGER.catching(e);
			this.httpHelper.notFound(exchange);
		}
		catch (final IOException | RuntimeException e)
		{
			LOGGER.catching(e);
			this.httpHelper.internalError(exchange);
		}
	}

}
