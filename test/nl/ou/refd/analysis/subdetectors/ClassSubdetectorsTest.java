package nl.ou.refd.analysis.subdetectors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ensoftcorp.atlas.core.indexing.IndexWorkspace;
import com.ensoftcorp.atlas.core.indexing.IMappingSettings;

import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.ClassesByName;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.locations.specifications.PackageSpecification;

public class ClassSubdetectorsTest {

	static class Settings implements IMappingSettings {
		Settings() { }
	}
	
	@BeforeAll
	static void initAll() { 
		Collection<IMappingSettings> settings = new HashSet<IMappingSettings>();
		settings.add(new Settings());
		IndexWorkspace.IndexWorkspaceJob job = new IndexWorkspace.IndexWorkspaceJob(settings);
		job.schedule();
		System.out.println("name: " + job.getName() + " state: " + job.getResult().toString());
	}
	
	@BeforeEach
	void init() { }
	
	@Test
	void givenExistingClassName_whenDetectClassesByName_thenReturnOneLocationWithSameName() {
		
		// Arrange
		String name = "LegacyEmployee";
		PackageSpecification pkg = new PackageSpecification("nl.ou.refd.mocks");
		ClassSpecification cls = new ClassSpecification(name, AccessModifier.PUBLIC, pkg);
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		Set<ProgramLocation> result = clsByName.applyOn(Graph.query().universe().locations());
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.iterator().next().<String>getAttribute(Tags.Attributes.NAME));
	}
	
	@AfterEach
	void tearDown() { }
	
	@AfterAll
	static void tearDownAll() { }
}
