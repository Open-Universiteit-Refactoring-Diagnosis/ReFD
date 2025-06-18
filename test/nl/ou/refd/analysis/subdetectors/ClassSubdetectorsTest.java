package nl.ou.refd.analysis.subdetectors;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ensoftcorp.atlas.ui.util.ProjectImporterUtil;
import com.ensoftcorp.atlas.core.indexing.IndexStatus;
import com.ensoftcorp.atlas.core.indexing.IndexStatusUtil;
import com.ensoftcorp.atlas.core.licensing.AtlasLicenseException;

import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.ClassesByName;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.DirectSuperClasses;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;

public class ClassSubdetectorsTest {

	static final String TEST_PROJECT_NAME = "ReFDTestProject";
	static final String TEST_PACKAGE_NAME = "nl.ou.refd.test.analysis.subdetectors.classes";
	static final long MAPPING_TIMEOUT = 10;
	
	private static Set<ProgramLocation> querySpace = null;
	
	@BeforeAll
	static void initAll() { 
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(TEST_PROJECT_NAME);

		try {
			ProjectImporterUtil.mapProject(project);
			Awaitility.await()
				.atMost(MAPPING_TIMEOUT, TimeUnit.SECONDS)
				.until( () -> IndexStatusUtil.getIndexStatus().equals(IndexStatus.READY));
			
			querySpace = Graph.query()
						.universe()
						.relations(Tags.Relation.EDGE)
						.forward(Graph.query()
								.universe()
								.pkg(TEST_PACKAGE_NAME))
						.locations();
		} catch (AtlasLicenseException e) {
			System.out.println("Atlas Indexing failed. No valid license");
		} catch (ConditionTimeoutException e) {
			System.out.println("Atlas Indexing failed. Timeout occured");
		}	
	}
	
	@BeforeEach
	void init() { }
	
	@Test
	void givenExistingClassName_whenDetectClassesByName_thenReturnOneLocationWithSameName() {	
		// Arrange
		String name = "ClassANoSuper";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		Set<ProgramLocation> result = clsByName.applyOn(querySpace);
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result
				.iterator()
				.next()
				.<String>getAttribute(Tags.Attributes.NAME));
	}
	
	@Test
	void givenNonExistingClassName_whenDetectClassesByName_thenReturnEmpty() {
		// Arrange
		String name = "NonExistingClass";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		Set<ProgramLocation> result = clsByName.applyOn(querySpace);
		
		// Assert
		Assertions.assertEquals(0, result.size());
	}
	
	@Test
	void givenClassWithSuper_whenDetectDirectSuperClasses_thenReturnOneLocationOfDirectSuper() {
		// Arrange
		String subName = "ClassCExtendsB";
		String superName = "ClassBExtendsA";
		Set<ProgramLocation> subCls = new ClassesByName(subName).applyOn(querySpace);
		DirectSuperClasses superCls = new DirectSuperClasses();
		
		// Act
		Set<ProgramLocation> result = superCls.applyOn(subCls);
		
		// Assert
		Assertions.assertEquals(1,  result.size());
		Assertions.assertEquals(superName, result
				.iterator()
				.next()
				.<String>getAttribute(Tags.Attributes.NAME));
	}
	
	@Test
	void givenClassWithoutSuper_whenDetectDirectSuperClasses_thenReturnObject() {
		// Arrange
				String name = "ClassANoSuper";
				Set<ProgramLocation> cls = new ClassesByName(name).applyOn(querySpace);
				DirectSuperClasses superCls = new DirectSuperClasses();
				
				// Act
				Set<ProgramLocation> result = superCls.applyOn(cls);
				
				// Assert
				Assertions.assertEquals(1,  result.size());
				Assertions.assertEquals("Object", result.iterator().next().<String>getAttribute(Tags.Attributes.NAME));
	}
	
	@AfterEach
	void tearDown() { }
	
	@AfterAll
	static void tearDownAll() { }
}
