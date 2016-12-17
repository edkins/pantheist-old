package pantheist.system.server;

public interface PantheistServer
{
	/**
	 * Start the server running on the configured port.
	 *
	 * @throws StartupException
	 */
	void start();
}
