package pantheist.system.config;

public interface PantheistConfig
{
	/**
	 * @return the port that the pantheist server runs on. Default 3142
	 */
	int httpPort();

	/**
	 * @return the maximum permitted number of connections in the backlog
	 */
	int httpBacklog();
}
