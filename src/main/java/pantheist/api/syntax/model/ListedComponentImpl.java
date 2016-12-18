package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class ListedComponentImpl implements ListedComponent
{
	private final String path;
	private final String id;
	private final Object data;

	@Inject
	ListedComponentImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("id") @JsonProperty("id") final String id,
			@Assisted("data") @JsonProperty("data") final Object data)
	{
		this.path = checkNotNullOrEmpty(path);
		this.id = checkNotNullOrEmpty(id);
		this.data = checkNotNull(data);
	}

	@Override
	public String path()
	{
		return path;
	}

	@Override
	public String id()
	{
		return id;
	}

	@Override
	public Object data()
	{
		return data;
	}

}
