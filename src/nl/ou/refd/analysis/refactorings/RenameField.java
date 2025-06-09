package nl.ou.refd.analysis.refactorings;

import nl.ou.refd.analysis.DangerAggregator;
import nl.ou.refd.analysis.VerdictFunction;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.analysis.microsteps.AddField;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.FieldSpecification;

public class RenameField extends Refactoring {
	
	private final FieldSpecification target;
	private final FieldSpecification replacement;
	
	public RenameField(FieldSpecification target, FieldSpecification replacement) {
		this.target = target;
		this.replacement = replacement;
		microstep(new AddField(replacement));
	}

	@Override
	public VerdictFunction verdictFunction(DangerAggregator aggregator) {
		return new VerdictFunction(aggregator) {
			
			public void visit(DoubleDefinition.Field detector) {
				all(detector);
			}
		};
	}

}
