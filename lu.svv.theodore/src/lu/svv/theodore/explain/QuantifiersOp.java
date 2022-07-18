package lu.svv.theodore.explain;


import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import io.jenetics.prog.op.Op;
import io.jenetics.ext.util.Tree;
import io.jenetics.ext.util.TreeNode;

import lu.svv.theodore.explain.Cast;


public enum QuantifiersOp implements Op<Double> {

    /**
     * Conjunction. <em>This operation has arity 2.</em>
     */
    FORALL("forall", 3, v -> forAll(v[0], v[1], v[2])),

    /**
     * Disjunction. <em>This operation has arity 2.</em>
     */
    EXISTS("exists", 3, v -> exists(v[0], v[1], v[2]));

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

    private final String _name;
    private final int _arity;
    private final Function<Double[], Double> _function;

    QuantifiersOp(
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
    public static Op<Double> toBoolOp(final String string) {
            requireNonNull(string);

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
                    case "1": return Optional.of(true);
                    case "false":
                    case "0": return Optional.of(false);
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