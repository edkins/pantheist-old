package pantheist.common.http;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.WillClose;
import javax.annotation.WillNotClose;

import com.sun.net.httpserver.HttpExchange;

import pantheist.common.except.ExceptionWritingException;

public interface HttpHelper
{
	/**
	 * Begin an http 200 response. The specified content type is written, and an
	 * output stream is returned. The HttpExchange must be closed after all the
	 * data is written.
	 *
	 * This method does not take an output size, which means chunked encoding
	 * will be used.
	 *
	 * @param exchange
	 *            http session
	 * @param contentType
	 *            Content type, e.g. text/html
	 * @return Output stream to write the data to
	 * @throws IOException
	 *             if there is an error writing the response headers
	 */
	OutputStream beginOk(@WillNotClose HttpExchange exchange, String contentType) throws IOException;

	/**
	 * Write an http 404 not found response.
	 *
	 * @param exchange
	 *            http session
	 * @throws ExceptionWritingException
	 *             if something went wrong (if this happens it will be too late
	 *             to try and write another http error response)
	 */
	void notFound(@WillClose HttpExchange exchange);

	/**
	 * Write an http 405 method not allowed response. Used when someone calls
	 * POST when they should have called GET, etc.
	 *
	 * @param exchange
	 *            http session
	 * @throws ExceptionWritingException
	 *             if something went wrong (if this happens it will be too late
	 *             to try and write another http error response)
	 */
	void methodNotAllowed(@WillClose HttpExchange exchange);

	/**
	 * Write an http 500 internal server error response. Generally used for
	 * unexpected errors.
	 *
	 * @param exchange
	 *            http session
	 * @throws ExceptionWritingException
	 *             if something went wrong (if this happens it will be too late
	 *             to try and write another http error response)
	 */
	void internalError(@WillClose HttpExchange exchange);
}
