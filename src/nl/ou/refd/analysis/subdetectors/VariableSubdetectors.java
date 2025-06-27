package nl.ou.refd.analysis.subdetectors;

import java.util.Set;

import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;

public final class VariableSubdetectors {
	private VariableSubdetectors(){}
	
	/**
	 * Filters the provided set of variable locations by name.
	 * @param variableName the name of the variable
	 * @return variable locations with name specified by variableName
	 */
	public static class FilterByName extends Subdetector {
		
		private final String variableName;
		
		public FilterByName(String variableName) {
			this.variableName = variableName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).variables(variableName).locations();
		}
	}

}
