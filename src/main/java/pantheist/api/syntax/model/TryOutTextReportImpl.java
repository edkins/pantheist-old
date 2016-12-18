package pantheist.api.syntax.model;

import javax.annotation.Nullable;
import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class TryOutTextReportImpl implements TryOutTextReport
{
	private final String whatHappened;

	@Inject
	TryOutTextReportImpl(@Nullable @Assisted("whatHappened") @JsonProperty("whatHappened") final String whatHappened)
	{
		this.whatHappened = whatHappened;
	}

	@Override
	public String whatHappened()
	{
		return whatHappened;
	}

}
