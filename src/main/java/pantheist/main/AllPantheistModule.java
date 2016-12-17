package pantheist.main;

import com.google.inject.PrivateModule;

import pantheist.api.syntax.backend.ApiSyntaxBackendModule;
import pantheist.api.syntax.handler.ApiSyntaxHandlerModule;
import pantheist.api.syntax.model.ApiSyntaxModelModule;
import pantheist.common.http.CommonHttpModule;
import pantheist.system.config.SystemConfigModule;
import pantheist.system.server.PantheistServer;
import pantheist.system.server.SystemServerModule;
import pantheist.system.statics.backend.SystemStaticsBackendModule;
import pantheist.system.statics.handler.SystemStaticsHandlerModule;

public class AllPantheistModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(PantheistServer.class);
		install(new ApiSyntaxBackendModule());
		install(new ApiSyntaxModelModule());
		install(new ApiSyntaxHandlerModule());
		install(new CommonHttpModule());
		install(new SystemConfigModule());
		install(new SystemStaticsBackendModule());
		install(new SystemStaticsHandlerModule());
		install(new SystemServerModule());
	}

}
