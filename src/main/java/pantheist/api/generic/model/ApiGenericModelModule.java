package pantheist.api.generic.model;

import com.google.inject.PrivateModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ApiGenericModelModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(ApiGenericModelFactory.class);
		install(new FactoryModuleBuilder()
				.implement(ListResourceResponse.class, ListResourceResponseImpl.class)
				.implement(ListComponentResponse.class, ListComponentResponseImpl.class)
				.implement(ListedComponent.class, ListedComponentImpl.class)
				.implement(ResourceMetadata.class, ResourceMetadataImpl.class)
				.build(ApiGenericModelFactory.class));

	}

}
