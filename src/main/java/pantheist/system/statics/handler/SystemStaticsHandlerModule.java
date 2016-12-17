package pantheist.system.statics.handler;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class SystemStaticsHandlerModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(RootHandler.class);
		expose(StaticsHandler.class);
		bind(StaticsHandler.class).to(StaticsHandlerImpl.class).in(Scopes.SINGLETON);
		bind(RootHandler.class).to(RootHandlerImpl.class).in(Scopes.SINGLETON);
	}

}
