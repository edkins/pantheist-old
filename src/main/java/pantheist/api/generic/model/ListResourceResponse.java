package pantheist.api.generic.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ListResourceResponseImpl.class)
public interface ListResourceResponse
{
	@JsonProperty("resources")
	List<ResourceMetadata> resources();
}
