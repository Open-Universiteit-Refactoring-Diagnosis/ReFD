package nl.ou.refd.locations.specifications;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.jface.text.TextSelection;

import com.ensoftcorp.atlas.core.index.common.SourceCorrespondence;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

public class ExpressionSpecification extends LocationSpecification {
	
	//private ExpressionSpecification enclosingExpression;
	private Set<ProgramLocation> expression;
	private MethodSpecification enclosingMethod;
	
	public ExpressionSpecification(Set<ProgramLocation> expression, MethodSpecification enclosingMethod) {
		this.expression = expression;
		this.enclosingMethod = enclosingMethod;
	}
	
	public ExpressionSpecification(Set<ProgramLocation> locations, TextSelection selection) {
//		if (!locationIsExpression(location))
//			throw new IncompatibleProgramLocationException("Node not tagged with Tags.Node.DATA_FLOW");
		//this.enclosingExpression = new ExpressionSpecification(Graph.query(location).parent().singleLocation()); //Error: is a CONTROL_FLOW_NODE
		ProgramLocation methodLocation = Graph.query(locations)
				.containers()
				.locations(Tags.ProgramLocation.METHOD)
				.singleLocation();
		this.expression = getLocationsFromTextSelection(methodLocation, selection);
		this.enclosingMethod = new MethodSpecification(methodLocation);
	}

	/**
	 * Returns all the Program Locations that intersect with a text selection 
	 * @param method the program location of the parent method of the selection
	 * @param selection the selected text which contains the expression
	 * @return A set of ProgramLocation containing all intersecting locations with the selected text
	 */
	private Set<ProgramLocation> getLocationsFromTextSelection(ProgramLocation method, TextSelection selection) {
		Set<ProgramLocation> result = new HashSet<>();
		
		Set<ProgramLocation> childrenLocations = Graph.query(method).contained().locations();
		
		result = childrenLocations.stream().filter(location -> {
			SourceCorrespondence sc = (SourceCorrespondence)location.getAttribute(Tags.Attributes.SOURCE_CORRESPONDENCE);
			return (sc != null) && 
					(new SourceCorrespondence(
							sc.sourceFile, 
							selection.getOffset(), 
							selection.getLength()))
					.contains(sc);
			}).collect(Collectors.toSet());
		
		return result;
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
		return pl.taggedWith(Tags.ProgramLocation.DATAFLOW);  // TODO: change this to check all locations in expression
	}
	
	/**
	 * Returns the expression as a set of ProgramLocation.
	 * @return the expression as a set of ProgramLocation
	 */
	public Set<ProgramLocation> getExpression() {
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
