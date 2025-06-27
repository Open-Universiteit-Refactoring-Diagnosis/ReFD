package nl.ou.refd.analysis.detectors;

import nl.ou.refd.analysis.DetectorVisitor;
import nl.ou.refd.locations.collections.FieldSet;
import nl.ou.refd.locations.generators.ProgramComponentsGenerator;
import nl.ou.refd.locations.specifications.VariableSpecification;
import nl.ou.refd.locations.streams.ClassStream;

public class ScopeShadowing {
	private ScopeShadowing() {}

	
	public static class Field extends Detector<FieldSet> {
		
		private final VariableSpecification subject;
		
		public Field(VariableSpecification subject) {
			this.subject = subject;
		}

		@Override
		public FieldSet actualRisks() {
			ClassStream localClass = new ProgramComponentsGenerator()
					.stream()
					.classes()
					.classesByName(subject.getEnclosingMethod().getEnclosingClass().getClassName());
			ClassStream fullContext = localClass.unionWithClasses(localClass.allSuperClasses());
			
			return fullContext.fields()
					.filterByName(subject.getName())
					.collect();
		}

		@Override
		public void accept(DetectorVisitor visitor) {
			visitor.visit(this);
		}
	}
}
