package nl.ou.refd.analysis.subdetectors;

import java.util.Set;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

public final class VariableSubdetectors {
	private VariableSubdetectors(){}
	
	public static class MethodVariables extends Subdetector {

		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			
			return Graph.query(locations).locations(Tags.ProgramLocation.VARIABLE).locations();
		}
		
	}

}
