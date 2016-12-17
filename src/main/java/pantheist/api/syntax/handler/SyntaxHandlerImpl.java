package pantheist.api.syntax.handler;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;

final class SyntaxHandlerImpl implements SyntaxHandler
{
	private static final Logger LOGGER = LogManager.getLogger(SyntaxHandlerImpl.class);

	@Override
	public void handle(final HttpExchange exchange) throws IOException
	{
		LOGGER.warn("Someone asked us to do a syntax thing");
		exchange.close();
	}

}
