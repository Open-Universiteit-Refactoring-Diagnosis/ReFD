package nl.ou.refd.analysis.microsteps;

import java.util.HashSet;
import java.util.Set;

import nl.ou.refd.analysis.ModelVisitor;
import nl.ou.refd.analysis.detectors.DoubleDefinition;
import nl.ou.refd.analysis.subdetectors.MethodSubdetectors;
import nl.ou.refd.analysis.subdetectors.MethodSubdetectors.MethodsCalledAt;
import nl.ou.refd.locations.collections.MethodSet;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.MethodSpecification;

/**
 * Class representing a RenameMethod microstep, an altering operation on a codebase
 * which renames a method. A microstep contains a number of detectors which
 * detect possible dangers (potential risks) when the microstep would be executed
 * on the codebase. The microstep can also be executed on the model of the
 * codebase to simulate its execution.
 */
public class RenameMethod extends Microstep {
	private final MethodSpecification methodToRename;
	private final MethodSpecification methodRenamed;
	
	/**
	 * Creates the microstep with specification of the method to rename.
	 * @param methodToRename The specification of the method to rename.
	 * @param methodRenamed The new specification of the method that contains the new name.
	 */
	public RenameMethod(MethodSpecification methodToRename, MethodSpecification methodRenamed) {
		this.methodToRename = methodToRename;
		this.methodRenamed = methodRenamed;
		
		potentialRisk(new DoubleDefinition.Method(this.methodRenamed));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(ModelVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeOnGraph(Graph graph) {
		// Find all references to the target method.
		GraphQuery gq = graph.query(new MethodSet(this.methodToRename).locations());
		GraphQuery dynamicCallSites =
				gq
					.universe()
					.locations(Tags.ProgramLocation.IDENTITY)
					.predecessorsOn(gq.universe().relations(Tags.Relation.DATAFLOW))
					.locations(Tags.ProgramLocation.IDENTITY_PASS)
					.successorsOn(gq.universe().relations(Tags.Relation.IDENTITY_PASSED_TO));
					
		GraphQuery invokedSignature = gq.predecessorsOn(gq.universe().relations(Tags.Relation.INVOKED_SIGNATURE));
		GraphQuery staticCallSites = gq.predecessorsOn(gq.universe().relations(Tags.Relation.INVOKED_FUNCTION));
		Set<ProgramLocation> callSites = dynamicCallSites.union(staticCallSites, invokedSignature).locations();
		
		// Rename all references to the target method.
		for (ProgramLocation callSite : callSites) {
			System.out.print(callSite);
		}
		
		
	}
}
