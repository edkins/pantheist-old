package pantheist.common.http;

import javax.ws.rs.core.Response;

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
}
