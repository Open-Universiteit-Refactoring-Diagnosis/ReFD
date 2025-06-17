package nl.ou.refd.plugin.ui.topbarmenu;

import org.eclipse.core.commands.ExecutionEvent;

import com.ensoftcorp.open.commons.ui.utilities.DisplayUtils;
import com.ensoftcorp.open.commons.utilities.MappingUtils;

import nl.ou.refd.exceptions.NoActiveProjectException;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.SelectionUtil;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.plugin.Controller;

/**
 * Class representing the menu button for the Change Method Declaration refactoring option.
 * The presence of this button can be configured in plugin.xml.
 */
public class ChangeMethodDeclarationButton extends MenuButtonHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ExecutionEvent event) {
		GraphQuery selectedElement = SelectionUtil.getSelection();
		
		if (selectedElement.locationCount() < 1) {
			DisplayUtils.showMessage("Error: No selection made");
			return;
		}
		
		ProgramLocation location = selectedElement.singleLocation();
		MethodSpecification methodSource = null;
		if (MethodSpecification.locationIsMethod(location)) {
			methodSource = new MethodSpecification(location);
		}
		else {
			DisplayUtils.showMessage("Error: Selection was not a method");
			return;
		}
		
		try {
			MappingUtils.mapWorkspace();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MethodSpecification newSpecification;
		
		try {
			Controller.getController().changeMethodSpecification(methodSource, newSpecification);
		} catch (NoActiveProjectException e) {
			DisplayUtils.showMessage("Error: No active project");
			return;
		}
	}

}
