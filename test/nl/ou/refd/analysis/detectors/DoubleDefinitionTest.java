package nl.ou.refd.analysis.detectors;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.specifications.PackageSpecification;
import nl.ou.refd.locations.specifications.ParameterSpecification;

public class DoubleDefinitionTest {

	static final String TEST_PROJECT_NAME = "ReFDTestProject";
	static final String TEST_PACKAGE_NAME = "nl.ou.refd.test.analysis.detectors.doubledefinition";
	static final long MAPPING_TIMEOUT = 10;
	
	private final static PackageSpecification pkg = new PackageSpecification(TEST_PACKAGE_NAME);
	
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
	
	/**
	 * TODO: Detector disregards package scope. Check reason.
	 */
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
	
	@Test
	void givenNonExistingClassName_whenDetectClass_thenNoRisk() {
		// Arrange
		String name = "NonExisitingClass";
		ClassSpecification clsSpec = new ClassSpecification(name, AccessModifier.PUBLIC, pkg);
		DoubleDefinition.Class ddClass = new DoubleDefinition.Class(clsSpec);
		
		// Act
		Set<ProgramLocation> result = ddClass.actualRisks().locations();
		
		// Assert
		Assertions.assertEquals(0, result.size());
	}
	
	@Test
	void givenExistingMethodNoParams_whenDetectMethod_thenActualRisk() {
		// Arrange
		String clsName = "ClassA";
		String name = "methodA1";
		ClassSpecification clsSpec = new ClassSpecification(clsName, AccessModifier.PUBLIC, pkg);
		MethodSpecification mSpec = new MethodSpecification(
				name, List.of(), AccessModifier.PUBLIC, false, false, "void", clsSpec);
		DoubleDefinition.Method ddMethod = new DoubleDefinition.Method(mSpec);
		
		// Act
		List<String> result = ddMethod.actualRisks()
				.locations()
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
	}
	
	/**
	 * TODO: Using non-primitive param or return types interfere with tests. 
	 * i.e. String incorporates other Classes into the code Graph as well.
	 * Only use primitives or types from the test project.
	 */
	@Test
	void givenExistingMethodWithParams_whenDetectMethod_thenActualRisk() {
		// Arrange
		String clsName = "ClassA";
		String name = "methodA2";
		Map<String, String> params = Map.of("p1A2", "int", "p2A2", "long");
		ClassSpecification clsSpec = new ClassSpecification(clsName, AccessModifier.PUBLIC, pkg);
		
		List<ParameterSpecification> paramSpecs = params.entrySet()
				.stream()
				.map(p -> new ParameterSpecification(p.getKey(), p.getValue()))
				.toList();
		
		MethodSpecification mSpec = new MethodSpecification(
				name, paramSpecs, AccessModifier.PRIVATE, false, false, "void", clsSpec);
		DoubleDefinition.Method ddMethod = new DoubleDefinition.Method(mSpec);
		
		// Act
		List<String> result = ddMethod.actualRisks()
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
