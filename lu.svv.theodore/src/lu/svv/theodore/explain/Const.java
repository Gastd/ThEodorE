package lu.svv.theodore.explain;

import static java.util.Objects.requireNonNull;
import static io.jenetics.internal.util.SerialIO.readNullableString;
import static io.jenetics.internal.util.SerialIO.writeNullableString;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


/**
 * Represents an operation which always returns the same, constant, value. To
 * improve readability, constants may have a name. If a name is given, this name
 * is used when printing the program tree. The {@code Const} operation is a
 * <em>terminal</em> operation.
 *
 * <pre>{@code
 * final static Op<Double> PI = Const.of("pi", Math.PI);
 * final static Op<Double> ONE = Const.of(1.0);
 * }</pre>
 *
 */
public final class Const<T> extends Val<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 2L;

	private final T _const;

	private Const(final String name, final T constant) {
		super(name);
		_const = constant;
	}

	@Override
	public T value() {
		return _const;
	}

	@Override
	public String toString() {
		return name() != null ? name() : Objects.toString(_const);
	}

	/**
	 * Return a new constant with the given name and value.
	 *
	 * @param name the constant name
	 * @param value the constant value
	 * @param <T> the constant type
	 * @return a new constant
	 * @throws NullPointerException if the given constant {@code name} is
	 *        {@code null}
	 */
	public static <T> Const<T> of(final String name, final T value) {
		return new Const<>(requireNonNull(name), value);
	}

	/**
	 * Return a new constant with the given value.
	 *
	 * @param value the constant value
	 * @param <T> the constant type
	 * @return a new constant
	 */
	public static <T> Const<T> of(final T value) {
		return new Const<>(null, value);
	}


	/* *************************************************************************
	 *  Java object serialization
	 * ************************************************************************/

	@Serial
	private Object writeReplace() {
		return new SerialProxy(SerialProxy.CONST, this);
	}

	@Serial
	private void readObject(final ObjectInputStream stream)
		throws InvalidObjectException
	{
		throw new InvalidObjectException("Serialization proxy required.");
	}

	void write(final ObjectOutput out) throws IOException {
		writeNullableString(name(), out);
		out.writeObject(_const);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	static Object read(final ObjectInput in)
		throws IOException, ClassNotFoundException
	{
		final String name = readNullableString(in);
		final Object value = in.readObject();
		return new Const(name, value);
	}

}