package nl.ou.refd.analysis.refactorings;

import nl.ou.refd.analysis.DangerAggregator;
import nl.ou.refd.analysis.VerdictFunction;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.analysis.detectors.MissingDefinition;
import nl.ou.refd.analysis.microsteps.RenameFieldComposite;
import nl.ou.refd.locations.specifications.FieldSpecification;

/**
 * Class representing a Rename Field refactoring. This refactoring can be analyzed by
 * using a DangerAnalyzer object.
 */
public class RenameField extends Refactoring {
	
	/**
	 * Creates the Rename Field refactoring with the target field to be renamed
	 * and the replacement that it should be renamed to.
	 * @param target field that should be renamed
	 * @param replacement the field that the target should be renamed to
	 */	
	public RenameField(FieldSpecification target, FieldSpecification replacement) {
		microstep(new RenameFieldComposite(target, replacement));
	}
	
	public VerdictFunction verdictFunction(DangerAggregator aggregator) {
		return new VerdictFunction(aggregator) {
			
			@Override
			public void visit(DoubleDefinition.Field detector) {
				all(detector);
			}
			

			@Override
			public void visit(MissingDefinition.Field detector) {
				all(detector);				
			}
		};
	}

}
