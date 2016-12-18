package pantheist.api.syntax.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface TryOutTextReport
{
	@JsonProperty("whatHappened")
	String whatHappened();
}
