package pantheist.system.server;

import java.io.IOException;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.sun.net.httpserver.HttpServer;

public class SystemServerModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(PantheistServer.class);
		bind(PantheistHandler.class).to(PantheistHandlerImpl.class).in(Scopes.SINGLETON);
		bind(PantheistServer.class).to(PantheistServerImpl.class).in(Scopes.SINGLETON);
	}

	@Provides
	HttpServer provideHttpServer() throws IOException
	{
		return HttpServer.create();
	}
}
