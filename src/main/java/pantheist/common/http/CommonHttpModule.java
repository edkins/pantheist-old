package pantheist.common.http;

import com.google.inject.PrivateModule;
import com.google.inject.Scopes;

public class CommonHttpModule extends PrivateModule
{

	@Override
	protected void configure()
	{
		expose(HttpHelper.class);
		bind(HttpHelper.class).to(HttpHelperImpl.class).in(Scopes.SINGLETON);
	}

}
