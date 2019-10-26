package miner.view.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import miner.Application;
import miner.view.gui.Gui;


public class InfoDialog {

	private Gui gui = new Gui();
	private Shell dialog;
	
	public InfoDialog() {
		createAndConfigurateDialog();
		addCloseListener();
		gui.centerShell(dialog);
		dialog.pack();
		dialog.open();
		
	}
	
	private void createAndConfigurateDialog() {
		dialog = gui.createOsSpecificDialog();
		dialog.setSize(250, 140);
		gui.centerShell(dialog);
		dialog.setText(Application.INSTANCE.messages.info_dialog_name);
	    dialog.setLayout(new GridLayout(1, false));
	    
	    Label rowsLbl = new Label(dialog, SWT.NONE);
	    rowsLbl.setText(Application.INSTANCE.messages.info_dialog_message);
		rowsLbl.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1,1));
	}
	private void addCloseListener() {
		dialog.addListener(SWT.Close, event-> {
			dialog.dispose();
		});
	}
}
