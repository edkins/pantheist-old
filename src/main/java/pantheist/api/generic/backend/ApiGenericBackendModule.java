package pantheist.api.generic.backend;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class ApiGenericBackendModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(GenericBackend.class);
		bind(GenericBackend.class).to(GenericBackendImpl.class).in(Scopes.SINGLETON);
	}

}
