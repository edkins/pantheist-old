package pantheist.engine.parser;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class EngineParserModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(ParserEngine.class);
		bind(ParserEngine.class).to(ParserEngineImpl.class).in(Scopes.SINGLETON);
	}

}
