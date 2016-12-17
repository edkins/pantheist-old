package pantheist.system.statics.backend;

import pantheist.common.except.NotFoundException;

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
	 * @return InputStream and content type representing the resource.
	 * @throws NotFoundException
	 *             if the path is invalid or no resource is found with that
	 *             name.
	 */
	InputStreamWithType serveStaticFile(String path) throws NotFoundException;
}
