package nl.ou.refd.plugin.ui.topbarmenu;

import org.eclipse.core.commands.ExecutionEvent;
import com.ensoftcorp.open.commons.ui.utilities.DisplayUtils;
import nl.ou.refd.exceptions.NoActiveProjectException;
import nl.ou.refd.locations.graph.GraphQuery;
import nl.ou.refd.locations.graph.ProgramLocation;
import nl.ou.refd.locations.graph.SelectionUtil;
import nl.ou.refd.locations.graph.Tags;
import nl.ou.refd.locations.specifications.ExpressionSpecification;
import nl.ou.refd.locations.specifications.VariableSpecification;
import nl.ou.refd.plugin.Controller;

/**
 * Class representing the menu button for the Extract Variable refactoring
 * option. The presence of this button can be configured in plugin.xml.
 */
public class ExtractVariableButton extends MenuButtonHandler {

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
		
		ProgramLocation location = selectedElement.locations().iterator().next();
		
		ExpressionSpecification expressionSource = null;
		
		// TODO: can't directly select expression with multiple operators and operands in Atlas
//		if (ExpressionSpecification.locationIsExpression(location)) {
			expressionSource = new ExpressionSpecification(location);
//		}
//		else {
//			DisplayUtils.showMessage("Error: Selection was not an expression");
//			return;
//		}
		
		// Check location is a statement
		
		// Open dialog and ask user to select expression
		
		// Check expression is valid (hard to do!)
		
		String newVariableString = DisplayUtils.promptString(
				"New Variable", "Please provide the name and type of the new variable (name,type)");
		String[] splitVariableString = newVariableString.split(",");
		
		VariableSpecification destination = new VariableSpecification(
				splitVariableString[0], 
				splitVariableString[1], 
				expressionSource.getEnclosingMethod());
		
		try {
			Controller.getController().extractVariable(expressionSource, destination);
		} catch (NoActiveProjectException e) {
			DisplayUtils.showMessage("Error: No active project");
			return;
		}
	}
	
	

}
