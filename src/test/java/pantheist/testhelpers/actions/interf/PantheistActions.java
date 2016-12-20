package pantheist.testhelpers.actions.interf;

import java.util.List;

/**
 * Represents a suite of actions available to tests.
 *
 * These can be implemented through either UI or API actions.
 */
public interface PantheistActions
{
	void createResource(String resourceType, String resourceId);

	void deleteResource(String resourceType, String resourceId);

	void assertResourceExists(String resourceType, String resourceId);

	void assertResourceDoesNotExist(String resourceType, String resourceId);

	List<String> listResourceIdsOfType(String resourceType);
}
