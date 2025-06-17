package nl.ou.refd.analysis;

import nl.ou.refd.analysis.microsteps.AddClass;
import nl.ou.refd.analysis.microsteps.AddMethod;
import nl.ou.refd.analysis.microsteps.MoveMethod;
import nl.ou.refd.analysis.microsteps.RemoveMethod;
import nl.ou.refd.analysis.microsteps.RenameMethod;

/**
 * Interface which represents a visitor for microsteps.
 */
public interface MicrostepVisitor {
	void visit(AddClass microstep);
	void visit(AddMethod microstep);
	void visit(RemoveMethod microstep);
	void visit(RenameMethod microstep);
	void visit(MoveMethod microstep);
}
