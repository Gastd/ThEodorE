package lu.svv.theodore.explain;

import io.jenetics.Mutator;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathExpr;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.prog.regression.Regression;
import io.jenetics.prog.regression.Sample;
import io.jenetics.util.ISeq;

import lu.svv.theodore.hls.And;
import lu.svv.theodore.hls.Expression;
import lu.svv.theodore.hls.Or;
import lu.svv.theodore.explain.DoubleBoolOp;
import lu.svv.theodore.explain.QuantifiersOp;

public class Explain {

	// Definition of the allowed operations
	// Ricardo> Here I think that we should have the internal nodes of our property: ForAll, And, Or, ...
	private static final ISeq<Op<Double>> OPS = ISeq.of(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV,
			DoubleBoolOp.AND, DoubleBoolOp.OR, DoubleBoolOp.NOT, DoubleBoolOp.IMP, DoubleBoolOp.XOR, DoubleBoolOp.EQU,
			QuantifiersOp.FORALL, QuantifiersOp.EXISTS);

	// Definition of terminals
	// Ricardo> Here I think that we should have the internal nodes of our property: Consts, Vars, ...
	private static final ISeq<Op<Double>> TMS = ISeq.of(Var.of("x",0), EphemeralConst.of(() -> (double) random().nextInt (10)));
	
	private static final int depth = 5;
	
	final ProgramChromosome<Double> program = ProgramChromosome.of(depth, OPS, TMS);

	private static final Regression<Double> REGRESSION = Regression.of(
	    Regression.codecOf(OPS, TMS, 5),
	    Error.of(LossFunction::mse),
	    Sample.ofDouble(-1.0, -8.0000),
	    // ...
	    Sample.ofDouble(0.9, 1.3860),
	    Sample.ofDouble(1.0, 2.0000)
	);
	
	/**
	 * Transform AST Expression in a Program Chromosome
	 * 
	 * @param expression HLS Expression
	 * @return the AST as a Chromosome
	 */
	public static ProgramChromosome<Double> setSeed(Expression expression) {
		ProgramChromosome<Double> seedExpression;
//		if(expression instanceof And) {
//			ProgramChromosome<Double> left = setSeed(((And)expression).getLeft());
//			ProgramChromosome<Double> right = setSeed(((And)expression).getRight());
//		}
		return seedExpression;
	}

	public static void run() {
	    final Engine<ProgramGene<Double>, Double> engine = Engine
	        .builder(REGRESSION)
	        .minimizing()
	        .alterers(
	            new SingleNodeCrossover<>(0.1),
	            new Mutator<>())
	        .build();

	    final EvolutionResult<ProgramGene<Double>, Double> result = engine
	        .stream()
	        .limit(Limits.byFitnessThreshold(0.01))
	        .collect(EvolutionResult.toBestEvolutionResult());

	    final ProgramGene<Double> program = result.bestPhenotype()
	        .genotype()
	        .gene();

	    final TreeNode<Op<Double>> tree = program.toTreeNode();
		    MathExpr.rewrite(tree); // Simplify result program.
		    System.out.println("Generations: " + result.totalGenerations());
		    System.out.println("Function:    " + new MathExpr(tree));
		    System.out.println("Error:       " + REGRESSION.error(tree));
	    
	    final Error error = Error.of(
	    	    LossFunction::mse,
	    	    Complexity.ofMaxNodeCount(50)
	    	);
	}
}

