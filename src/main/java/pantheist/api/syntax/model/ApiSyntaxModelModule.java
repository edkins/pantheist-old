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
				.implement(SyntaxNode.class, SyntaxNodeImpl.class)
				.implement(SyntaxToken.class, SyntaxTokenImpl.class)
				.implement(Syntax.class, SyntaxImpl.class)
				.build(SyntaxModelFactory.class));
	}

}
