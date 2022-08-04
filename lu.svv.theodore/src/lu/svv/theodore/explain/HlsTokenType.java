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
    NE(59),             // '!='
    LPAREN(45),         // '('
    LPARENMINUS(91),    // '(-'
    RPAREN(47),         // ')'
    TIMES(81),          // '*'
    PLUS(79),           // '+'
    COMMA(28),          // ','
    MINUS(80),          // '-'
    IMPL(50),           // '->'
    DOT(90),            // '.'
    DIV(82),            // '/'
    COLON(48),          // ':'
    ASSIGN(38),         // '::='
    SEMICOLON(31),      // ';'
    LT(57),             // '<'
    LE(55),             // '<='
    EQU(58),            // '=='
    GT(56),             // '>'
    GE(54),             // '>='
    ATINDEX(87),        // '@index'
    ATTIMESTAMP(86),    // '@timestamp'
    ANDUPPER(52),       // 'And'
    CONSTANT(37),       // 'Constant'
    EXISTS(42),         // 'Exists'
    FINALINDEX(89),     // 'FinalIndex'
    FINALTIMESTAMP(92), // 'FinalTimestamp'
    FORALL(41),         // 'ForAll'
    GOAL(11),           // 'Goal:'
    INUPPER(43),        // 'In'
    INDEX(30),          // 'Index'
    INTERPOLATION(35),  // 'Interpolation'
    LINEAR(36),         // 'Linear'
    NOT(53),            // 'Not'
    NUM(33),            // 'Num'
    OR(51),             // 'Or'
    PROPERTIES(27),     // 'Properties='
    REQUIREMENT(39),    // 'Requirement'
    SAMPLESTEP(19),     // 'SampleStep='
    SAMPLE_STEP(14),    // 'Sample_Step:'
    SIGNALUPPER(34),    // 'Signal'
    SPECIFICATION(40),  // 'Specification'
    TIMESTAMP(32),      // 'Timestamp'
    TRACE(18),          // 'Trace'
    VALUEUPPER(49),     // 'Value'
    LBRACKETS(44),      // '['
    HOUR(20),           // '[h]'
    MICROS(24),         // '[micros]'
    MIN(21),            // '[min]'
    MS(23),             // '[ms]'
    NANOSBRACKETS(25),  // '[nanos]'
    SEC(22),            // '[s]'
    RBRACKETS(46),      // ']'
    POW(83),            // '^('
    AMPLITUDE(67),      // 'amplitude'
    ANDLOWER(63),       // 'and'
    BY(77),             // 'by'
    FALLS(74),          // 'falls'
    FIXEDMANUAL(16),    // 'fixed-manual'
    FIXEDMIN(17),       // 'fixed-min'
    GENERATE(13),       // 'generate'
    I2T(85),            // 'i2t'
    INLOWER(61),        // 'in'
    INTERVAL(62),       // 'interval'
    MONOTONICALLY(72),  // 'monotonically'
    NANOS(88),          // 'nanos'
    OSCILLATION(68),    // 'oscillation'
    OVERSHOOTS(75),     // 'overshoots'
    P2PAMP(69),         // 'p2pAmp'
    PERIOD(70),         // 'period'
    REACHING(73),       // 'reaching'
    RISES(71),          // 'rises'
    SAVE(12),           // 'save'
    SIGNALLOWER(64),    // 'signal'
    SPIKE(60),          // 'spike'
    T2I(84),            // 't2i'
    UNDERSHOOTS(78),    // 'undershoots'
    VALUELOWER(76),     // 'value'
    VARIABLE(15),       // 'variable'
    WIDTH(66),          // 'width'
    WITH(65),           // 'with'
    LBRACES(26),        // '{'
    RBRACES(29),        // '}'

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
