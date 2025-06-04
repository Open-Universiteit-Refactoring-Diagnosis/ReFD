package nl.ou.refd.locations.specifications;

import nl.ou.refd.exceptions.IncompatibleProgramLocationException;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

public class ExpressionSpecification extends LocationSpecification {
	
	private ExpressionSpecification enclosingExpression;
	
	public ExpressionSpecification(ProgramLocation location) {
		if (!locationIsExpression(location))
			throw new IncompatibleProgramLocationException("Node not tagged with Tags.Node.DATA_FLOW");
		this.enclosingExpression = new ExpressionSpecification(Graph.query(location).parent().singleLocation());
	}

	@Override
	public LocationSpecification copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProgramLocation construct(Graph graph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean locationIsExpression(ProgramLocation pl) {
		return pl.taggedWith(Tags.ProgramLocation.DATAFLOW);
	}
	
	

}
