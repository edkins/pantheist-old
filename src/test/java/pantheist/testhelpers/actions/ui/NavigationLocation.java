package pantheist.testhelpers.actions.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

final class NavigationLocation
{
	private final NavigationLevel level;
	private final String resourceType;
	private final String resourceId;

	private NavigationLocation(final NavigationLevel level,
			final String resourceType,
			final String resourceId)
	{
		this.level = checkNotNull(level);
		this.resourceType = resourceType;
		this.resourceId = resourceId;
	}

	public static NavigationLocation root()
	{
		return new NavigationLocation(NavigationLevel.ROOT, null, null);
	}

	public static NavigationLocation resourceType(final String resourceType)
	{
		return new NavigationLocation(NavigationLevel.RESOURCE_TYPE, resourceType, null);
	}

	public static NavigationLocation resource(final String resourceType, final String resourceId)
	{
		return new NavigationLocation(NavigationLevel.RESOURCE, resourceType, resourceId);
	}

	public boolean canSeeSidebar()
	{
		return level.canSeeSidebar();
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(level, resourceType, resourceId);
	}

	@Override
	public boolean equals(final Object object)
	{
		if (object instanceof NavigationLocation)
		{
			final NavigationLocation that = (NavigationLocation) object;
			return Objects.equal(this.level, that.level)
					&& Objects.equal(this.resourceType, that.resourceType)
					&& Objects.equal(this.resourceId, that.resourceId);
		}
		return false;
	}
}
