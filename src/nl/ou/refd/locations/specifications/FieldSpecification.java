package nl.ou.refd.locations.specifications;

import org.apache.commons.lang3.NotImplementedException;


import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;

/**
 * Class representing a specification of a single field location
 * in a codebase. The specification can be of either a location that
 * already exists within the codebase, or that does not exists (yet).
 */
public class FieldSpecification extends LocationSpecification {
	
	private String fieldName;
	private ClassSpecification enclosingClass;
	private AccessModifier visibility;
	private boolean isStatic;
	
	/**
	 * Creates a field location specification from a field name and
	 * a specification of a class the field belongs to.
	 * @param fieldName the name of the field
	 * @param enclosingClass the class the field belongs to
	 */
	public FieldSpecification(String fieldName, ClassSpecification enclosingClass, AccessModifier visibility, boolean isStatic) {
		this.fieldName = fieldName;
		this.enclosingClass = enclosingClass;
		this.visibility = visibility;
		this.isStatic = isStatic;
	}
	
	public FieldSpecification(ProgramLocation pl) {
		GraphQuery nQ = Graph.query(pl);
		this.fieldName = pl.<String>getAttribute(Tags.Attributes.NAME);
		this.visibility = AccessModifier.fromTaggedLocation(pl);
		
		this.isStatic = pl.taggedWith(Tags.ProgramLocation.CLASS_VARIABLE);		
		this.enclosingClass = new ClassSpecification(Graph.query(pl).parent().singleLocation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldSpecification copy() {
		return new FieldSpecification(fieldName, enclosingClass, visibility, isStatic);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.fieldName;
	}
	
	/**
	 * Returns an immutable String representing the field name.
	 * @return the name of the class as a string
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Sets the field's name.
	 * @param fieldName the new name of the field
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Returns the enclosing class as an object. This object
	 * is mutable.
	 * @return the enclosing package as ClassSpecification
	 */
	public ClassSpecification getEnclosingClass() {
		return enclosingClass;
	}

	/**
	 * Sets the enclosing class.
	 * @param enclosingClass the new enclosing class
	 */
	public void setEnclosingClass(ClassSpecification enclosingClass) {
		this.enclosingClass = enclosingClass;
	}
	
	public static boolean locationIsField(ProgramLocation pl) {
		return (pl.taggedWith(Tags.ProgramLocation.FIELD) || pl.taggedWith(Tags.ProgramLocation.CLASS_VARIABLE) ||
				pl.taggedWith(Tags.ProgramLocation.INSTANCE_VARIABLE));
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public AccessModifier getVisibility() {
		return visibility;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgramLocation construct(Graph graph) {
		ProgramLocation parentClassLocation = new nl.ou.refd.locations.collections.ClassSet(this.getEnclosingClass()).singleLocation();
		
		ProgramLocation nField = createField(graph);
		
		if (this.isStatic()) {
			nField.tag(Tags.ProgramLocation.CLASS_VARIABLE);
		}
		else {
			nField.tag(Tags.ProgramLocation.INSTANCE_VARIABLE);
		}
		
		if (this.getVisibility() == AccessModifier.PUBLIC) {
			nField.tag(Tags.ProgramLocation.PUBLIC_VISIBILITY);
		}
		else if (this.getVisibility() == AccessModifier.PACKAGE) {
			nField.tag(Tags.ProgramLocation.PACKAGE_VISIBILITY);
		}
		else if (this.getVisibility() == AccessModifier.PROTECTED) {
			nField.tag(Tags.ProgramLocation.PROTECTED_PACKAGE_VISIBILITY);
		}
		else if (this.getVisibility() == AccessModifier.PRIVATE) {
			nField.tag(Tags.ProgramLocation.PRIVATE_VISIBILITY);
		}
		
		nField.putAttribute(Tags.Attributes.NAME, this.getFieldName());
		return nField;
	}
	
	private static ProgramLocation createField(Graph graph) {
		ProgramLocation node = graph.createProgramLocation();
		node.tag(Tags.ProgramLocation.FIELD);
		return node;
	}

}
