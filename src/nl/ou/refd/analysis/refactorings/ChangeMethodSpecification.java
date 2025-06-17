package nl.ou.refd.analysis.refactorings;

import nl.ou.refd.analysis.DangerAggregator;
import nl.ou.refd.analysis.VerdictFunction;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.analysis.microsteps.RenameMethod;
import nl.ou.refd.locations.specifications.MethodSpecification;

public class ChangeMethodSpecification extends Refactoring {
	private final MethodSpecification target;
	
	/**
	 * Creates the Change Method Specification refactoring.
	 * This refactoring can be analysed by using a DangerAnalyser object.
	 * @param target The method that should get a new specification.
	 * @param newSpecification The new method specification.
	 */
	public ChangeMethodSpecification(MethodSpecification target, MethodSpecification newSpecification) {
		this.target = target;
		
		microstep(new RenameMethod(target, newSpecification));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VerdictFunction verdictFunction(DangerAggregator aggregator) {
		return new VerdictFunction(aggregator) {
			@Override
			public void visit(DoubleDefinition.Method detector) {
				all(detector);
			}
		};
	}

}
