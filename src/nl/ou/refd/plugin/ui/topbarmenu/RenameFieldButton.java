package nl.ou.refd.plugin.ui.topbarmenu;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import com.ensoftcorp.open.commons.ui.utilities.DisplayUtils;
import com.ensoftcorp.open.commons.utilities.MappingUtils;

import nl.ou.refd.exceptions.LocationSetException;
import nl.ou.refd.exceptions.NoActiveProjectException;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.SelectionUtil;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.FieldSpecification;
import nl.ou.refd.plugin.Controller;
import nl.ou.refd.plugin.ui.EclipseUtil;

/**
 * Class representing the menu button for the Rename Field refactoring 
 * option. The presence of this button can be configured in plugin.xml.
 */
public class RenameFieldButton extends MenuButtonHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(ExecutionEvent event) {
		
		// get the field selected by the user
		GraphQuery selectedElement = SelectionUtil.getSelection().locations(Tags.ProgramLocation.INSTANCE_VARIABLE);
		
		ProgramLocation location = null;
		FieldSpecification fieldSource = null;
		
		// try to extract a single location from the GraphQuery. If this fails, either no selection was made
		// or the selection wasn't a field as selecting a class or method always yields more than a single
		// location. To be sure that no non-fields slip through, we also explicitly check if the selection is a field.
		try {
			location = selectedElement.singleLocation();
			if (FieldSpecification.locationIsField(location)) {
				fieldSource = new FieldSpecification(location);
			}
			else {
				DisplayUtils.showMessage("Error: selection wasn't a field");
				return;
			}
		}
		catch (LocationSetException e) {
			DisplayUtils.showMessage("Error: couldn't trace your selection to a single field. Either nothing was selected or you didn't select a field.");
			return;
		}
		
		try {
			MappingUtils.mapWorkspace();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// User only needs to provide the new field's name, remaining specification can be copied over from the source field.
		String newFieldName = DisplayUtils.promptString("Rename field", "Please enter the new name for the field:");
		if (newFieldName == null || newFieldName.isEmpty()) {
			DisplayUtils.showMessage("Warning: received a null or empty field name. The new field name cannot be empty. Refactoring analysis will not be performed");
			return;
		}
		else {
			newFieldName = newFieldName.strip();
		}		
		
		FieldSpecification newFieldSpecification = new FieldSpecification(newFieldName, fieldSource.getEnclosingClass(), fieldSource.getVisibility(), fieldSource.isStatic());
		
		try {
			Controller.getController().renameField(fieldSource, newFieldSpecification);
		} catch (NoActiveProjectException e) {
			DisplayUtils.showMessage("Error: No active project");
			return;
		}
	}

}
