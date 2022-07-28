package lu.svv.theodore.explain;


import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.jenetics.prog.op.Op;
//import io.jenetics.prog.op.Const;
import io.jenetics.prog.op.Var;
import io.jenetics.ext.util.Tree;
import io.jenetics.ext.util.TreeNode;

public enum HlsOp implements Op<Double> {

	/* *************************************************************************
	 * Arithmetic operations
	 * ************************************************************************/
	/**
	 * The identity function.
	 */
	ID("id", 1, v -> v[0]),

	/**
	 * Return the negation value of a double value.
	 * <em>This operation has arity 1.</em>
	 */
	NEG("neg", 1, v -> -v[0]),

	/**
	 * Returns the sum of its arguments.
	 * <em>This operation has arity 2.</em>
	 */
	ADD("add", 2, v -> v[0] + v[1]),

	/**
	 * Return the diff of its arguments.
	 * <em>This operation has arity 2.</em>
	 */
	SUB("sub", 2, v -> v[0] - v[1]),

	/**
	 * Returns the product of its arguments.
	 * <em>This operation has arity 2.</em>
	 */
	MUL("mul", 2, v -> v[0]*v[1]),

	/**
	 * Returns the quotient of its arguments.
	 * <em>This operation has arity 2.</em>
	 */
	DIV("div", 2, v -> v[0]/v[1]),

	/* *************************************************************************
	 * Conditional functions
	 * ************************************************************************/

	/**
	 * Returns +1.0 if its first argument is greater than its second argument
	 * and returns -1.0 otherwise.
	 *
	 * @since 5.0
	 */
	GT("gt", 2, v -> v[0] > v[1] ? 1.0 : -1.0),

	/**
	 * Returns +1.0 if its first argument is greater or equal than its second argument
	 * and returns -1.0 otherwise.
	 *
	 * @since 5.0
	 */
	GE("ge", 2, v -> v[0] >= v[1] ? 1.0 : -1.0),

	/**
	 * Returns +1.0 if its first argument is less than its second argument
	 * and returns -1.0 otherwise.
	 *
	 * @since 5.0
	 */
	LT("lt", 2, v -> v[0] < v[1] ? 1.0 : -1.0),

	/**
	 * Returns +1.0 if its first argument is less or equal than its second argument
	 * and returns -1.0 otherwise.
	 *
	 * @since 5.0
	 */
	LE("le", 2, v -> v[0] <= v[1] ? 1.0 : -1.0),

	/**
	 * Returns +1.0 if its first argument is equal than its second argument
	 * and returns -1.0 otherwise.
	 *
	 * @since 5.0
	 */
	EQ("eq", 2, v -> v[0] == v[1] ? 1.0 : -1.0),

	/**
	 * Returns +1.0 if its first argument is not equal than its second argument
	 * and returns -1.0 otherwise.
	 *
	 * @since 5.0
	 */
	NE("ne", 2, v -> v[0] != v[1] ? 1.0 : -1.0),

	/* *************************************************************************
	 * Logic operations
	 * ************************************************************************/
	/**
     * Conjunction. <em>This operation has arity 2.</em>
     */
    AND("and", 2, v -> Cast.toDouble(Cast.toBoolean(v[0]) && Cast.toBoolean(v[1]))),

    /**
     * Disjunction. <em>This operation has arity 2.</em>
     */
    OR("or", 2, v -> Cast.toDouble(Cast.toBoolean(v[0]) || Cast.toBoolean(v[1]))),

    /**
     * Negation. <em>This operation has arity 1.</em>
     */
    NOT("not", 1, v -> Cast.toDouble(!Cast.toBoolean(v[0]))),

    /**
     * Implication. <em>This operation has arity 2.</em>
     */
    IMP("imp", 2, v -> Cast.toDouble(!Cast.toBoolean(v[0]) || Cast.toBoolean(v[1]))),

    /**
     * Exclusive or. <em>This operation has arity 2.</em>
     */
    XOR("xor", 2, v -> Cast.toDouble((Cast.toBoolean(v[0]) || Cast.toBoolean(v[1])) && !(Cast.toBoolean(v[0]) && Cast.toBoolean(v[1])))),

    /**
     * Equivalence. <em>This operation has arity 2.</em>
     */
    EQU("equ", 2, v -> Cast.toDouble((Cast.toBoolean(v[0]) && Cast.toBoolean(v[1])) || (!Cast.toBoolean(v[0]) && !Cast.toBoolean(v[1])))),

    /**
     * Conjunction. <em>This operation has arity 3.</em>
     */
    FORALL("ForAll", 3, v -> forAll(v[0], v[1], v[2])),

    /**
     * Disjunction. <em>This operation has arity 3.</em>
     */
    EXISTS("Exists", 3, v -> exists(v[0], v[1], v[2]));
	
	/**
	 * The double value that is closer than any other to pi, the ratio of the
	 * circumference of a circle to its diameter. <em>This is a terminal
	 * operation.</em>
	 *
	 * @see Math#PI
	 */
	public static final Const<Double> PI = Const.of("pi", Math.PI);

    /**
     * Represents the constant {@code true}.
     */
    public static final Const<Double> TRUE = Const.of("true", 1.0);

    /**
     * Represents the constant {@code true}.
     */
    public static final Const<Double> FALSE = Const.of("false", 0.0);

	/**
	 * Stub method
	 * @param lowerBound
	 * @param upperBound
	 * @param list
	 * @return
	 */
    private static Double forAll(Double lowerBound, Double upperBound, Double list) {
        return 1.0;
    }

    /**
     * Stub method
     * @param lowerBound
     * @param upperBound
     * @param list
     * @return
     */
    private static Double exists(Double lowerBound, Double upperBound, Double list) {
        return 1.0;
    }
    
    /**
	 * The names of all defined operation names.
	 *
	 * @since 7.0
	 */
	public static final Set<String> NAMES = Stream.of(HlsOp.values())
		.map(HlsOp::toString)
		.collect(Collectors.toUnmodifiableSet());

    private final String _name;
    private final int _arity;
    private final Function<Double[], Double> _function;

    HlsOp(
            final String name,
            final int arity,
            final Function<Double[], Double> function
    ) {
            assert name != null;
            assert arity >= 0;
            assert function != null;

            _name = name;
            _function = function;
            _arity = arity;
    }

    @Override
    public int arity() {
            return _arity;
    }

    @Override
    public Double apply(final Double[] args) {
            return _function.apply(args);
    }

    /**
     * Evaluates the operation with the given arguments.
     *
     * @see #apply(Double[])
     *
     * @param args the operation arguments
     * @return the evaluated operation
     */
    public Double eval(final Double... args) {
            final Double[] v = new Double[args.length];
            for (int i = 0; i < args.length; ++i) {
                    v[i] = args[i];
            }

            return apply(v);
    }

    @Override
    public String toString() {
            return _name;
    }


    /**
     * Converts the string representation of an operation to the operation
     * object. It is used for converting the string representation of a tree to
     * an operation tree. If you use it that way, you should not forget to
     * re-index the tree variables.
     *
     * <pre>{@code
     * final TreeNode<Op<Double>> tree = TreeNode.parse(
     *     "and(or(x,y),not(y))",
     *     BoolOp::toBoolOp
     * );
     *
     * assert Program.eval(tree, false, false) == false;
     * Var.reindex(tree);
     * assert Program.eval(tree, false, false) == true;
     * }</pre>
     *
     * @since 5.0
     *
     * @see Var#reindex(TreeNode)
     * @see Program#eval(Tree, Object[])
     *
     * @param string the string representation of an operation which should be
     *        converted
     * @return the operation, converted from the given string
     * @throws IllegalArgumentException if the given {@code value} doesn't
     *         represent a mathematical expression
     * @throws NullPointerException if the given string {@code value} is
     *         {@code null}
     */
    public static Op<Double> toHlsOp(final String string) {
    	requireNonNull(string);
    	System.out.println(string);

		final Op<Double> result;
		final Optional<Const<Double>> cop = toConst(string);
		if (cop.isPresent()) {
			result = cop.orElseThrow(AssertionError::new);
		} else {
			final Optional<Op<Double>> mop = toOp(string);
			result = mop.isPresent()
				? mop.orElseThrow(AssertionError::new)
				: Var.parse(string);
		}

		return result;
    }

    static Optional<Const<Double>> toConst(final String string) {
        return tryParseDouble(string)
                .map(Const::of);
    }

    private static Optional<Double> tryParseDouble(final String value) {
        switch (value) {
                case "true":
                case "1": return Optional.of(1.0);
                case "false":
                case "0": return Optional.of(0.0);
                default: return Optional.empty();
        }
    }

    private static Optional<Op<Double>> toOp(final String string) {
            return Stream.of(values())
                    .filter(op -> Objects.equals(op._name, string))
                    .map(op -> (Op<Double>)op)
                    .findFirst();
    }
}