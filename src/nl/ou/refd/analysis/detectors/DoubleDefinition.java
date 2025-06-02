package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.locations.collections.ClassSet;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.collections.VariableSet;
import nl.ou.refd.locations.generators.ProgramComponentsGenerator;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.specifications.VariableSpecification;

/**
 * A collection of classes which represent DoubleDefinition detectors,
 * each with its own type of context.
 */
public final class DoubleDefinition {
	private DoubleDefinition(){}
	
	/**
	 * Class representing a DoubleDefinition detector for a class.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Class extends Detector<ClassSet> {
		
		private final ClassSpecification subject;
		
		/**
		 * Creates the detector with its context.
		 * @param subject the context
		 */
		public Class(ClassSpecification subject) {
			this.subject = subject;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public ClassSet actualRisks() {
			return new ProgramComponentsGenerator()
					.stream()
					.classes()
					.classesByName(subject.getClassName())
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
	
	/**
	 * Class representing a DoubleDefinition detector for a method.
	 * A detector checks the program graph for potential risks. If it finds
	 * these, they are determined to be actual risks.
	 */
	public static class Method extends Detector<MethodSet> {
		
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
		public MethodSet actualRisks() {
			return new ProgramComponentsGenerator()
					.stream()
					.classes()
					.classesByName(subject.getEnclosingClass().getClassName())
					.methods()
					.methodsWithSignature(subject.getMethodName(), subject.getParameterTypes())
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
	
	public static class Variable extends Detector<VariableSet> {

		private final VariableSpecification subject;
		
		public Variable(VariableSpecification subject) {
			this.subject = subject;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public VariableSet actualRisks() {
			return new ProgramComponentsGenerator()
					.stream()
					.classes()
					.classesByName(subject.getEnclosingMethod().getEnclosingClass().getClassName())
					.methods()
					.methodsWithSignature(
							subject.getEnclosingMethod().getMethodName(), 
							subject.getEnclosingMethod().getParameterTypes())
					.variables()
					.filterByName(subject.getName())
					.collect();
		}

		@Override
		public void accept(DetectorVisitor visitor) {
			visitor.visit(this);
		}
		
	}
	
}
