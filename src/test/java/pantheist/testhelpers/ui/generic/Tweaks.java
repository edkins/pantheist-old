package pantheist.testhelpers.ui.generic;

/**
 * Represents tweaks for certain elements that don't behave quite as expected.
 *
 * - input buttons, input text and textarea: text is stored in value attribute
 *
 * - input text: cannot enter newline characters
 */
enum Tweaks
{
	DEFAULT, INPUT_BUTTON, INPUT_TEXT, TEXTAREA;

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
		return equals(INPUT_BUTTON) || equals(INPUT_TEXT) || equals(TEXTAREA);
	}
}
