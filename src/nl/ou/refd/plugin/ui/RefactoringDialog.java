package nl.ou.refd.plugin.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public final class RefactoringDialog {
	
	public static class CombineMethodsDialog {
		
		private static String[] result;
		
		public static String[] showDialog() {
			
			// create dialog shell
			Display display = Display.getCurrent();
			final Shell shell = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			shell.setFocus();
			shell.setText("RefD refactoring diagnosis: Combine Methods into Class");
			FormLayout formLayout = new FormLayout ();
			formLayout.marginWidth = 10;
			formLayout.marginHeight = 10;
			formLayout.spacing = 10;
			shell.setLayout (formLayout);
			
			// pack and open shell
			shell.pack ();
			shell.open ();

			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			
			return null;
		}
	}
}
