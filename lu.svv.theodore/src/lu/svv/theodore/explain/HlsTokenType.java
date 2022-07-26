package lu.svv.theodore.explain;

import io.jenetics.ext.internal.parser.Token;

/**
 * Token types as they are used in <em>mathematical</em> (arithmetic) expressions.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstotter</a>
 * @version 7.1
 * @since 7.1
 */
enum MathTokenType implements Token.Type {
	LPAREN(1),
	RPAREN(2),
	COMMA(3),

	PLUS(4),
	MINUS(5),
	TIMES(6),
	DIV(7),
	MOD(8),
	POW(9),

	NUMBER(10),
	IDENTIFIER(11);

	private final int _code;

	MathTokenType(final int code) {
		_code = code;
	}

	@Override
	public int code() {
		return _code;
	}

}
