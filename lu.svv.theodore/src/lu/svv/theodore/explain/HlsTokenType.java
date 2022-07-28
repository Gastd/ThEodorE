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
	GT(12),
	GE(13),
	LT(14),
	LE(15),
	NE(16),
	EQ(17),
	POW(18),
	AND(19),
	OR(20),
	IMP(21),
	XOR(22),
	EQU(23),
	FORALL(24),
	EXISTS(25),

	NUMBER(26),
	IDENTIFIER(27);

	private final int _code;

	HlsTokenType(final int code) {
		_code = code;
	}

	@Override
	public int code() {
		return _code;
	}

}
