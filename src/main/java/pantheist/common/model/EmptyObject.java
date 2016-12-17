package pantheist.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Represents an empty json object
 */
@JsonDeserialize(as = EmptyObjectImpl.class)
@JsonFormat(shape = Shape.OBJECT)
public interface EmptyObject
{

}
