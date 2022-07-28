package lu.svv.theodore.explain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;
import java.io.StreamCorruptedException;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstotter</a>
 * @version 4.1
 * @since 4.1
 */
final class SerialProxy implements Externalizable {

	@Serial
	private static final long serialVersionUID = 1L;

	static final byte HLS_EXPR = 2;
	static final byte CONST = 3;
	static final byte EPHEMERAL_CONST = 4;

	/**
	 * The type being serialized.
	 */
	private byte _type;

	/**
	 * The object being serialized.
	 */
	private Object _object;

	/**
	 * Constructor for deserialization.
	 */
	public SerialProxy() {
	}

	/**
	 * Creates an instance for serialization.
	 *
	 * @param type  the type
	 * @param object  the object
	 */
	SerialProxy(final byte type, final Object object) {
		_type = type;
		_object = object;
	}

	@Override
	public void writeExternal(final ObjectOutput out) throws IOException {
		out.writeByte(_type);
		switch (_type) {
			case HLS_EXPR -> ((HlsExpr)_object).write(out);
			case CONST -> ((Const<?>)_object).write(out);
			case EPHEMERAL_CONST -> ((EphemeralConst<?>)_object).write(out);
			default -> throw new StreamCorruptedException("Unknown serialized type.");
		}
	}

	@Override
	public void readExternal(final ObjectInput in)
		throws IOException, ClassNotFoundException
	{
		_type = in.readByte();
		_object = switch (_type) {
			case HLS_EXPR -> HlsExpr.read(in);
			case CONST -> Const.read(in);
			case EPHEMERAL_CONST -> EphemeralConst.read(in);
			default -> throw new StreamCorruptedException("Unknown serialized type.");
		};
	}

	@Serial
	private Object readResolve() {
		return _object;
	}

}