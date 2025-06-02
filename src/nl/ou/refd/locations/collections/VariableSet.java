package nl.ou.refd.locations.collections;

import java.util.Set;

import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.streams.Stream;
import nl.ou.refd.locations.streams.VariableStream;

/**
 * Class representing a set of variable locations that can be streamed
 * with the specialized variable location streams.
 */
public class VariableSet extends LocationSet {

	/**
	 * Creates the variable set from a set of program locations.
	 * @param locations the set of program locations
	 */
	public VariableSet(Set<ProgramLocation> locations) {
		super(locations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream stream() {
		return new VariableStream(this);
	}
	
	

}
