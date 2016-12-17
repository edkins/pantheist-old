package pantheist.system.statics.backend;

import java.io.InputStream;
import java.util.Optional;

/**
 * Serves static files
 */
public interface StaticsBackend
{
	/**
	 * Serve the file identified by path.
	 *
	 * The calling method must close the returned InputStreamWithType.
	 *
	 * @param path
	 *            Path of the form /static/file.html
	 * @return InputStream and content type representing the resource, or
	 *         Optional.empty() if not found.
	 */
	Optional<InputStream> serveStaticFile(String path);
}
