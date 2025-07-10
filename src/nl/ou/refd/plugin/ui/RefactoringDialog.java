package nl.ou.refd.plugin.ui;

import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Class containing user interaction dialogs for various refactorings
 */
public final class RefactoringDialog {

	/**
	 * Dialog for obtaining the data required to perform Combine Methods into Class.
	 * This is a basic dialog with minimal layout.
	 */
	public static class CombineMethodsDialog {

		private static String[] result = new String[3];

		/**
		 * Show the dialog and return the user's input as an array of Strings.
		 * If the user leaves any field blank, will show an alert and persist
		 * until all input is non-empty.
		 * @return the user's input, an array of strings of size 3.
		 */
		public static String[] showDialog() {

			// create the dialog shell
			Display display = Display.getCurrent();
			final Shell shell = new Shell (display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			shell.setFocus();
			shell.setText("RefD: Combine Methods into Class");

			// define shell layout
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			shell.setLayout(gridLayout);

			// add components
			GridData gridData = new GridData();
			Label infoLabel = new Label(shell, SWT.NONE);
			infoLabel.setText("Please provide visibility, name and package for the new class.");
			gridData.horizontalSpan = 2;
			infoLabel.setLayoutData(gridData);

			Label visibilityLabel = new Label(shell, SWT.NONE);
			visibilityLabel.setText("Visibility:");

			Combo visibilityCombo = new Combo (shell, SWT.READ_ONLY);
			visibilityCombo.setItems("public", "protected", "private");

			Label nameLabel = new Label(shell, SWT.NONE);
			nameLabel.setText("Name:");

			Text nameText = new Text(shell, SWT.BORDER);

			Label packageLabel = new Label(shell, SWT.NONE);
			packageLabel.setText("Package:");

			Text packageText = new Text(shell, SWT.BORDER);

			// OK button
			Button ok = new Button(shell, SWT.PUSH);
			shell.setDefaultButton (ok);
			ok.setText ("OK");
			ok.addSelectionListener(widgetSelectedAdapter(event -> {
				result[0] = visibilityCombo.getItem(visibilityCombo.getSelectionIndex());
				result[1] = nameText.getText();
				result[2] = packageText.getText();
				for (String s : result) {
					if (s == null || s.isEmpty()) { // warn user when input is not complete
						MessageDialog.openWarning(new Shell(Display.getCurrent()), "Alert", "A field was empty or null. Please provide all information.");
						break;
					}
				}
				shell.close();
			}));

			// Cancel button
			Button cancel = new Button (shell, SWT.PUSH);
			cancel.setText ("Cancel");
			cancel.addSelectionListener(widgetSelectedAdapter(event -> {
				shell.close ();
			}));

			// pack and open shell
			shell.pack ();
			shell.open ();

			// event loop
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			
			return result;
		}
	}
}
