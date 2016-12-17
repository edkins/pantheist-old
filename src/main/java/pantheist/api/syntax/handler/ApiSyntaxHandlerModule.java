package pantheist.api.syntax.handler;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class ApiSyntaxHandlerModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(SyntaxHandler.class);
		bind(SyntaxHandler.class).to(SyntaxHandlerImpl.class).in(Scopes.SINGLETON);
	}

}
