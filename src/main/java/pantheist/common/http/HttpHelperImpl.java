package pantheist.common.http;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;

import pantheist.common.except.ExceptionWritingException;

class HttpHelperImpl implements HttpHelper
{
	private static final Logger LOGGER = LogManager.getLogger(HttpHelperImpl.class);

	@Override
	public void notFound(final HttpExchange exchange)
	{
		try
		{
			LOGGER.debug("Serving 404 response");
			exchange.sendResponseHeaders(404, 0);
			exchange.getResponseBody().write("Not found".getBytes());
		}
		catch (final IOException ex)
		{
			LOGGER.warn("Exception when serving 404 response, will not get handled");
			throw new ExceptionWritingException(ex);
		}
		finally
		{
			exchange.close();
		}
	}

	@Override
	public void methodNotAllowed(final HttpExchange exchange)
	{
		try
		{
			LOGGER.debug("Serving 405 response");
			exchange.sendResponseHeaders(405, 0);
			exchange.getResponseBody().write("Method not allowed".getBytes());
		}
		catch (final IOException ex)
		{
			LOGGER.warn("Exception when serving 405 response, will not get handled");
			throw new ExceptionWritingException(ex);
		}
		finally
		{
			exchange.close();
		}
	}

	@Override
	public void internalError(final HttpExchange exchange)
	{
		try
		{
			LOGGER.debug("Serving 500 response");
			exchange.sendResponseHeaders(500, 0);
			exchange.getResponseBody().write("Internal server error".getBytes());
		}
		catch (final IOException ex)
		{
			LOGGER.warn("Exception when serving 500 response, will not get handled");
			throw new ExceptionWritingException(ex);
		}
		finally
		{
			exchange.close();
		}
	}

	@Override
	public OutputStream beginOk(final HttpExchange exchange, final String contentType) throws IOException
	{
		exchange.getResponseHeaders().add("Content-Type", contentType);
		exchange.sendResponseHeaders(200, 0);
		return exchange.getResponseBody();
	}

}
