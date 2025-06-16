package nl.ou.refd.plugin.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import nl.ou.refd.exceptions.NoActiveProjectException;

/**
 * Class containing utility methods to work with Eclipse.
 */
public class EclipseUtil {
	
	/**
	 * Gets the current open project in the Eclipse environment.
	 * @return an object of type IProject representing the open project
	 * @throws NoActiveProjectException if there is no project currently open
	 */
	public static IProject currentProject() throws NoActiveProjectException {
		IEditorPart activeEditor = getActivePage().getActiveEditor();
		
		if (activeEditor != null) {
			IEditorInput input = activeEditor.getEditorInput();
			
			IProject project = input.getAdapter(IProject.class);
			if (project == null) {
				IResource resource = input.getAdapter(IResource.class);
				if (resource != null) {
					project = resource.getProject();
				}
			}
			
			return project;
		}
		
		throw new NoActiveProjectException("No active project to get the name of");
	}
	
	/**
	 * Gets the current selected text in the eclipse editor
	 * @return the selected text or the empty string if none selected
	 */
	public static String currentEditorTextSelection() {
		ISelection selection = getActivePage().getSelection();
		
		if (selection instanceof TextSelection) {
			return ((TextSelection)selection).getText();
		}
		return "";
	}
	
	/**
	 * Gets the active page of the eclipse editor
	 * @return the active page
	 */
	private static IWorkbenchPage getActivePage() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		return window.getActivePage();
	}
	
}
