package nl.ou.refd.analysis.detectors;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ensoftcorp.atlas.core.indexing.IndexStatus;
import com.ensoftcorp.atlas.core.indexing.IndexStatusUtil;
import com.ensoftcorp.atlas.core.licensing.AtlasLicenseException;
import com.ensoftcorp.atlas.ui.util.ProjectImporterUtil;

import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.locations.specifications.PackageSpecification;

public class DoubleDefinitionTest {

	static final String TEST_PROJECT_NAME = "ReFDTestProject";
	static final String TEST_PACKAGE_NAME = "nl.ou.refd.test.analysis.detectors.doubledefinition";
	static final long MAPPING_TIMEOUT = 10;
	
	private static PackageSpecification pkg = new PackageSpecification(TEST_PACKAGE_NAME);
	
	@BeforeAll
	static void initAll() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(TEST_PROJECT_NAME);

		try {
			ProjectImporterUtil.mapProject(project);
			Awaitility.await()
				.atMost(MAPPING_TIMEOUT, TimeUnit.SECONDS)
				.until( () -> IndexStatusUtil.getIndexStatus().equals(IndexStatus.READY));

		} catch (AtlasLicenseException e) {
			System.out.println("Atlas Indexing failed. No valid license");
		} catch (ConditionTimeoutException e) {
			System.out.println("Atlas Indexing failed. Timeout occured");
		}
	}
	
	@BeforeEach
	void init() { }
	
	
	@Test
	void givenExistingClassName_whenDetectClass_thenActualRisk() {
		// Arrange
		String name = "ClassA";
		ClassSpecification clsSpec = new ClassSpecification(name, AccessModifier.PUBLIC, pkg);
		DoubleDefinition.Class ddClass = new DoubleDefinition.Class(clsSpec);
		
		// Act
		List<String> result = ddClass.actualRisks()
				.locations()
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
	}
	
	
	@AfterEach
	void tearDown() { }
	
	@AfterAll
	static void tearDownAll() { }
}
