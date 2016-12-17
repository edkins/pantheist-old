package pantheist.system.statics.backend;

import java.io.InputStream;

import pantheist.common.except.NotFoundException;

final class StaticsBackendImpl implements StaticsBackend
{

	@Override
	public InputStreamWithType serveStaticFile(final String path) throws NotFoundException
	{
		if (!path.startsWith("/static/"))
		{
			throw new NotFoundException("path must start /static/");
		}
		if (path.contains(".."))
		{
			throw new NotFoundException(".. not allowed in path");
		}

		final InputStream result = StaticsBackendImpl.class.getResourceAsStream(path);
		if (result == null)
		{
			throw new NotFoundException("no such resource");
		}
		final String contentType = guessContentType(path);
		return new InputStreamWithTypeImpl(result, contentType);
	}

	private String guessContentType(final String path) throws NotFoundException
	{
		final int dotIndex = path.lastIndexOf('.');
		final int slashIndex = path.lastIndexOf('/');
		if (dotIndex == -1 || dotIndex <= slashIndex + 1)
		{
			throw new NotFoundException("No extension in filename");
		}
		final String ext = path.substring(dotIndex + 1);
		switch (ext) {
		case "html":
			return "text/html";
		default:
			throw new NotFoundException("Unknown file extension");
		}
	}
}
