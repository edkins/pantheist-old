package pantheist.api.syntax.model;

import com.google.inject.PrivateModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ApiSyntaxModelModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(SyntaxModelFactory.class);
		install(new FactoryModuleBuilder()
				.implement(ListResourceResponse.class, ListResourceResponseImpl.class)
				.implement(ListComponentResponse.class, ListComponentResponseImpl.class)
				.implement(ListedComponent.class, ListedComponentImpl.class)
				.implement(ListTokenResponse.class, ListTokenResponseImpl.class)
				.implement(ResourceMetadata.class, ResourceMetadataImpl.class)
				.implement(SyntaxNode.class, SyntaxNodeImpl.class)
				.implement(SyntaxToken.class, SyntaxTokenImpl.class)
				.implement(Syntax.class, SyntaxImpl.class)
				.build(SyntaxModelFactory.class));
	}

}
