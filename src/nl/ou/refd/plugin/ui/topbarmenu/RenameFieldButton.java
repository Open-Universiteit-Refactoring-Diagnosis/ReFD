package nl.ou.refd.plugin.ui.topbarmenu;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ensoftcorp.open.commons.ui.utilities.DisplayUtils;
import com.ensoftcorp.open.commons.utilities.MappingUtils;

import nl.ou.refd.exceptions.NoActiveProjectException;
import nl.ou.refd.locations.generators.ProjectProgramComponentsGenerator;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.SelectionUtil;
import nl.ou.refd.locations.specifications.ClassSpecification;
import nl.ou.refd.locations.specifications.FieldSpecification;
import nl.ou.refd.locations.specifications.MethodSpecification;
import nl.ou.refd.locations.specifications.PackageSpecification;
import nl.ou.refd.locations.specifications.LocationSpecification.AccessModifier;
import nl.ou.refd.plugin.Controller;
import nl.ou.refd.plugin.ui.EclipseUtil;

/**
 * Class representing the menu button for the Combine Methods into Class
 * refactoring option. The presence of this button can be configured in
 * plugin.xml.
 */
public class RenameFieldButton extends MenuButtonHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ExecutionEvent event) {
		
		// get the field selected by the user
		GraphQuery selectedElement = SelectionUtil.getSelection();
		
		if (selectedElement.locationCount() < 1) {
			DisplayUtils.showMessage("Error: No selection made");
			return;
		}
		
		ProgramLocation location = selectedElement.singleLocation();
		
		FieldSpecification fieldSource = null;
		
		if (FieldSpecification.locationIsField(location)) {
			fieldSource = new FieldSpecification(location);
		}
		else {
			DisplayUtils.showMessage("Error: Selection was not a field");
			return;
		}
		
		try {
			MappingUtils.mapWorkspace();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		IProject currentProject;
		
		try {
			currentProject = EclipseUtil.currentProject();
		} catch (NoActiveProjectException e) {
			DisplayUtils.showMessage("Error: No active project");
			return;
		}
		
		String newFieldString = DisplayUtils.promptString("Rename field", "Please provide the new name and visibility for the field (name, visibility)");
		String[] rawStrings = newFieldString.split(",");
		String[] splitFieldString = new String[2];
		for (int i=0; i<splitFieldString.length; i++)	{
			splitFieldString[i] = rawStrings[i].strip();
		}
		
		FieldSpecification newFieldSpecification = new FieldSpecification(splitFieldString[0], fieldSource.getEnclosingClass(), AccessModifier.fromString(splitFieldString[1]), fieldSource.isStatic());
		
		try {
			Controller.getController().renameField(fieldSource, newFieldSpecification);
		} catch (NoActiveProjectException e) {
			DisplayUtils.showMessage("Error: No active project");
			return;
		}
	}

}
