package lu.svv.theodore.explain;

import io.jenetics.ext.internal.parser.Token;

/**
 * Token types as they are used in <em>mathematical</em> (arithmetic) expressions.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstotter</a>
 * @version 7.1
 * @since 7.1
 */
enum HlsTokenType implements Token.Type {
	LBRACKETS(1),
	RBRACKETS(2),
	LBRACES(3),
	RBRACES(4),
	LPAREN(5),
	RPAREN(6),
	COMMA(7),

	PLUS(8),
	MINUS(9),
	TIMES(10),
	DIV(11),
	POW(12),
	AND(13),
	OR(14),
	IMP(15),
	XOR(16),
	EQU(17),
	FORALL(18),
	EXISTS(19),

	NUMBER(20),
	IDENTIFIER(21);

	private final int _code;

	HlsTokenType(final int code) {
		_code = code;
	}

	@Override
	public int code() {
		return _code;
	}

}
