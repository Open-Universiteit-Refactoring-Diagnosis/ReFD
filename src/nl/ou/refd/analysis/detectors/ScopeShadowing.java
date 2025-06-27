package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.locations.collections.FieldSet;
import nl.ou.refd.locations.generators.ProgramComponentsGenerator;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.locations.specifications.VariableSpecification;
import nl.ou.refd.locations.streams.ClassStream;

/**
 * A collection of classes which represent ScopeShadowing detectors,
 * each with its own type of context.
 */
public class ScopeShadowing {
	private ScopeShadowing() {}

	/**
	 * Class representing a ScopeShadowing detector for fields.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Field extends Detector<FieldSet> {
		
		private final VariableSpecification subject;
		
		/**
		 * Creates the detector with its variable context.
		 * @param subject the variable context
		 */
		public Field(VariableSpecification subject) {
			this.subject = subject;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public FieldSet actualRisks() {
			ClassStream localClass = new ProgramComponentsGenerator()
					.stream()
					.classes()
					.classesByName(subject.getEnclosingMethod().getEnclosingClass().getClassName());
			
			return localClass.fields()
					.instanceFields()  // TODO check static fields for variables declared in static methods
					.filterByName(subject.getName())
					.union(localClass.allSuperClasses()
							.fields()
							.instanceFields()
							.filterByName(subject.getName())
							.filterByAccess(AccessModifier.PACKAGE, AccessModifier.PUBLIC, AccessModifier.PROTECTED))
					.collect();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void accept(DetectorVisitor visitor) {
			visitor.visit(this);
		}
	}
}
