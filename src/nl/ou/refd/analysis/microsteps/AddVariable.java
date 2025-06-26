package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.specifications.VariableSpecification;

public class AddVariable extends Microstep {

	private final VariableSpecification variableToAdd;
	
	public AddVariable(VariableSpecification variableToAdd) {
		this.variableToAdd = variableToAdd;
		
		potentialRisk(new DoubleDefinition.Variable(variableToAdd));
		// TODO add risk of shadowing of parameter
		// TODO add risk of shadowing of instance field
	}

	@Override
	public void accept(ModelVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeOnGraph(Graph graph) {
		// TODO Auto-generated method stub
		
	}
}
