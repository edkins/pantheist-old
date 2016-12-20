package pantheist.testhelpers.ui.generic;

/**
 * Extra weird operations on ElementFinder that are only useful inside this
 * package.
 */
interface ExtendedElementFinder extends ElementFinder<CssPath>
{
	/**
	 * Apply the given tweak.
	 *
	 * @param tweaks
	 *            desired tweaks
	 * @return element finder
	 * @throws IllegalStateException
	 *             if tweaks is already set.
	 */
	ExtendedElementFinder tweak(Tweaks tweaks);

	/**
	 * Find an element by any html attribute.
	 *
	 * It won't check that the attribute is something that really exists in the
	 * html standard.
	 *
	 * @param key
	 *            attribute, e.g. href
	 * @param value
	 *            attribute value (may be empty but not null)
	 * @return interface to access this element
	 * @throws IllegalArgumentException
	 *             if key contains problematic characters or is empty
	 */
	ExtendedElementFinder withAttrib(String key, String value);
}
