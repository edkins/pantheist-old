package pantheist.api.generic.store;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ApiGenericStoreModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(ResourceStore.class);
		bind(ResourceStore.class).to(ResourceStoreImpl.class).in(Scopes.SINGLETON);
		install(new FactoryModuleBuilder()
				.implement(ResourceStoreSession.class, ResourceStoreSessionImpl.class)
				.build(ResourceStoreFactory.class));
	}

}
