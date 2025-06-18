package nl.ou.refd.analysis.subdetectors;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import org.hamcrest.Matchers;
import org.awaitility.Awaitility;

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
	
	@BeforeAll
	static void initAll() { 
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(TEST_PROJECT_NAME);

		try {
			ProjectImporterUtil.mapProject(project);
			Awaitility.await()
				.atMost(10, TimeUnit.SECONDS)
				.pollDelay(100, TimeUnit.MILLISECONDS)
				.until( () -> IndexStatusUtil.getIndexStatus(), Matchers.equalTo(IndexStatus.READY));
		} catch (AtlasLicenseException e) {
			System.out.println("Indexing failed. No valid license");
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
		Set<ProgramLocation> result = clsByName.applyOn(Graph.query()
				.universe()
				.locations());
		
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
		Set<ProgramLocation> result = clsByName.applyOn(Graph.query()
				.universe()
				.locations());
		
		// Assert
		Assertions.assertEquals(0, result.size());
	}
	
	@Test
	void givenClassWithSuper_whenDetectDirectSuperClasses_thenReturnOneLocationOfDirectSuper() {
		// Arrange
		String subName = "ClassCExtendsB";
		String superName = "ClassBExtendsA";
		Set<ProgramLocation> subCls = new ClassesByName(subName).applyOn(Graph.query()
				.universe()
				.locations());
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
				Set<ProgramLocation> cls = new ClassesByName(name).applyOn(Graph.query()
						.universe()
						.locations());
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
