package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ListComponentResponseImpl.class)
public interface ListComponentResponse
{
	/**
	 * @return a list of components together with their id and path.
	 */
	@JsonProperty("components")
	List<ListedComponent> components();
}
