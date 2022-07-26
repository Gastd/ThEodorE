package lu.svv.theodore.explain;

import io.jenetics.BitChromosome;
import lu.svv.theodore.explain.Explain;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;
 
public class HelloWorld {
    // 2.) Definition of the fitness function.
    private static int eval(Genotype<BitGene> gt) {
        return gt.chromosome()
            .as(BitChromosome.class)
            .bitCount();
    }
 
    public static void main(String[] args) {
    	Explain explain = Explain.of("x+3", 2.0);
    	explain.run();
    }
}