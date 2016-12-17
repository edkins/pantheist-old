package pantheist.system.statics.backend;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class SystemStaticsBackendModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(StaticsBackend.class);
		bind(StaticsBackend.class).to(StaticsBackendImpl.class).in(Scopes.SINGLETON);
	}

}
