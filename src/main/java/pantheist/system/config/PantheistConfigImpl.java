package pantheist.system.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import javax.inject.Inject;

import com.google.common.annotations.VisibleForTesting;
import com.netflix.config.DynamicPropertyFactory;

import pantheist.common.annotations.NotFinalForTesting;

@VisibleForTesting
@NotFinalForTesting
public class PantheistConfigImpl implements PantheistConfig
{
	private final DynamicPropertyFactory propertyFactory;

	@Inject
	@VisibleForTesting
	protected PantheistConfigImpl(final DynamicPropertyFactory propertyFactory)
	{
		this.propertyFactory = checkNotNull(propertyFactory);
	}

	@Override
	public int httpPort()
	{
		return propertyFactory.getIntProperty("PANTHEIST_HTTP_PORT", 3142).get();
	}

	@Override
	public int httpBacklog()
	{
		return propertyFactory.getIntProperty("PANTHEIST_HTTP_BACKLOG", 10).get();
	}

	@Override
	public File dataPath()
	{
		final String path = propertyFactory.getStringProperty("PANTHEIST_DATA_PATH", null).get();
		if (path == null)
		{
			return new File(System.getProperty("user.dir"), "data");
		}
		else
		{
			return new File(path);
		}
	}

}
