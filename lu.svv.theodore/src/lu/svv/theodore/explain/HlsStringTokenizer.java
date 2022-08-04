package lu.svv.theodore.explain;

import static java.lang.Character.isDigit;
import static java.lang.Character.isJavaIdentifierPart;
import static java.lang.Character.isJavaIdentifierStart;
import static java.lang.String.format;
import static lu.svv.theodore.explain.HlsTokenType.NE;
import static lu.svv.theodore.explain.HlsTokenType.LPAREN;
import static lu.svv.theodore.explain.HlsTokenType.LPARENMINUS;
import static lu.svv.theodore.explain.HlsTokenType.RPAREN;
import static lu.svv.theodore.explain.HlsTokenType.TIMES;
import static lu.svv.theodore.explain.HlsTokenType.PLUS;
import static lu.svv.theodore.explain.HlsTokenType.COMMA;
import static lu.svv.theodore.explain.HlsTokenType.MINUS;
import static lu.svv.theodore.explain.HlsTokenType.IMPL;
import static lu.svv.theodore.explain.HlsTokenType.DOT;
import static lu.svv.theodore.explain.HlsTokenType.DIV;
import static lu.svv.theodore.explain.HlsTokenType.COLON;
import static lu.svv.theodore.explain.HlsTokenType.ASSIGN;
import static lu.svv.theodore.explain.HlsTokenType.SEMICOLON;
import static lu.svv.theodore.explain.HlsTokenType.LT;
import static lu.svv.theodore.explain.HlsTokenType.LE;
import static lu.svv.theodore.explain.HlsTokenType.EQU;
import static lu.svv.theodore.explain.HlsTokenType.GT;
import static lu.svv.theodore.explain.HlsTokenType.GE;
import static lu.svv.theodore.explain.HlsTokenType.ATINDEX;
import static lu.svv.theodore.explain.HlsTokenType.ATTIMESTAMP;
import static lu.svv.theodore.explain.HlsTokenType.ANDUPPER;
import static lu.svv.theodore.explain.HlsTokenType.CONSTANT;
import static lu.svv.theodore.explain.HlsTokenType.EXISTS;
import static lu.svv.theodore.explain.HlsTokenType.FINALINDEX;
import static lu.svv.theodore.explain.HlsTokenType.FINALTIMESTAMP;
import static lu.svv.theodore.explain.HlsTokenType.FORALL;
import static lu.svv.theodore.explain.HlsTokenType.GOAL;
import static lu.svv.theodore.explain.HlsTokenType.INUPPER;
import static lu.svv.theodore.explain.HlsTokenType.INDEX;
import static lu.svv.theodore.explain.HlsTokenType.INTERPOLATION;
import static lu.svv.theodore.explain.HlsTokenType.LINEAR;
import static lu.svv.theodore.explain.HlsTokenType.NOT;
import static lu.svv.theodore.explain.HlsTokenType.NUM;
import static lu.svv.theodore.explain.HlsTokenType.OR;
import static lu.svv.theodore.explain.HlsTokenType.PROPERTIES;
import static lu.svv.theodore.explain.HlsTokenType.REQUIREMENT;
import static lu.svv.theodore.explain.HlsTokenType.SAMPLESTEP;
import static lu.svv.theodore.explain.HlsTokenType.SAMPLE_STEP;
import static lu.svv.theodore.explain.HlsTokenType.SIGNALUPPER;
import static lu.svv.theodore.explain.HlsTokenType.SPECIFICATION;
import static lu.svv.theodore.explain.HlsTokenType.TIMESTAMP;
import static lu.svv.theodore.explain.HlsTokenType.TRACE;
import static lu.svv.theodore.explain.HlsTokenType.VALUEUPPER;
import static lu.svv.theodore.explain.HlsTokenType.LBRACKETS;
import static lu.svv.theodore.explain.HlsTokenType.HOUR;
import static lu.svv.theodore.explain.HlsTokenType.MICROS;
import static lu.svv.theodore.explain.HlsTokenType.MIN;
import static lu.svv.theodore.explain.HlsTokenType.MS;
import static lu.svv.theodore.explain.HlsTokenType.NANOSBRACKETS;
import static lu.svv.theodore.explain.HlsTokenType.SEC;
import static lu.svv.theodore.explain.HlsTokenType.RBRACKETS;
import static lu.svv.theodore.explain.HlsTokenType.POW;
import static lu.svv.theodore.explain.HlsTokenType.AMPLITUDE;
import static lu.svv.theodore.explain.HlsTokenType.ANDLOWER;
import static lu.svv.theodore.explain.HlsTokenType.BY;
import static lu.svv.theodore.explain.HlsTokenType.FALLS;
import static lu.svv.theodore.explain.HlsTokenType.FIXEDMANUAL;
import static lu.svv.theodore.explain.HlsTokenType.FIXEDMIN;
import static lu.svv.theodore.explain.HlsTokenType.GENERATE;
import static lu.svv.theodore.explain.HlsTokenType.I2T;
import static lu.svv.theodore.explain.HlsTokenType.INLOWER;
import static lu.svv.theodore.explain.HlsTokenType.INTERVAL;
import static lu.svv.theodore.explain.HlsTokenType.MONOTONICALLY;
import static lu.svv.theodore.explain.HlsTokenType.NANOS;
import static lu.svv.theodore.explain.HlsTokenType.OSCILLATION;
import static lu.svv.theodore.explain.HlsTokenType.OVERSHOOTS;
import static lu.svv.theodore.explain.HlsTokenType.P2PAMP;
import static lu.svv.theodore.explain.HlsTokenType.PERIOD;
import static lu.svv.theodore.explain.HlsTokenType.REACHING;
import static lu.svv.theodore.explain.HlsTokenType.RISES;
import static lu.svv.theodore.explain.HlsTokenType.SAVE;
import static lu.svv.theodore.explain.HlsTokenType.SIGNALLOWER;
import static lu.svv.theodore.explain.HlsTokenType.SPIKE;
import static lu.svv.theodore.explain.HlsTokenType.T2I;
import static lu.svv.theodore.explain.HlsTokenType.UNDERSHOOTS;
import static lu.svv.theodore.explain.HlsTokenType.VALUELOWER;
import static lu.svv.theodore.explain.HlsTokenType.VARIABLE;
import static lu.svv.theodore.explain.HlsTokenType.WIDTH;
import static lu.svv.theodore.explain.HlsTokenType.WITH;
import static lu.svv.theodore.explain.HlsTokenType.LBRACES;
import static lu.svv.theodore.explain.HlsTokenType.RBRACES;
import static lu.svv.theodore.explain.HlsTokenType.NUMBER;
import static lu.svv.theodore.explain.HlsTokenType.IDENTIFIER;

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
					if (LA(2) == '-') {
						consume();
						consume();
						return LPARENMINUS.token("(-");
					} else {
						consume();
						return LPAREN.token(value);
					}
				case ')':
					consume();
					return RPAREN.token(value);
				case ',':
					consume();
					return COMMA.token(value);
				case '.':
					consume();
					return DOT.token(value);
				case ':':
					if (LA(2) == ':' && LA(3) == '=') {
						consume();
						consume();
						consume();
						return ASSIGN.token("::=");
					} else if (LA(2) == ' ') {
						consume();
						return COLON.token(value);
					} else {
						throw new ParsingException(format(
								"Got invalid character '%s' at position '%d'.",
								c, pos
							));
						}
				case ';':
					consume();
					return SEMICOLON.token(value);
				case '+':
					consume();
					return PLUS.token(value);
				case '-':
					if (LA(2) == '>') {
						consume();
						consume();
						return IMPL.token("->");
					} else {
						consume();
						return MINUS.token(value);
					}
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
				case '@':
					if (LA(2) == 'i') {
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						return ATINDEX.token("@index");
					} else if (LA(2) == 't') {
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						return ATINDEX.token("@timestamp");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case '>':
					if (LA(2) == '=') {
						consume();
						consume();
						return GE.token(">=");
					} else {
						consume();
						return GT.token(value);
					}
				case '<':
					if (LA(2) == '=') {
						consume();
						consume();
						return LE.token("<=");
					} else {
						consume();
						return LT.token(value);
					}
				case '!':
					if (LA(2) == '=') {
						consume();
						consume();
						return NE.token("!=");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case '=':
					if (LA(2) == '=') {
						consume();
						consume();
						return EQU.token("==");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case 'A':
					if (LA(2) == 'n' && LA(3) == 'd') {
						consume();
						consume();
						consume();
						return ANDUPPER.token("And");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case 'a':
					if (LA(2) == 'n' && LA(3) == 'd') {
						consume();
						consume();
						consume();
						return ANDLOWER.token("and");
					} else if (LA(2) == 'm' && LA(3) == 'p') {
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						return AMPLITUDE.token("amplitude");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case 'E':
					if (LA(2) == 'x') {
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
						return EXISTS.token("Exists");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case 'F':
					if (LA(2) == 'i' && LA(6) == 'I') {
						consume();//F
						consume();//i
						consume();//n
						consume();//a
						consume();//l
						consume();//I
						consume();//n
						consume();//d
						consume();//e
						consume();//x
						return FINALINDEX.token("FinalIndex");
					} else if (LA(2) == 'i' && LA(6) == 'T') {
						consume();//F
						consume();//i
						consume();//n
						consume();//a
						consume();//l
						consume();//T
						consume();//i
						consume();//m
						consume();//e
						consume();//s
						consume();//t
						consume();//a
						consume();//m
						consume();//p
						return FINALTIMESTAMP.token("FinalTimestamp");
					} else if (LA(2) == 'o') {
						consume();
						consume();
						consume();
						consume();
						consume();
						consume();
//						System.out.println(FORALL.token("ForAll"));
						return FORALL.token("ForAll");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case 'G':
					if (LA(2) == 'o') {
						consume();//G
						consume();//o
						consume();//a
						consume();//l
						consume();//:
						return GOAL.token("Goal:");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case 'I':
					if (LA(3) == 'd') {
						consume();//I
						consume();//n
						consume();//d
						consume();//e
						consume();//x
						return INDEX.token("Index");
					} else if (LA(3) == 't') {
						consume();//I
						consume();//n
						consume();//t
						consume();//e
						consume();//r
						consume();//p
						consume();//o
						consume();//l
						consume();//a
						consume();//t
						consume();//i
						consume();//o
						consume();//n
						return INTERPOLATION.token("Interpolation");
					} else if (LA(2) == 'n') {
						consume();//I
						consume();//n
						return INUPPER.token("In");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
				case 'L':
					if (LA(2) == 'i' ) {
						consume();//L
						consume();//i
						consume();//n
						consume();//e
						consume();//a
						consume();//r
						return LINEAR.token("Linear");
					} else {
						throw new ParsingException(format(
								"Got invalid character '%s' at position '%d'.",
								c, pos
							));
					}
				case 'N':
					if (LA(2) == 'o') {
						consume();//N
						consume();//o
						consume();//t
						return NOT.token("Not");
					} else if (LA(2) == 'u') {
						consume();//N
						consume();//u
						consume();//m
						return NUM.token("Num");
					} else {
						throw new ParsingException(format(
								"Got invalid character '%s' at position '%d'.",
								c, pos
							));
					}
				case 'O':
					if (LA(2) == 'r') {
						consume();
						consume();
						return OR.token("Or");
					} else {
						throw new ParsingException(format(
								"Got invalid character '%s' at position '%d'.",
								c, pos
							));
					}
				case 'S':
					if(LA(2) == 'a' && LA(7) == 'S' && LA(7) == '=') {
						consume();//S
						consume();//a
						consume();//m
						consume();//p
						consume();//l
						consume();//e
						consume();//S
						consume();//t
						consume();//e
						consume();//p
						consume();//=
						return SAMPLESTEP.token("SampleStep=");
					} else if (LA(2) == 'a' && LA(7) == '_' && LA(12) == ':') {
						consume();//S
						consume();//a
						consume();//m
						consume();//p
						consume();//l
						consume();//e
						consume();//_
						consume();//S
						consume();//t
						consume();//e
						consume();//p
						consume();//:
						return SAMPLE_STEP.token("Sample_Step:");
					} else if (LA(2) == 'i' && LA(6) == 'l') {
						consume();//S
						consume();//i
						consume();//g
						consume();//n
						consume();//a
						consume();//l
						return SIGNALUPPER.token("Signal:");
					} else if (LA(2) == 'p' && LA(7) == 'i' && LA(13) == 'n') {
						consume();//S
						consume();//p
						consume();//e
						consume();//c
						consume();//i
						consume();//f
						consume();//i
						consume();//c
						consume();//a
						consume();//t
						consume();//i
						consume();//o
						consume();//n
						return SPECIFICATION.token("Specification");
					} else {
						throw new ParsingException(format(
								"Got invalid character '%s' at position '%d'.",
								c, pos
							));
					}
				case 'T':
					if (LA(2) == 'i') {
						consume();//T
						consume();//i
						consume();//m
						consume();//e
						consume();//s
						consume();//t
						consume();//a
						consume();//m
						consume();//p
						return TIMESTAMP.token("Timestamp");
					} else if (LA(2) == 'r') {
						consume();//T
						consume();//r
						consume();//a
						consume();//c
						consume();//e
						return TRACE.token("Trace");
					} else {
						throw new ParsingException(format(
								"Got invalid character '%s' at position '%d'.",
								c, pos
							));
					}
				case 'V':
					if (LA(2) == 'a') {
						consume();//V
						consume();//a
						consume();//l
						consume();//u
						consume();//e
						return VALUEUPPER.token("Value");
					}
				case '[':
					if (LA(2) == 'h') {
						consume();
						consume();
						consume();
						return HOUR.token("[h]");
					} else if (LA(2) == 'm' && LA(3) == 'i' && LA(4) == 'c') {
						consume();//[
						consume();//m
						consume();//i
						consume();//c
						consume();//r
						consume();//o
						consume();//s
						consume();//]
						return MICROS.token("[micros]");
					} else if (LA(2) == 'm' && LA(3) == 'i' && LA(4) == 'n') {
						consume();//[
						consume();//m
						consume();//i
						consume();//n
						consume();//]
						return MIN.token("[min]");
					} else if (LA(2) == 'm' && LA(3) == 's') {
						consume();//[
						consume();//m
						consume();//s
						consume();//]
						return MS.token("[ms]");
					} else if (LA(2) == 'n' && LA(3) == 'a') {
						consume();//[
						consume();//n
						consume();//a
						consume();//n
						consume();//o
						consume();//s
						consume();//]
						return NANOSBRACKETS.token("[nanos]");
					} else if (LA(2) == 's' && LA(3) == ']') {
						consume();//[
						consume();//s
						consume();//]
						return SEC.token("[s]");
					} else {
						consume();
						return LBRACKETS.token("[");
					}
				case ']':
					consume();
					return RBRACKETS.token("]");
				case '^':
					if (LA(2) == '(') {
						consume();
						consume();
						return POW.token("^(");
					} else {
						throw new ParsingException(format(
							"Got invalid character '%s' at position '%d'.",
							c, pos
						));
					}
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