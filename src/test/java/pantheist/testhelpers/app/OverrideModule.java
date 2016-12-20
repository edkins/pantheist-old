package pantheist.testhelpers.app;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.netflix.config.DynamicPropertyFactory;

import pantheist.system.config.PantheistConfig;
import pantheist.testhelpers.session.TestSession;

class OverrideModule extends PrivateModule
{
	private final TestSession session;

	OverrideModule(final TestSession session)
	{
		this.session = checkNotNull(session);
	}

	@Override
	protected void configure()
	{
		expose(PantheistConfig.class);
		bind(PantheistConfig.class).to(OverrideConfig.class).in(Scopes.SINGLETON);
		bind(TestSession.class).toInstance(session);
	}

	@Provides
	DynamicPropertyFactory provideDynamicPropertyFactory()
	{
		return DynamicPropertyFactory.getInstance();
	}

}
