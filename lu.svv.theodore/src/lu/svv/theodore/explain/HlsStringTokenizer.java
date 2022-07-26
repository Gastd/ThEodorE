package lu.svv.theodore.explain;

import static java.lang.Character.isDigit;
import static java.lang.Character.isJavaIdentifierPart;
import static java.lang.Character.isJavaIdentifierStart;
import static java.lang.String.format;
import static lu.svv.theodore.explain.MathTokenType.COMMA;
import static lu.svv.theodore.explain.MathTokenType.DIV;
import static lu.svv.theodore.explain.MathTokenType.IDENTIFIER;
import static lu.svv.theodore.explain.MathTokenType.LPAREN;
import static lu.svv.theodore.explain.MathTokenType.MINUS;
import static lu.svv.theodore.explain.MathTokenType.MOD;
import static lu.svv.theodore.explain.MathTokenType.NUMBER;
import static lu.svv.theodore.explain.MathTokenType.PLUS;
import static lu.svv.theodore.explain.MathTokenType.POW;
import static lu.svv.theodore.explain.MathTokenType.RPAREN;
import static lu.svv.theodore.explain.MathTokenType.TIMES;

import io.jenetics.ext.internal.parser.CharSequenceTokenizer;
import io.jenetics.ext.internal.parser.ParsingException;
import io.jenetics.ext.internal.parser.Token;

/**
 * Tokenizer for simple arithmetic expressions.
 *
 * <pre>{@code
 * LPAREN: '(';
 * RPAREN: ')';
 * COMMA: ',';
 * PLUS: '+';
 * MINUS: '-';
 * TIMES: '*';
 * DIV: '/';
 * POW: '^';
 * NUMBER: ('0'..'9')+ ('.' ('0'..'9')+)? ((e|E) (+|-)? ('0'..'9'))?
 * ID: ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')+;
 * WS: [ \r\n\t] + -> skip;
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstotter</a>
 * @since 7.1
 * @version 7.1
 */
final class HlsStringTokenizer extends CharSequenceTokenizer {

	public HlsStringTokenizer(final CharSequence input) {
		super(input);
	}

	@Override
	public Token<String> next() {
		while (isNonEof(c)) {
			final char value = c;

			switch (value) {
				case ' ', '\r', '\n', '\t':
					WS();
					continue;
				case '(':
					consume();
					return LPAREN.token(value);
				case ')':
					consume();
					return RPAREN.token(value);
				case ',':
					consume();
					return COMMA.token(value);
				case '+':
					consume();
					return PLUS.token(value);
				case '-':
					consume();
					return MINUS.token(value);
				case '*':
					if (LA(2) == '*') {
						consume();
						consume();
						return POW.token("**");
					} else {
						consume();
						return TIMES.token(value);
					}
				case '/':
					consume();
					return DIV.token(value);
				case '%':
					consume();
					return MOD.token(value);
				case '^':
					consume();
					return POW.token(value);
				case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
					return NUMBER();
				default:
					if (isJavaIdentifierStart(c)) {
						return ID();
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
			}
		}

		return null;
	}

	// NUMBER (E SIGN? UNSIGNED_INTEGER)?
	private Token<String> NUMBER() {
		final var value = new StringBuilder();

		REAL_NUMBER(value);
		if ('e' == c || 'E' == c) {
			value.append(c);
			consume();

			if ('+' == c || '-' == c) {
				value.append(c);
				consume();
			}
			if (isDigit(c)) {
				UNSIGNED_NUMBER(value);
			}
		}

		return NUMBER.token(value.toString());
	}

	// ('0' .. '9') + ('.' ('0' .. '9') +)?
	private void REAL_NUMBER(final StringBuilder value) {
		UNSIGNED_NUMBER(value);
		if ('.' == c) {
			value.append(c);
			consume();
			UNSIGNED_NUMBER(value);
		}
	}

	// ('0' .. '9')+
	private void UNSIGNED_NUMBER(final StringBuilder value) {
		while (isDigit(c)) {
			value.append(c);
			consume();
		}
	}

	private Token<String> ID() {
		final var value = new StringBuilder();

		do {
			value.append(c);
			consume();
		} while (isJavaIdentifierPart(c));

		return IDENTIFIER.token(value.toString());
	}

}