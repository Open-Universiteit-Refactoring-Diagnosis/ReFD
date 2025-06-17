package nl.ou.refd.analysis.microsteps;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.BrokenLocalReferences;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.analysis.detectors.DoubleDefinition.Field;
import nl.ou.refd.locations.collections.FieldSet;
import nl.ou.refd.locations.collections.InstructionSet;
import nl.ou.refd.locations.collections.LocationSet;
import nl.ou.refd.locations.specifications.FieldSpecification;
import nl.ou.refd.locations.streams.FieldStream;
import nl.ou.refd.locations.streams.InstructionStream;
import nl.ou.refd.locations.generators.LocationGenerator;
import nl.ou.refd.locations.generators.ProgramComponentsGenerator;

/**
 * Class representing a Rename Field microstep (a composite microstep).
 * This class is named RenameFieldComposite to avoid confusion with the
 * RenameField refactoring it supports.
 */
public class RenameFieldComposite extends CompositeMicrostep {
	
	/**
	 * Create a new RenameFieldComposite microstep
	 * @param target the field that is to be renamed
	 * @param newName the new field name
	 */
	public RenameFieldComposite(FieldSpecification target, FieldSpecification newName) {
		
		potentialRisk(new DoubleDefinition.Field(newName));
		
		ProgramComponentsGenerator pcg = new ProgramComponentsGenerator();
		InstructionSet iset = new InstructionSet(pcg.generate());
		InstructionStream istream = new InstructionStream(iset);
		potentialRisk(new BrokenLocalReferences.Body(istream, target.getEnclosingClass()));
		
		compositeMicrostep(new RemoveField(target.getEnclosingClass()));
		compositeMicrostep(new AddField(newName));		
	}

	@Override
	public void accept(ModelVisitor visitor) {
		visitor.visit(this);		
	}

}
