package nl.ou.refd.locations.streams;

import nl.ou.refd.analysis.subdetectors.Subdetector;
import nl.ou.refd.analysis.subdetectors.VariableSubdetectors;
import nl.ou.refd.locations.collections.LocationSet;
import nl.ou.refd.locations.collections.VariableSet;

/**
 * Class which represents a stream of variables. Much like a Java stream,
 * this stream chains a number of operations called subdetectors which operate
 * on these program locations. Through these subdetectors, the program locations
 * can be queried in this way.
 */
public class VariableStream extends Stream{

	/**
	 * Creates a stream of variables from a collection of variables.
	 * @param source the collection of variables
	 */
	public VariableStream(VariableSet source) {
		super(source);
	}
	
	/**
	 * Creates a stream from a previous stream and a next step in the stream, being
	 * the subdetector. The previous stream is not changed, and this stream works on
	 * a copy of that stream.
	 * @param precursor the previous stream
	 * @param next the next step in the chain within the stream
	 */
	protected VariableStream(Stream precursor, Subdetector next) {
		super(precursor, next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationSet collect() {
		return new VariableSet(this.locations());
	}
	
	/**
	 * Filters the contained variables by name.
	 * @param variableName the name of the variable
	 * @return a stream containing variables with name specified by variableName
	 */
	public VariableStream filterByName(String variableName) {
		return new VariableStream(this, new VariableSubdetectors.FilterByName(variableName));
	}
	
	/**
	 * Queries the methods the variables belong to.
	 * @return the classes the variables belong to
	 */
	public MethodStream parentMethods() {
		return new MethodStream(this, new VariableSubdetectors.ParentMethods());
	}

}
