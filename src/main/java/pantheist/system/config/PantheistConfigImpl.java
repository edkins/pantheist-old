package pantheist.system.config;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.netflix.config.DynamicPropertyFactory;

final class PantheistConfigImpl implements PantheistConfig
{
	private final DynamicPropertyFactory propertyFactory;

	@Inject
	PantheistConfigImpl(final DynamicPropertyFactory propertyFactory)
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

}
