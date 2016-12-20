package pantheist.testhelpers.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import javax.inject.Inject;

import com.netflix.config.DynamicPropertyFactory;

import pantheist.system.config.PantheistConfigImpl;
import pantheist.testhelpers.session.TestSession;

final class OverrideConfig extends PantheistConfigImpl
{
	private final TestSession session;

	@Inject
	OverrideConfig(final DynamicPropertyFactory propertyFactory, final TestSession session)
	{
		super(propertyFactory);
		this.session = checkNotNull(session);
	}

	@Override
	public int httpPort()
	{
		return session.pantheistPort();
	}

	@Override
	public File dataPath()
	{
		return session.dataDir();
	}
}
