package lu.svv.theodore.explain;

import lu.svv.theodore.hls.And;
import lu.svv.theodore.hls.Expression;
import lu.svv.theodore.hls.Or;

public class Mutate {
	
	/**
	 * returns the number of records added to the file
	 * @param fsa
	 * @param inputFile
	 * @param outputFile
	 * @param signals
	 * @return
	 */
	public Expression mutate(Expression expression) {
		if (expression instanceof And) {
			Expression left = mutate(((And)expression).getLeft());
			Expression right = mutate(((And)expression).getRight());
			((And) expression).setOp("Or");
			((And) expression).setLeft(left);
			((And) expression).setRight(right);
		} else if (expression instanceof Or) {
			
		} 
		
		return expression;
	}
}