package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

final class PutComponentRequestImpl<T> implements PutComponentRequest<T>
{
	private final boolean updateExisting;
	private final T data;

	PutComponentRequestImpl(@JsonProperty("updateExisting") final boolean updateExisting,
			@JsonProperty("data") final T data)
	{
		this.updateExisting = updateExisting;
		this.data = checkNotNull(data);
	}

	@Override
	public boolean updateExisting()
	{
		return updateExisting;
	}

	@Override
	public T data()
	{
		return data;
	}

}
