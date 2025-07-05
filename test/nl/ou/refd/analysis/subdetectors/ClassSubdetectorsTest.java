package nl.ou.refd.analysis.subdetectors;

import java.util.ArrayList;
import java.util.Collections;
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

import com.ensoftcorp.atlas.core.index.filter.IndexFilter;
import com.ensoftcorp.atlas.core.index.filter.IndexFilterService;
import com.ensoftcorp.atlas.core.licensing.AtlasLicenseException;
import com.ensoftcorp.atlas.ui.util.ProjectImporterUtil;

import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.AbstractClasses;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.AllSubclasses;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.AllSuperClasses;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.ClassesByName;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.ConcreteClasses;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.DifferenceWithClasses;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.DirectSubclasses;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.DirectSuperClasses;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.FirstConcreteSubclasses;
import nl.ou.refd.analysis.subdetectors.ClassSubdetectors.Methods;
import nl.ou.refd.locations.collections.ClassSet;
import nl.ou.refd.locations.graph.Graph;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.streams.ClassStream;

@Execution(SAME_THREAD)
public class ClassSubdetectorsTest {

	static final String TEST_PROJECT_NAME = "SampleAtlasPluginProject";
	static final String TEST_PACKAGE_NAME = "nl.ou.refd.mock.analysis.subdetectors.classes";
	
	private static Set<ProgramLocation> querySpace = null;
	
	@BeforeAll
	static void initAll() { 
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(TEST_PROJECT_NAME);
		
		IndexFilter filter = new IndexFilter(TEST_PACKAGE_NAME, false, false);

		try {
			if (IndexFilterService.getInstance().setFilters(List.of(), List.of(filter), false, true)) {
				ProjectImporterUtil.mapProject(project);  // Blocking
			}
		} catch (AtlasLicenseException e) {
			System.out.println("Atlas Indexing failed. No valid license");
		}
		
		querySpace = Graph.query()
			.universe()
			.relations(Tags.Relation.EDGE)
			.forward(Graph.query()
				.universe()
				.pkg(TEST_PACKAGE_NAME))
			.locations();	
	}
	
	@BeforeEach
	void init() { }
	
	@Test
	void givenExistingClassName_whenDetectClassesByName_thenReturnClassName() {	
		// Arrange
		String name = "ClassANoSuper";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		List<String> result = clsByName.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
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
	void givenAbstractClassName_whenDetectClassesByName_thenReturnAbstractClass() {
		// Arrange
		String name = "AbstractClassE";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		List<String> result = clsByName.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
	}
	
	@Test
	void givenInterfaceName_whenDetectClassesByName_thenReturnInterface() {
		// Arrange
		String name = "InterfaceF";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		List<String> result = clsByName.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
	}
	
	@Test
	void givenPublicInnerClassName_whenDetectClassesByName_thenReturnInnerClass() {
		// Arrange
		String name = "InnerClassI1";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		List<String> result = clsByName.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
	}
	
	@Test
	void givenPrivateInnerClassName_whenDetectClassesByName_thenReturnInnerClass() {
		// Arrange
		String name = "InnerClassI2";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		List<String> result = clsByName.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
	}
	
	@Test
	void givenPublicStaticNestedClassName_whenDetectClassesByName_thenReturnStaticNestedClass() {
		// Arrange
		String name = "StaticNestedClassI3";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		List<String> result = clsByName.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
	}
	
	@Test
	void givenPrivateStaticNestedClassName_whenDetectClassesByName_thenReturnStaticNestedClass() {
		// Arrange
		String name = "StaticNestedClassI4";
		ClassesByName clsByName = new ClassesByName(name);
		
		// Act
		List<String> result = clsByName.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(name, result.get(0));
	}
	
	@Test
	void givenClassWithSuper_whenDetectDirectSuperClasses_thenReturnDirectSuperOnly() {
		// Arrange
		String subName = "ClassCExtendsB";
		String superName = "ClassBExtendsA";
		Set<ProgramLocation> subCls = new ClassesByName(subName).applyOn(querySpace);
		DirectSuperClasses superCls = new DirectSuperClasses();
		
		// Act
		List<String> result = superCls.applyOn(subCls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1,  result.size());
		Assertions.assertEquals(superName, result.get(0));
	}
	
	@Test
	void givenClassWithoutSuper_whenDetectDirectSuperClasses_thenReturnObject() {
		// Arrange
		String name = "ClassANoSuper";
		Set<ProgramLocation> cls = new ClassesByName(name).applyOn(querySpace);
		DirectSuperClasses superCls = new DirectSuperClasses();
		
		// Act
		List<String> result = superCls.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1,  result.size());
		Assertions.assertEquals("Object", result.get(0));
	}
	
	@Test
	void givenClassNoSuperWithInterface_whenDetectDirectSuperClasses_thenReturnObject() {
		// Arrange
		String name = "ClassGImplementsF";
		Set<ProgramLocation> cls = new ClassesByName(name).applyOn(querySpace);
		DirectSuperClasses superCls = new DirectSuperClasses();
		
		// Act
		List<String> result = superCls.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1,  result.size());
		Assertions.assertEquals("Object", result.get(0));
	}
	
	@Test
	void givenClassWithMultipleSuper_whenDetectAllSuperClasses_thenReturnAll() {
		// Arrange
		String clsName = "ClassCExtendsB";
		List<String> superNames = List.of("ClassANoSuper", "ClassBExtendsA", "Object");
		Set<ProgramLocation> cls = new ClassesByName(clsName).applyOn(querySpace);
		AllSuperClasses superCls = new AllSuperClasses();
		
		// Act
		List<String> result = superCls.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(superNames.size(), result.size());
		Assertions.assertTrue(superNames.containsAll(result));
	}
	
	@Test
	void givenClassWithNoSuper_whenDetectAllSuperClasses_thenReturnObject() {
		// Arrange
		String clsName = "ClassANoSuper";
		Set<ProgramLocation> cls = new ClassesByName(clsName).applyOn(querySpace);
		AllSuperClasses superCls = new AllSuperClasses();
		
		// Act
		List<String> result = superCls.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(1, result.size());
		Assertions.assertTrue("Object".equals(result.get(0)));
	}
	
	@Test
	void givenClassWithPublicProtectedPrivateMethods_whenDetectMethods_thenReturnAllClassMethods() {
		// Arrange
		String clsName = "ClassANoSuper";
		List<String> methodNames = List.of("methodA1", "methodA2", "methodA3");
		Set<ProgramLocation> cls = new ClassesByName(clsName).applyOn(querySpace);
		Methods methods = new Methods();
		
		// Act
		List<String> result = methods.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(methodNames.size(), result.size());
		Assertions.assertTrue(methodNames.containsAll(result));
	}
	
	@Test
	void givenClassWithStaticMethods_whenDetectMethods_thenReturnAllClassMethods() {
		// Arrange
		String clsName = "ClassDNoSuper";
		List<String> methodNames = List.of("staticMethodD1", "staticMethodD2", "methodD3");
		Set<ProgramLocation> cls = new ClassesByName(clsName).applyOn(querySpace);
		Methods methods = new Methods();
		
		// Act
		List<String> result = methods.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(methodNames.size(), result.size());
		Assertions.assertTrue(methodNames.containsAll(result));
	}
	
	@Test
	void givenClassWithAbstractMethods_whenDetectMethods_thenReturnAllClassMethods() {
		// Arrange
		String clsName = "AbstractClassE";
		List<String> methodNames = List.of("abstractMethodE1", "methodE2", "methodE3");
		Set<ProgramLocation> cls = new ClassesByName(clsName).applyOn(querySpace);
		Methods methods = new Methods();
		
		// Act
		List<String> result = methods.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(methodNames.size(), result.size());
		Assertions.assertTrue(methodNames.containsAll(result));
	}
	
	@Test
	void givenInterfaceWithMethods_whenDetectMethods_thenReturnAllInterfaceMethods() {
		// Arrange
		String iName = "InterfaceF";
		List<String> methodNames = List.of("iMethodF1", "iMethodF2", "iMethodF3");
		Set<ProgramLocation> cls = new ClassesByName(iName).applyOn(querySpace);
		Methods methods = new Methods();
		
		// Act
		List<String> result = methods.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(methodNames.size(), result.size());
		Assertions.assertTrue(methodNames.containsAll(result));
	}
	
	@Test
	void givenClassWithSuperMethods_whenDetectMethods_thenReturnClassMethodsOnly() {
		// Arrange
		String clsName = "ClassBExtendsA";
		List<String> methodNames = List.of("methodB1", "methodB2", "methodB3");
		Set<ProgramLocation> cls = new ClassesByName(clsName).applyOn(querySpace);
		Methods methods = new Methods();
		
		// Act
		List<String> result = methods.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(methodNames.size(), result.size());
		Assertions.assertTrue(methodNames.containsAll(result));
	}
	
	@Test
	void givenMultipleAbstractClasses_whenDetectAbstractClasses_thenReturnAllAbstractClasses() {
		// Arrange
		List<String> clsNames = List.of(
				"AbstractClassE", 
				"AbstractClassH", 
				"AbstractClassJExtendsB", 
				"AbstractLExtendsK", 
				"AbstractMExtendsL");
		AbstractClasses cls = new AbstractClasses();
		
		// Act
		List<String> result = cls.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(clsNames.size(), result.size());
		Assertions.assertTrue(clsNames.containsAll(result));
	}
	
	
	@Test
	void givenAllClasses_whenDetectDifferenceWithClassesClassA_thenReturnAllClassesWithoutA() {
		// Arrange
		String excludedClsName = "ClassANoSuper";
		Set<ProgramLocation> allClasses = Graph.query(querySpace)
				.locations(Tags.ProgramLocation.CLASS)
				.locations();
		List<String> clsNames = allClasses.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		Set<ProgramLocation> excludedCls = new ClassesByName(excludedClsName).applyOn(allClasses);
		DifferenceWithClasses cls = new DifferenceWithClasses(
				new ClassStream(new ClassSet(excludedCls)));
		
		// Act
		List<String> result = cls.applyOn(allClasses)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();		
		
		// Assert
		Assertions.assertEquals(clsNames.size() - 1, result.size());
		Assertions.assertFalse(result.contains(excludedClsName));
		Assertions.assertTrue(clsNames.containsAll(result));
	}
	
	@Test
	void givenAllClassesIncludingAbstract_whenDetectConcreteClasses_thenReturnConcreteOnly() {
		// Arrange
		List<String> abstractClsNames = List.of(
				"AbstractClassE", 
				"AbstractClassH", 
				"AbstractClassJExtendsB",
				"AbstractLExtendsK",
				"AbstractMExtendsL");
		Set<ProgramLocation> allClasses = Graph.query(querySpace)
				.locations(Tags.ProgramLocation.CLASS)
				.locations();
		List<String> allClsNames = allClasses.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		List<String> concreteClsNames = new ArrayList<>(allClsNames);
		concreteClsNames.removeAll(abstractClsNames);
		ConcreteClasses cls = new ConcreteClasses();
		
		// Act
		List<String> result = cls.applyOn(querySpace)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(concreteClsNames.size(), result.size());
		Assertions.assertTrue(Collections.disjoint(abstractClsNames, result));
		Assertions.assertTrue(result.containsAll(concreteClsNames));
	}
	
	@Test
	void givenClassWithDirectSub_whenDetectDirectSubclasses_thenReturnDirectSubsOnly() {
		// Arrange
		List<String> subNames = List.of("ClassCExtendsB", "AbstractClassJExtendsB");
		String superName = "ClassBExtendsA";
		Set<ProgramLocation> superCls = new ClassesByName(superName).applyOn(querySpace);
		DirectSubclasses subCls = new DirectSubclasses();
		
		// Act
		List<String> result = subCls.applyOn(superCls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(subNames.size(),  result.size());
		Assertions.assertTrue(result.containsAll(subNames));
	}
	
	@Test
	void givenClassWithoutSub_whenDetectDirectSubclasses_thenReturnEmpty() {
		// Arrange
		String name = "ClassCExtendsB";
		Set<ProgramLocation> cls = new ClassesByName(name).applyOn(querySpace);
		DirectSubclasses subCls = new DirectSubclasses();
		
		// Act
		List<String> result = subCls.applyOn(cls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(0,  result.size());
	}
	
	@Test
	void givenClassWithDeeperSubs_whenDetectAllSubclasses_thenReturnAllSubs() {
		// Arrange
		List<String> subNames = List.of("ClassCExtendsB", "ClassBExtendsA", "AbstractClassJExtendsB");
		String superName = "ClassANoSuper";
		Set<ProgramLocation> superCls = new ClassesByName(superName).applyOn(querySpace);
		AllSubclasses subCls = new AllSubclasses();
		
		// Act
		List<String> result = subCls.applyOn(superCls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(subNames.size(),  result.size());
		Assertions.assertTrue(result.containsAll(subNames));
	}
	
	/**
	 * Graph of class hierarchy to test on. 
	 * 
	 *           K
	 *         /   \
	 *       (a)L   P
	 *       /  \
	 *    (a)M   O
	 *      |
	 *      N 
	 *      
	 * FIXME Unclear what the subdetector has to do. Description is different then the applied query.
	 * Test fails. Does not return N, O, P as expected.
	 */
	@Test
	void givenClassWithDeeperAbstractSubs_whenDetectFirstConcreteSubclasses_thenReturnConcreteSubOnly() {
		// Arrange
		List<String> subNames = List.of("ClassNExtendsM", "ClassOExtendsL", "ClassPExtendsK");
		String superName = "ClassK";
		Set<ProgramLocation> superCls = new ClassesByName(superName).applyOn(querySpace);
		FirstConcreteSubclasses subCls = new FirstConcreteSubclasses();
		
		// Act
		List<String> result = subCls.applyOn(superCls)
				.stream()
				.map(pl -> pl.<String>getAttribute(Tags.Attributes.NAME))
				.toList();
		
		// Assert
		Assertions.assertEquals(subNames.size(),  result.size());
		Assertions.assertTrue(result.containsAll(subNames));
	}
	
	
	
	@AfterEach
	void tearDown() { }
	
	@AfterAll
	static void tearDownAll() { }
}
