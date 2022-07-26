package lu.svv.theodore.explain;

import static io.jenetics.util.RandomRegistry.random;

import java.util.function.Function;

import io.jenetics.EnumGene;
import io.jenetics.Genotype;
import io.jenetics.IntegerGene;
import io.jenetics.Mutator;
import io.jenetics.Phenotype;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.engine.EvolutionStream;
import io.jenetics.engine.Limits;
import io.jenetics.engine.Problem;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.Tree;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathExpr;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.prog.regression.Complexity;
import io.jenetics.prog.regression.LossFunction;
import io.jenetics.prog.regression.Regression;
import io.jenetics.prog.regression.Sample;
import io.jenetics.prog.regression.Error;
import io.jenetics.prog.regression.Sampling;
import io.jenetics.prog.regression.Sampling.Result;
import io.jenetics.util.Factory;
import io.jenetics.util.ISeq;

import lu.svv.theodore.hls.And;
import lu.svv.theodore.hls.Expression;
import lu.svv.theodore.hls.Or;
import lu.svv.theodore.explain.DoubleBoolOp;
import lu.svv.theodore.explain.QuantifiersOp;
import lu.svv.theodore.explain.ExprCompare;

public class Explain {

	// Definition of the allowed operations
	// Ricardo> Here I think that we should have the internal nodes of our property: ForAll, And, Or, ...
	private static final ISeq<Op<Double>> OPS = ISeq.of(MathOp.ADD, MathOp.SUB, MathOp.MUL, MathOp.DIV/*,
			DoubleBoolOp.AND, DoubleBoolOp.OR, DoubleBoolOp.NOT, DoubleBoolOp.IMP, DoubleBoolOp.XOR, DoubleBoolOp.EQU,
			QuantifiersOp.FORALL, QuantifiersOp.EXISTS*/
			);

	// Definition of terminals
	// Ricardo> Here I think that we should have the internal nodes of our property: Consts, Vars, ...
	private static final ISeq<Op<Double>> TMS = ISeq.of(Var.of("x",0), EphemeralConst.of(() -> (double) random().nextInt (10)));
	
	private static final int depth = 5;
	
	private ProgramChromosome<Double> _program =
		    ProgramChromosome.of(depth, OPS, TMS);
	
	final Tree<Op<Double>, ?> _formula;
	
	private Double _expected_result;
	
	public Tree<Op<Double>, ?> getForumula() {
		return _formula;
	}

	public Explain(Tree<Op<Double>, ?> seedExpression, Double expected_result) {
		this._program = ProgramChromosome.of(seedExpression, OPS, TMS);
		this._expected_result = expected_result;
		this._formula = seedExpression;
		this._error = Error.of(LossFunction::mse, Complexity.ofNodeCount(50));
	}

	/**
	 * Transform AST Expression in a Program Chromosome
	 * 
	 * @param expression HLS Expression
	 * @return the AST as a Chromosome
	 */
//	public static Explain of(/*Expression expression*/) {
	public static Explain of(String baseFormula, Double expected_result) {
		final MathExpr expr = MathExpr.parse(baseFormula);
		Tree<Op<Double>, ?> seedExpression = expr.tree();

		return new Explain(seedExpression, expected_result);
	}
	
	static String EXPR = "x+2-5";

	private static EvolutionStart<ProgramGene<Double>, Double>
	start(final int populationSize, final long generation) {
		Genotype<ProgramGene<Double>> genotype1 = Genotype.of(
				ProgramChromosome.of(MathExpr.parse(EXPR).tree(), ch -> ch.root().size() <= 50, OPS, TMS)
		 );
		Genotype<ProgramGene<Double>> genotype2 = Genotype.of(
				ProgramChromosome.of(MathExpr.parse(EXPR).tree(), ch -> ch.root().size() <= 50, OPS, TMS)
		 );

		Phenotype<ProgramGene<Double>, Double> phenotype1 = Phenotype.of(genotype1, generation, 10.0);
		Phenotype<ProgramGene<Double>, Double> phenotype2 = Phenotype.of(genotype2, generation, 10.0);

		final ISeq<Phenotype<ProgramGene<Double>, Double>> population = ISeq.of(phenotype1, phenotype2);

		return EvolutionStart.of(population, generation);
	}
	
	public void run() {
		Explain explain = new Explain(this._program.gene().toTreeNode(), 2.0);
		System.out.println("Welcome to GA");
		System.out.println("Trying to mutate "+ EXPR +". Lets see ....");

	    final Engine<ProgramGene<Double>, Double> engine = Engine
	        .builder(
	        		// fitness
	        		gene -> (10.0),
	        		// Math expression
	        		explain._program)
	        .populationSize(2)
	        .minimizing()
	        .alterers(
//	            new SingleNodeCrossover<>(0.1),
	            new Mutator<>(1))
	        .build();


	    final EvolutionResult<ProgramGene<Double>, Double> result = engine
	        .stream(() -> start(2, 1))
//	        .stream()
	        .limit(Limits.byFixedGeneration(1))
	        .collect(EvolutionResult.toBestEvolutionResult());

//	    final ProgramGene<Double> program1 = result.bestPhenotype()
//	        .genotype()
//	        .gene();
	    
	    final ProgramGene<Double> program1 = result.population().get(0)
		        .genotype()
		        .gene();
	    
	    final ProgramGene<Double> program2 = result.population().get(1)
		        .genotype()
		        .gene();

	    TreeNode<Op<Double>> tree = program1.toTreeNode();
	    MathExpr.rewrite(tree); // Simplify result program.
	    System.out.println("Generations: " + result.totalGenerations());
	    System.out.println("Population: " + result.population());
	    System.out.println("Function:    " + new MathExpr(tree));
	
	    tree = program2.toTreeNode();
	    MathExpr.rewrite(tree); // Simplify result program.
	    System.out.println("Generations: " + result.totalGenerations());
	    System.out.println("Population: " + result.population());
	    System.out.println("Function:    " + new MathExpr(tree));
	    
	    final Error<Double> error = Error.of(
	    	    LossFunction::mse,
	    	    Complexity.ofNodeCount(50)
	    	);
	}

	static final Codec<Tree<Op<Double>, ?>, ProgramGene<Double>> CODEC =
    Codec.of(
        Genotype.of(ProgramChromosome.of(
            // Program tree depth.
            5,
            // Chromosome validator.
            ch -> ch.root().size() <= 50,
            OPS,
            TMS
        )),
        Genotype::gene
    );
	
	private final Error<Double> _error;

	public double error(final Tree<? extends Op<Double>, ?> program) {
//		final Result<Double> result = _sampling.eval(program);
//		return result != null
//			? _error.apply(program, result.calculated(), result.expected())
//			: Double.MAX_VALUE;

		Double[] expected_result_list = {_expected_result};
		return _error.apply(program, expected_result_list, expected_result_list);
	}
}

