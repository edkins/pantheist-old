package pantheist.testhelpers.ui.generic;

/**
 * Represents check boxes and radio buttons. They can be clicked, and are in
 * either a checked or unchecked state.
 */
public interface Checkable extends Clickable
{
	boolean isChecked();
}
