package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.specifications.FieldSpecification;

/**
 * Class representing an Add Field microstep.
 */
public class AddField extends Microstep {
	
	private final FieldSpecification fieldToAdd;
	
	/**
	 * Creates the microstep with the provided field specification.
	 * @param fieldToAdd the FieldSpecification of the field to add
	 */
	public AddField(FieldSpecification fieldToAdd) {
		this.fieldToAdd = fieldToAdd;
		potentialRisk(new DoubleDefinition.Field(fieldToAdd));
	}

	@Override
	public void accept(ModelVisitor visitor) {
		visitor.visit(this);
		
	}

	@Override
	public void executeOnGraph(Graph graph) {
		this.fieldToAdd.construct(graph);
		
	}

}
