package pantheist.api.syntax.resource;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class ApiSyntaxResourceModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(SyntaxResource.class);
		bind(SyntaxResource.class).to(SyntaxResourceImpl.class).in(Scopes.SINGLETON);
	}

}
