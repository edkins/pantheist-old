package pantheist.common.http;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;

import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

public interface HttpHelper
{
	/**
	 * Return a 200 response with the given object converted to json.
	 *
	 * @param payload
	 *            Object with appropriate jackson annotations
	 * @return Response object
	 * @throws HttpHelperException
	 *             on json processing error
	 */
	Response jsonResponse(Object payload);

	/**
	 * Read the given string as json. It will cause a 400 Bad Request response
	 * if the request is invalid json syntax or cannot be deserialized to the
	 * given type.
	 *
	 * @param json
	 *            The json request data
	 * @param clazz
	 *            The class to deserialize to
	 * @return The request as a java object of the desired type
	 * @throws WebApplicationException
	 *             if unable to serialize.
	 */
	<T> T parseRequest(String json, Class<T> clazz);

	/**
	 * Parse a request given a type reference instead of a class.
	 *
	 * @param json
	 *            The json request data
	 * @param typeRef
	 *            Type reference identifying class to deserialize to
	 * @return The request as a java object of the desired type
	 * @throws WebApplicationException
	 *             if unable to serialize.
	 */
	<T> T parseRequest(String json, TypeReference<? extends T> typeRef);

	/**
	 * Throws the given exception as a WebApplicationException, with status 400
	 * indicating a bad request.
	 *
	 * This will call LOGGER.catching itself so you don't need to.
	 *
	 * @param ex
	 *            the exception caught from the backend that we wish to rethrow.
	 * @return Never actually returns. The return type is RuntimeException to
	 *         allow you to pretend to throw something to keep the compiler
	 *         happy.
	 */
	RuntimeException rethrow(AlreadyPresentException ex);

	/**
	 * Throws the given exception as a WebApplicationException, with status 404
	 * indicating the resource was not found.
	 *
	 * This will call LOGGER.catching itself so you don't need to.
	 *
	 * @param ex
	 *            the exception caught from the backend that we wish to rethrow.
	 * @return Never actually returns. The return type is RuntimeException to
	 *         allow you to pretend to throw something to keep the compiler
	 *         happy.
	 */
	RuntimeException rethrow(NotFoundException ex);
}
