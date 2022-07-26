package lu.svv.theodore.explain;


import java.util.Map;

import io.jenetics.ext.util.Tree;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstotter</a>
 * @version 4.3
 * @since 4.3
 */
final class HlsExprFormatter {
	private HlsExprFormatter() {}

	private static final Map<Op<Double>, String> INFIX_OPS = Map.of(
		MathOp.ADD, " + ",
		MathOp.SUB, " - ",
		MathOp.MUL, "*",
		MathOp.DIV, "/",
		MathOp.MOD, "%",
		MathOp.POW, "^"
	);

	private static final Map<Op<Double>, Integer> PRECEDENCE = Map.of(
		MathOp.ADD, 6,
		MathOp.SUB, 6,
		MathOp.MUL, 5,
		MathOp.DIV, 5,
		MathOp.MOD, 5,
		MathOp.POW, 4
	);

	static String format(final Tree<? extends Op<Double>, ?> tree) {
		final StringBuilder out = new StringBuilder();
		format(tree, out);
		return out.toString();
	}

	private static void format(
		final Tree<? extends Op<Double>, ?> tree,
		final StringBuilder out
	) {
		final Op<Double> op = tree.value();
		if (INFIX_OPS.containsKey(op)) {
			infix(tree, out);
		} else {
			out.append(op);
			if (!tree.isLeaf()) {
				out.append("(");
				format(tree.childAt(0), out);
				for (int i = 1; i < tree.childCount(); ++i) {
					out.append(", ");
					format(tree.childAt(i), out);
				}
				out.append(")");
			}
		}
	}

	private static void infix(
		final Tree<? extends Op<Double>, ?> tree,
		final StringBuilder out
	) {
		assert tree.childCount() == 2;

		final int precedence = PRECEDENCE.getOrDefault(tree.value(), 100);
		final int parentPrecedence = tree.parent()
			.map(p -> PRECEDENCE.getOrDefault(p.value(), 100))
			.orElse(100);

		final boolean brackets = !tree.isRoot() && precedence >= parentPrecedence;

		if (brackets) out.append("(");
		format(tree.childAt(0), out);
		out.append(INFIX_OPS.get(tree.value()));
		format(tree.childAt(1), out);
		if (brackets) out.append(")");
	}

}