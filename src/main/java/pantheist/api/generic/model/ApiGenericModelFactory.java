package pantheist.api.generic.model;

import java.util.Collection;
import java.util.List;

import com.google.inject.assistedinject.Assisted;

public interface ApiGenericModelFactory
{
	ListResourceResponse listResourceResponse(List<ResourceMetadata> resources);

	ResourceMetadata resourceMetadata(@Assisted("path") String path,
			@Assisted("id") String id,
			@Assisted("name") String name);

	ListComponentResponse listComponentResponse(Collection<ListedComponent> components);

	ListedComponent listedComponent(@Assisted("path") String path,
			@Assisted("id") String id,
			@Assisted("data") Object data);

}
