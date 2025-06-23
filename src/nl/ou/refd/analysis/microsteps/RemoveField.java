package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.BrokenLocalReferences;
import nl.ou.refd.locations.collections.FieldSet;
import nl.ou.refd.locations.collections.InstructionSet;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.FieldSpecification;
import nl.ou.refd.locations.streams.InstructionStream;

public class RemoveField extends Microstep {
	
	private final FieldSpecification fieldToRemove;
	
	public RemoveField(FieldSpecification fieldToRemove) {
		this.fieldToRemove = fieldToRemove;
		potentialRisk(new BrokenLocalReferences.Field(fieldToRemove));
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
