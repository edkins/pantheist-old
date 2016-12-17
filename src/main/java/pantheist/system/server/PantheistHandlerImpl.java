package pantheist.system.server;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;

final class PantheistHandlerImpl implements PantheistHandler
{
	private static final Logger LOGGER = LogManager.getLogger(PantheistHandlerImpl.class);

	@Inject
	PantheistHandlerImpl()
	{

	}

	@Override
	public void handle(final HttpExchange exchange) throws IOException
	{
		LOGGER.warn("Someone asked us to do something");
	}

}
