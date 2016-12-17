package pantheist.system.server;

import com.sun.net.httpserver.HttpHandler;

/**
 * Tagging interface to indicate that this HttpHandler is supposed to implement
 * the pantheist API.
 */
interface PantheistHandler extends HttpHandler
{

}
