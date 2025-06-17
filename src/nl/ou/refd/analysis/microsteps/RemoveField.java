package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.BrokenLocalReferences;
import nl.ou.refd.locations.collections.InstructionSet;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.FieldSpecification;
import nl.ou.refd.locations.streams.InstructionStream;

public class RemoveField extends Microstep {
	
	private final ClassSpecification enclosingClass;
	
	public RemoveField(ClassSpecification enclosingClass) {
		this.enclosingClass = enclosingClass;
		
		// TODO: potential risk
	}

	@Override
	public void accept(ModelVisitor visitor) {
		visitor.visit(this);
		
	}

	@Override
	public void executeOnGraph(Graph graph) {
		// TODO Auto-generated method stub
		
	}

}
