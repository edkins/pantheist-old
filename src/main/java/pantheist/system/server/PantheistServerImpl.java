package pantheist.system.server;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpServer;

import pantheist.api.syntax.handler.SyntaxHandler;
import pantheist.system.config.PantheistConfig;
import pantheist.system.statics.handler.RootHandler;
import pantheist.system.statics.handler.StaticsHandler;

final class PantheistServerImpl implements PantheistServer
{
	private static final Logger LOGGER = LogManager.getLogger(PantheistServerImpl.class);
	private final HttpServer httpServer;
	private final PantheistConfig config;
	private final StaticsHandler staticsHandler;
	private final SyntaxHandler syntaxHandler;
	private final RootHandler rootHandler;

	@Inject
	PantheistServerImpl(final HttpServer httpServer, final PantheistConfig config, final RootHandler rootHandler,
			final StaticsHandler staticsHandler, final SyntaxHandler syntaxHandler)
	{
		this.httpServer = checkNotNull(httpServer);
		this.config = checkNotNull(config);
		this.rootHandler = checkNotNull(rootHandler);
		this.staticsHandler = checkNotNull(staticsHandler);
		this.syntaxHandler = checkNotNull(syntaxHandler);
	}

	@Override
	public void start()
	{
		try
		{
			final int port = config.httpPort();
			httpServer.bind(new InetSocketAddress("localhost", port), config.httpBacklog());
			httpServer.createContext("/static/", staticsHandler);
			httpServer.createContext("/syntax/", syntaxHandler);
			httpServer.createContext("/", rootHandler);
			httpServer.start();
			LOGGER.info("Running http server on localhost:{}", port);
		}
		catch (final IOException e)
		{
			throw new StartupException(e);
		}
	}

}
