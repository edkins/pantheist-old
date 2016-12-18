package pantheist.api.generic.store;

import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import com.google.common.base.Objects;

final class ResourceIdWithType
{
	String resourceId;

	String resourceType;

	ResourceIdWithType(final String resourceType, final String resourceId)
	{
		this.resourceType = checkNotNullOrEmpty(resourceType);
		this.resourceId = checkNotNullOrEmpty(resourceId);
	}

	String sanitizedResourceId()
	{
		checkNotNullOrEmpty(resourceId);
		return resourceId.replace("/", "_");
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(resourceId, resourceType);
	}

	@Override
	public boolean equals(final Object object)
	{
		if (object instanceof ResourceIdWithType)
		{
			final ResourceIdWithType that = (ResourceIdWithType) object;
			return Objects.equal(this.resourceId, that.resourceId)
					&& Objects.equal(this.resourceType, that.resourceType);
		}
		return false;
	}

}
