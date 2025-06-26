package nl.ou.refd.analysis.detectors;

import java.util.List;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

import com.ensoftcorp.atlas.core.licensing.AtlasLicenseException;
import com.ensoftcorp.atlas.ui.util.ProjectImporterUtil;

import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.specifications.PackageSpecification;
import nl.ou.refd.locations.specifications.ParameterSpecification;

@Execution(SAME_THREAD)
public class DoubleDefinitionTest {

	static final String TEST_PROJECT_NAME = "ReFDTestProject";
	static final String TEST_PACKAGE_NAME = "nl.ou.refd.test.analysis.detectors.doubledefinition";
	
	private final static PackageSpecification pkg = new PackageSpecification(TEST_PACKAGE_NAME);
	
	@BeforeAll
	static void initAll() { 
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(TEST_PROJECT_NAME);

		try {
			ProjectImporterUtil.mapProject(project);  // Blocking
		} catch (AtlasLicenseException e) {
			System.out.println("Atlas Indexing failed. No valid license");
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
		String clsName = "ClassA";
		AccessModifier clsAccess = AccessModifier.PUBLIC;
		
		ClassSpecification clsSpec = new ClassSpecification(clsName, clsAccess, pkg);
		DoubleDefinition.Class ddClass = new DoubleDefinition.Class(clsSpec);
		
		// Act
		List<String> result = ddClass.actualRisks()
				.locations()
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(clsName, result.get(0));
	}
	
	@Test
	void givenNonExistingClassName_whenDetectClass_thenNoRisk() {
		// Arrange
		String clsName = "NonExisitingClass";
		AccessModifier clsAccess = AccessModifier.PUBLIC;
		
		ClassSpecification clsSpec = new ClassSpecification(clsName, clsAccess, pkg);
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
		AccessModifier clsAccess = AccessModifier.PUBLIC;
		String mName = "methodA1";
		AccessModifier mAccess = AccessModifier.PUBLIC;
		String rtnType = "void";
		
		ClassSpecification clsSpec = new ClassSpecification(clsName, clsAccess, pkg);
		MethodSpecification mSpec = new MethodSpecification(
				mName, List.of(), mAccess, false, false, rtnType, clsSpec);
		DoubleDefinition.Method ddMethod = new DoubleDefinition.Method(mSpec);
		
		// Act
		List<String> result = ddMethod.actualRisks()
				.locations()
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(mName, result.get(0));
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
		AccessModifier clsAccess = AccessModifier.PUBLIC;
		String mName = "methodA2";
		AccessModifier mAccess = AccessModifier.PRIVATE;
		String rtnType = "int";
		List<ParameterSpecification> paramSpecs = List.of(
				new ParameterSpecification("p1A2", "int"),
				new ParameterSpecification("p2A2", "long")
		);
		ClassSpecification clsSpec = new ClassSpecification(clsName, clsAccess, pkg);
		
		MethodSpecification mSpec = new MethodSpecification(
				mName, paramSpecs, mAccess, false, false, rtnType, clsSpec);
		DoubleDefinition.Method ddMethod = new DoubleDefinition.Method(mSpec);
		
		// Act
		List<String> result = ddMethod.actualRisks()
				.locations()
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(mName, result.get(0));
	}
	
	@Test
	void givenMethodWithDifferentReturnType_whenDetectMethod_thenActualRisk() {
		// Arrange
		String clsName = "ClassA";
		AccessModifier clsAccess = AccessModifier.PUBLIC;
		String mName = "methodA2";
		AccessModifier mAccess = AccessModifier.PRIVATE;
		String rtnType = "boolean";
		List<ParameterSpecification> paramSpecs = List.of(
				new ParameterSpecification("p1A2", "int"),
				new ParameterSpecification("p2A2", "long")
		);
		ClassSpecification clsSpec = new ClassSpecification(clsName, clsAccess, pkg);
		
		MethodSpecification mSpec = new MethodSpecification(
				mName, paramSpecs, mAccess, false, false, rtnType, clsSpec);
		DoubleDefinition.Method ddMethod = new DoubleDefinition.Method(mSpec);
		
		// Act
		List<String> result = ddMethod.actualRisks()
				.locations()
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(mName, result.get(0));
	}
	
	@Test
	void givenOverloadingMethodWithDifferentNrParams_whenDetectMethod_thenNoRisk() {
		// Arrange
		String clsName = "ClassA";
		AccessModifier clsAccess = AccessModifier.PUBLIC;
		String mName = "methodA2";
		AccessModifier mAccess = AccessModifier.PRIVATE;
		String rtnType = "int";
		List<ParameterSpecification> paramSpecs = List.of(
				new ParameterSpecification("p1A2", "int")
		);
		ClassSpecification clsSpec = new ClassSpecification(clsName, clsAccess, pkg);
		
		MethodSpecification mSpec = new MethodSpecification(
				mName, paramSpecs, mAccess, false, false, rtnType, clsSpec);
		DoubleDefinition.Method ddMethod = new DoubleDefinition.Method(mSpec);
		
		// Act
		List<String> result = ddMethod.actualRisks()
				.locations()
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(0, result.size());
	}
	
	@Test
	void givenOverloadingMethodWithSameNrParams_whenDetectMethod_thenNoRisk() {
		// Arrange
		String clsName = "ClassA";
		AccessModifier clsAccess = AccessModifier.PUBLIC;
		String mName = "methodA2";
		AccessModifier mAccess = AccessModifier.PRIVATE;
		String rtnType = "int";
		List<ParameterSpecification> paramSpecs = List.of(
				new ParameterSpecification("p1A2", "int"),
				new ParameterSpecification("p2A2", "int")
		);
		ClassSpecification clsSpec = new ClassSpecification(clsName, clsAccess, pkg);
		
		MethodSpecification mSpec = new MethodSpecification(
				mName, paramSpecs, mAccess, false, false, rtnType, clsSpec);
		DoubleDefinition.Method ddMethod = new DoubleDefinition.Method(mSpec);
		
		// Act
		List<String> result = ddMethod.actualRisks()
				.locations()
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(0, result.size());
	}
	
	@AfterEach
	void tearDown() { }
	
	@AfterAll
	static void tearDownAll() { }
}
