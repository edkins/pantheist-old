package pantheist.api.syntax.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = PutComponentRequestImpl.class)
public interface PutComponentRequest<T>
{
	/**
	 * @return whether this request is supposed to update an existing node.
	 */
	boolean updateExisting();

	/**
	 * The data associated with this put request. The type will be different
	 * depending on the component type.
	 *
	 * @return
	 */
	T data();

}
