package lu.svv.theodore.explain;


import java.util.Map;

import io.jenetics.ext.util.Tree;
//import io.jenetics.prog.op.HlsOp;
import io.jenetics.prog.op.Op;
import lu.svv.theodore.explain.HlsOp;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstotter</a>
 * @version 4.3
 * @since 4.3
 */
final class HlsExprFormatter {
	private HlsExprFormatter() {}

	private static final Map<Op<Double>, String> INFIX_OPS = Map.ofEntries(
		Map.entry(HlsOp.ADD, " + "),
		Map.entry(HlsOp.SUB, " - "),
		Map.entry(HlsOp.MUL, "*"),
		Map.entry(HlsOp.DIV, "/"),
		Map.entry(HlsOp.GT, ">"),
		Map.entry(HlsOp.GE, ">="),
		Map.entry(HlsOp.LT, "<"),
		Map.entry(HlsOp.LE, "<="),
		Map.entry(HlsOp.AND, " and "),
		Map.entry(HlsOp.OR, " or "),
		Map.entry(HlsOp.NOT, " not "),
		Map.entry(HlsOp.IMP, " -> "),
		Map.entry(HlsOp.XOR, " xor "),
		Map.entry(HlsOp.EQU, " == "),
		Map.entry(HlsOp.FORALL, " ForAll "),
		Map.entry(HlsOp.EXISTS, " Existis ")
	);

	private static final Map<Op<Double>, Integer> PRECEDENCE = Map.ofEntries(
		Map.entry(HlsOp.ADD, 6),
		Map.entry(HlsOp.SUB, 6),
		Map.entry(HlsOp.MUL, 5),
		Map.entry(HlsOp.DIV, 5),
		Map.entry(HlsOp.GT,  5),
		Map.entry(HlsOp.GE,  5),
		Map.entry(HlsOp.LT,  6),
		Map.entry(HlsOp.LE,  4),
		Map.entry(HlsOp.EQ,  4),
		Map.entry(HlsOp.AND, 6),
		Map.entry(HlsOp.OR,  6),
		Map.entry(HlsOp.NOT, 5),
		Map.entry(HlsOp.IMP, 5),
		Map.entry(HlsOp.XOR, 6),
		Map.entry(HlsOp.EQU, 5),
		Map.entry(HlsOp.FORALL, 4),
		Map.entry(HlsOp.EXISTS, 4)
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