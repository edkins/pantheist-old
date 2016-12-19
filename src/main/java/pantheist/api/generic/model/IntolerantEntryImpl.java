package pantheist.api.generic.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.Map.Entry;

class IntolerantEntryImpl implements Entry<String, Object>
{
	private final String key;
	private final Object value;

	IntolerantEntryImpl(final String key, final Object value)
	{
		this.key = checkNotNullOrEmpty(key);
		this.value = checkNotNull(value);
	}

	@Override
	public String getKey()
	{
		return key;
	}

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public Object setValue(final Object value)
	{
		throw new UnsupportedOperationException();
	}

}