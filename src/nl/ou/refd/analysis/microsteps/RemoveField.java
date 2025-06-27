package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.MissingDefinition;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.specifications.FieldSpecification;

/**
 * Class representing a Remove Field microstep
 */
public class RemoveField extends Microstep {
	
	private final FieldSpecification fieldToRemove;
	
	/**
	 * Create the Remove Field microstep.
	 * @param fieldToRemove a FieldSpecification of the field to be removed
	 */
	public RemoveField(FieldSpecification fieldToRemove) {
		this.fieldToRemove = fieldToRemove;
		potentialRisk(new MissingDefinition.Field(fieldToRemove));
	}

	@Override
	public void accept(ModelVisitor visitor) {
		visitor.visit(this);
		
	}

	@Override
	public void executeOnGraph(Graph graph) {
		ProgramLocation pl = fieldToRemove.construct(graph);
		graph.removeProgramLocation(pl);	
	}

}
