package nl.ou.refd.locations.specifications;

import org.apache.commons.lang3.NotImplementedException;

import nl.ou.refd.exceptions.IncompatibleProgramLocationException;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

/**
 * Class representing a specification of a variable location
 * in a codebase. The specification can be of either a location that
 * already exists within the codebase, or that does not exists (yet).
 */
public class VariableSpecification extends LocationSpecification {
	
	private MethodSpecification enclosingMethod;
	private String name;
	private String type;
	
	/**
	 * Creates a variable location specification 
	 * @param name the name of the variable
	 * @param type the type of the variable as String
	 * @param enclosingMethod the enclosing method the variable is declared in
	 */
	public VariableSpecification(String name, String type, MethodSpecification enclosingMethod) {
		this.name = name;
		this.type = type;
		this.enclosingMethod = enclosingMethod;
	}
	
	/**
	 * Creates a variable location specification from a ProgramLocation (Graph Node). This
	 * value is not stored inside the MethodSpecification. Rather, it is used to get the
	 * relevant data to create the ProgramLocation.
	 * @param location the ProgramLocation containing the relevant data to create the VariableSpecification
	 * @throws IncompatibleProgramLocationException if the provided ProgramLocation is not a valid variable
	 */
	public VariableSpecification(ProgramLocation location) {
		if (!locationIsVariable(location))
			throw new IncompatibleProgramLocationException("Node not tagged with Tags.Node.VARIABLE");
		this.name = location.<String>getAttribute(Tags.Attributes.NAME);
		this.type = null; //TODO: retrieve type from a ProgramLocation
		this.enclosingMethod = new MethodSpecification(Graph.query(location).parent().singleLocation());
	}
	
	/**
	 * Checks if the provided ProgramLocation is tagged as a variable.
	 * @param pl the ProgramLocation to check
	 * @return true if ProgramLocation instance is a variable, false otherwise
	 */
	public static boolean locationIsVariable(ProgramLocation pl) {
		return pl.taggedWith(Tags.ProgramLocation.VARIABLE);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VariableSpecification copy() {
		return new VariableSpecification(name, type, enclosingMethod.copy());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.enclosingMethod.getEnclosingClass().toString() + "." 
				+ this.getEnclosingMethod().toString() + "." 
				+ this.name + ":"
				+ this.type;
	}
	
	/**
	 * Returns an immutable String representing the variable's name.
	 * @return the name of the variable as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the variable's name.
	 * @param name the new name of the variable
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns an immutable String representing the variable's type.
	 * @return the type of the variable as a string
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * Sets the variable's type.
	 * @param type the new type of the variable
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the enclosing method as an object. This object
	 * is mutable.
	 * @return the enclosing method as MethodSpecification
	 */
	public MethodSpecification getEnclosingMethod() {
		return enclosingMethod;
	}

	/**
	 * Sets the enclosing method.
	 * @param enclosingMethod the new enclosing method
	 */
	public void setEnclosingMethod(MethodSpecification enclosingMethod) {
		this.enclosingMethod = enclosingMethod;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramLocation construct(Graph graph) {
		throw new NotImplementedException(); //TODO: construct variable as ProgramLocation to insert into the Graph
	}
}
