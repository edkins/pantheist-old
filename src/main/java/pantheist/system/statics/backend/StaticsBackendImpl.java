package pantheist.system.statics.backend;

import java.io.InputStream;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

final class StaticsBackendImpl implements StaticsBackend
{
	private static final Logger LOGGER = LogManager.getLogger(StaticsBackendImpl.class);

	@Override
	public Optional<InputStream> serveStaticFile(final String path)
	{
		LOGGER.debug(path);
		if (!path.startsWith("/static/"))
		{
			return Optional.empty();
		}
		if (path.contains(".."))
		{
			return Optional.empty();
		}

		final InputStream result = StaticsBackendImpl.class.getResourceAsStream(path);
		if (result == null)
		{
			return Optional.empty();
		}
		return Optional.of(result);
	}

}
