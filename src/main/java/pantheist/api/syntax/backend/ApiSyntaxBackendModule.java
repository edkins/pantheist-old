package pantheist.api.syntax.backend;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class ApiSyntaxBackendModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(SyntaxBackend.class);
		bind(SyntaxBackend.class).to(SyntaxBackendImpl.class).in(Scopes.SINGLETON);
	}

}
