package pantheist.system.statics.backend;

import java.io.Closeable;
import java.io.InputStream;

/**
 * Represents an InputStream together with an http content type.
 */
public interface InputStreamWithType extends Closeable
{
	/**
	 * @return The input stream
	 */
	InputStream input();

	/**
	 *
	 * @return the content type, e.g. text/html
	 */
	String contentType();
}
