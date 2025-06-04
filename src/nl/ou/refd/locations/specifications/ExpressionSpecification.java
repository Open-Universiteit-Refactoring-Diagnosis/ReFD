package nl.ou.refd.locations.specifications;

import nl.ou.refd.exceptions.IncompatibleProgramLocationException;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

public class ExpressionSpecification extends LocationSpecification {
	
	//private ExpressionSpecification enclosingExpression;
	private String expression;
	private MethodSpecification enclosingMethod;
	
	public ExpressionSpecification(String expression, MethodSpecification enclosingMethod) {
		this.expression = expression;
		this.enclosingMethod = enclosingMethod;
	}
	
	public ExpressionSpecification(ProgramLocation location) {
		if (!locationIsExpression(location))
			throw new IncompatibleProgramLocationException("Node not tagged with Tags.Node.DATA_FLOW");
		//this.enclosingExpression = new ExpressionSpecification(Graph.query(location).parent().singleLocation()); //Error: is a CONTROL_FLOW_NODE
		this.expression = null; // TODO: get the expression from the location
		this.enclosingMethod = new MethodSpecification(Graph.query(location)
				.containers()
				.locations(Tags.ProgramLocation.METHOD)
				.singleLocation());
	}

	@Override
	public LocationSpecification copy() {
		return new ExpressionSpecification(this.expression, enclosingMethod.copy());
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
	
	/**
	 * Returns the enclosing method as an object. This object
	 * is mutable.
	 * @return the enclosing method as MethodSpecification
	 */
	public MethodSpecification getEnclosingMethod() {
		return enclosingMethod;
	}

}
