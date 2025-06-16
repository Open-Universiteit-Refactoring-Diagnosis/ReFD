package nl.ou.refd.analysis.refactorings;

import nl.ou.refd.analysis.DangerAggregator;
import nl.ou.refd.analysis.VerdictFunction;
import nl.ou.refd.locations.specifications.ExpressionSpecification;
import nl.ou.refd.locations.specifications.VariableSpecification;

public class ExtractVariable extends Refactoring {
	
	private final ExpressionSpecification target;
	
	public ExtractVariable(ExpressionSpecification target, VariableSpecification destination) {
		this.target = target;
	}

	@Override
	public VerdictFunction verdictFunction(DangerAggregator aggregator) {
		// TODO Auto-generated method stub
		return null;
	}

}
