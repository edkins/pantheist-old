package pantheist.system.config;

import java.io.File;

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

	/**
	 * @return the path where all the data is stored.
	 */
	File dataPath();
}
