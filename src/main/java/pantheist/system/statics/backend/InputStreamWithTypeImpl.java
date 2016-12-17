package pantheist.system.statics.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;

final class InputStreamWithTypeImpl implements InputStreamWithType
{
	private final InputStream inputStream;
	private final String contentType;

	InputStreamWithTypeImpl(final InputStream inputStream, final String contentType)
	{
		this.inputStream = checkNotNull(inputStream);
		this.contentType = checkNotNull(contentType);
	}

	@Override
	public void close() throws IOException
	{
		this.inputStream.close();
	}

	@Override
	public InputStream input()
	{
		return this.inputStream;
	}

	@Override
	public String contentType()
	{
		return this.contentType;
	}

}
