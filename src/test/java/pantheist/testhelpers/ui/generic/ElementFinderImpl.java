package pantheist.testhelpers.ui.generic;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.regex.Pattern;

final class ElementFinderImpl implements ExtendedElementFinder
{
	private static final Pattern ELEMENT_TYPE_PATTERN = Pattern.compile("[a-z]+");
	private static final Pattern ID_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]*");
	private static final Pattern CLASS_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]*");
	private static final Pattern DATA_KEY_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]*");
	private static final Pattern ATTRIB_KEY_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_-]*");
	private static final Pattern ATTRIB_VALUE_PATTERN = Pattern.compile("[a-zA-Z0-9_ -]*");
	private final UiSession session;
	private final String path;
	private final String selector;
	private final Tweaks tweaks;

	private ElementFinderImpl(final UiSession session,
			final String path,
			final String selector,
			final Tweaks tweaks)
	{
		this.session = checkNotNull(session);
		this.path = checkNotNull(path);
		this.selector = checkNotNullOrEmpty(selector);
		this.tweaks = checkNotNull(tweaks);
	}

	static ExtendedElementFinder elementType(final UiSession session, final String path, final String elementType)
	{
		if (!ELEMENT_TYPE_PATTERN.matcher(elementType).matches())
		{
			throw new IllegalArgumentException("Bad element type: " + elementType);
		}
		return new ElementFinderImpl(session, path, elementType, Tweaks.DEFAULT);
	}

	private String fullPath()
	{
		if (path.isEmpty())
		{
			return selector;
		}
		else
		{
			return path + " " + selector;
		}
	}

	@Override
	public CssPath choose()
	{
		return CssPath.of(session, fullPath(), tweaks);
	}

	private ExtendedElementFinder with(final String suffix)
	{
		return new ElementFinderImpl(session, path, selector + suffix, tweaks);
	}

	@Override
	public ElementFinder<CssPath> withId(final String id)
	{
		checkNotNullOrEmpty(id);
		if (!ID_PATTERN.matcher(id).matches())
		{
			throw new IllegalArgumentException("Bad css id: " + id);
		}
		return with("#" + id);
	}

	@Override
	public ElementFinder<CssPath> withClass(final String cssClass)
	{
		checkNotNullOrEmpty(cssClass);
		if (!CLASS_PATTERN.matcher(cssClass).matches())
		{
			throw new IllegalArgumentException("Bad css class: " + cssClass);
		}
		return with("." + cssClass);
	}

	@Override
	public ExtendedElementFinder withAttrib(final String key, final String value)
	{
		if (!ATTRIB_KEY_PATTERN.matcher(key).matches())
		{
			throw new IllegalArgumentException("Bad attrib key: " + key);
		}
		if (!ATTRIB_VALUE_PATTERN.matcher(value).matches())
		{
			throw new IllegalArgumentException("Bad attrib value: " + value);
		}
		return with("[" + key + "=\"" + value + "\"]");
	}

	@Override
	public ElementFinder<CssPath> withData(final String key, final String value)
	{
		checkNotNullOrEmpty(key);
		checkNotNull(value);
		if (!DATA_KEY_PATTERN.matcher(key).matches())
		{
			throw new IllegalArgumentException("Bad data key: " + key);
		}
		return withAttrib("data-" + key, value);
	}

	@Override
	public int count()
	{
		return session.find(fullPath()).size();
	}

	@Override
	public ExtendedElementFinder tweak(final Tweaks newTweaks)
	{
		if (!tweaks.isDefault())
		{
			throw new IllegalStateException("Cannot combine tweaks");
		}
		return new ElementFinderImpl(session, path, selector, newTweaks);
	}

	@Override
	public ElementFinder<CssPath> withValue(final String value)
	{
		return withAttrib("value", value);
	}

}
