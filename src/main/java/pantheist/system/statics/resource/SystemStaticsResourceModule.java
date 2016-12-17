package pantheist.system.statics.resource;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class SystemStaticsResourceModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(StaticsResource.class);
		bind(StaticsResource.class).to(StaticsResourceImpl.class).in(Scopes.SINGLETON);
	}

}
