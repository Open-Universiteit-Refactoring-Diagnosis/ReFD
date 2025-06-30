package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.locations.collections.InstructionSet;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.generators.ProgramComponentsGenerator;
import nl.ou.refd.locations.specifications.FieldSpecification;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.streams.ClassStream;
import nl.ou.refd.locations.streams.FieldStream;
import nl.ou.refd.locations.streams.InstructionStream;

/**
 * A collection of classes which represent MissingDefinition detectors,
 * each with its own type of context.
 */
public final class MissingDefinition {
	private MissingDefinition(){}

	/**
	 * Class representing a MissingDefinition detector for a method.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Method extends Detector<InstructionSet> {

		private final MethodSpecification subject;

		/**
		 * Creates the detector with its context.
		 * @param subject the context
		 */
		public Method(MethodSpecification subject) {
			this.subject = subject;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public InstructionSet actualRisks() {
			return new MethodSet(subject).stream().methodsCalledAt().collect();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void accept(DetectorVisitor visitor) {
			visitor.visit(this);
		}

	}

	/**
	 * Class representing a MissingDefinition detector for a method.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Field extends Detector<InstructionSet> {

		private final FieldSpecification subject;

		/**
		 * Creates the detector with its context.
		 * @param subject the context
		 */
		public Field(FieldSpecification subject) {
			this.subject = subject;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public InstructionSet actualRisks() {
			ClassStream context = new ProgramComponentsGenerator()
					.stream()
					.classes()
					.classesByName(subject.getEnclosingClass().getClassName());
			context = context.unionWithClasses(context.allSubclasses());			
			return context
					.fields()
					.filterByName(subject.getFieldName())
					.fieldsCalledAt()
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
