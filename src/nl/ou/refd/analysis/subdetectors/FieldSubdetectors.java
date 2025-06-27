package nl.ou.refd.analysis.subdetectors;

import java.util.Arrays;
import java.util.Set;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.locations.streams.FieldStream;

/**
 * Class containing all subdetectors that work on sets of field locations.
 */
public final class FieldSubdetectors {
	private FieldSubdetectors(){}
	
	/**
	 * Filters provided set of fields for fields belonging to instances of classes.
	 * @return the fields contained within the provided set that belong to instances of classes
	 */
	public static class InstanceFields extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).locations(Tags.ProgramLocation.INSTANCE_VARIABLE).locations();
		}
	}

	/**
	 * Filters provided set of fields for static fields belonging to classes.
	 * @return the static fields contained within the provided set that belong to classes
	 */
	public static class StaticFields extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).locations(Tags.ProgramLocation.CLASS_VARIABLE).locations();
		}
	}

	/**
	 * Queries the locations in the codebase where the provided field locations are called.
	 * @return the callsites of the provided field locations
	 */
	public static class FieldsCalledAt extends Subdetector {
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			GraphQuery gq = Graph.query(locations);
			return gq.successorsOn(gq.universe().relations(Tags.Relation.DATAFLOW)).locations();
		}
	}

	/**
	 * Filters the provided set of field locations by name.
	 * @param fieldName the name of the field
	 * @return field locations with name specified by fieldName
	 */
	public static class FilterByName extends Subdetector {
		
		private final String fieldName;
		
		public FilterByName(String fieldName) {
			this.fieldName = fieldName;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).fields(fieldName).locations();
		}
	}

	/**
	 * Queries the classes the provided field locations belong to.
	 * @return the classes the provided field locations belong to
	 */
	public static class ParentClasses extends Subdetector {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).parent().locations();
		}
	}
	
	/**
	 * Filters the provided set of field locations and keeps only fields also contained
	 * within incoming streams intersectWith.
	 * @param intersectWith the streams of fields to keep
	 * @return the filtered set of field locations
	 */
	public static class IntersectionWithFields extends Subdetector {
		
		private final FieldStream[] intersectWith;
		
		public IntersectionWithFields(FieldStream... intersectWith) {
			this.intersectWith = intersectWith;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).intersection(streamVarArgToGraphQueryArray(intersectWith)).locations();
		}
	}
	
	/**
	 * Filters the provided set of field locations by any of the provided access modifiers
	 * @param one or more access modifiers to filter by
	 * @return field locations with any of the provided access modifiers
	 */
	public static class FilterByAccess extends Subdetector {
		
		private final AccessModifier[] modifiers;
		 
		public FilterByAccess(AccessModifier... modifiers) {
			this.modifiers = modifiers;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations)
					.locations(Arrays.stream(modifiers)
							.map(AccessModifier::toTag)
							.toArray(Tags.ProgramLocation[]::new))
					.locations();
		}
	}
	
	/**
	 * Integrates incoming streams of fields unionWith into the provided
	 * set of fields, removing duplicates.
	 * @param unionWith fields to add to current set, removing duplicates
	 * @return the result of integrating both sets
	 */
	public static class Union extends Subdetector {
		
		private final FieldStream[] unionWith;
		
		public Union(FieldStream... unionWith) {
			this.unionWith = unionWith;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Set<ProgramLocation> applyOn(Set<ProgramLocation> locations) {
			return Graph.query(locations).union(streamVarArgToGraphQueryArray(unionWith)).locations();
		}
	}
}
