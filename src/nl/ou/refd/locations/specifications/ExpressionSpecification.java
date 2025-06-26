package nl.ou.refd.locations.specifications;

import org.apache.commons.lang3.NotImplementedException;

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
//		if (!locationIsExpression(location))
//			throw new IncompatibleProgramLocationException("Node not tagged with Tags.Node.DATA_FLOW");
		//this.enclosingExpression = new ExpressionSpecification(Graph.query(location).parent().singleLocation()); //Error: is a CONTROL_FLOW_NODE
		this.expression = location.<String>getAttribute(Tags.Attributes.NAME);
		this.enclosingMethod = new MethodSpecification(Graph.query(location)
				.containers()
				.locations(Tags.ProgramLocation.METHOD)
				.singleLocation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationSpecification copy() {
		return new ExpressionSpecification(this.expression, enclosingMethod.copy());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramLocation construct(Graph graph) {
		throw new NotImplementedException(); //TODO: construct expression as ProgramLocation to insert into the Graph
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.enclosingMethod.getEnclosingClass().toString() + "." 
				+ this.getEnclosingMethod().toString()
				+ " [" + this.expression + "]";
	}

	/**
	 * Checks if the provided ProgramLocation is tagged as a DATAFLOW node.
	 * @param pl the program location to check
	 * @return true if the program location is an expression
	 */
	public static boolean locationIsExpression(ProgramLocation pl) {
		return pl.taggedWith(Tags.ProgramLocation.DATAFLOW);
	}
	
	/**
	 * Returns the expression as a String.
	 * @return the expression as String
	 */
	public String getExpression() {
		return this.expression;
	}
	
	/**
	 * Returns the enclosing method as an object. This object
	 * is mutable.
	 * @return the enclosing method as MethodSpecification
	 */
	public MethodSpecification getEnclosingMethod() {
		return this.enclosingMethod;
	}

}
