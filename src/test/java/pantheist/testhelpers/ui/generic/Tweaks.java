package pantheist.testhelpers.ui.generic;

/**
 * Represents tweaks for certain elements that don't behave quite as expected.
 *
 * - input buttons and input text: text is stored in value attribute
 */
enum Tweaks
{
	DEFAULT, INPUT_BUTTON, INPUT_TEXT;

	public boolean isDefault()
	{
		return equals(DEFAULT);
	}

	public boolean cannotEnterNewLines()
	{
		return equals(INPUT_TEXT);
	}

	public boolean textIsValueAttribute()
	{
		return equals(INPUT_BUTTON) || equals(INPUT_TEXT);
	}
}
