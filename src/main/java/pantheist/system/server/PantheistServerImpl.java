package pantheist.system.server;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.google.common.base.Throwables;

import pantheist.api.syntax.resource.SyntaxResource;
import pantheist.common.util.MutableOptional;
import pantheist.system.config.PantheistConfig;
import pantheist.system.statics.resource.StaticsResource;

final class PantheistServerImpl implements PantheistServer
{
	private static final Logger LOGGER = LogManager.getLogger(PantheistServerImpl.class);
	private final PantheistConfig config;
	private final StaticsResource staticsHandler;
	private final SyntaxResource syntaxHandler;

	// State
	MutableOptional<Server> serverOpt;

	@Inject
	PantheistServerImpl(final PantheistConfig config, final StaticsResource staticsHandler,
			final SyntaxResource syntaxHandler)
	{
		this.config = checkNotNull(config);
		this.staticsHandler = checkNotNull(staticsHandler);
		this.syntaxHandler = checkNotNull(syntaxHandler);
		this.serverOpt = MutableOptional.empty();
	}

	@Override
	public void start()
	{
		try
		{
			final int port = config.httpPort();

			final ServletContextHandler context = new ServletContextHandler();
			context.setContextPath("/");

			final Server server = new Server(port);
			serverOpt.add(server);
			server.setHandler(context);

			final ResourceConfig resourceConfig = new ResourceConfig();
			resourceConfig.register(staticsHandler).register(syntaxHandler);

			context.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

			server.start();

			LOGGER.info("Running http server on localhost:{}", port);
		}
		catch (final Exception e)
		{
			throw new StartupException(e);
		}
	}

	@Override
	public void close()
	{
		if (serverOpt.isPresent())
		{
			try
			{
				serverOpt.get().stop();
			}
			catch (final Exception e)
			{
				Throwables.propagate(e);
			}
		}
		serverOpt.clear();
	}

}
