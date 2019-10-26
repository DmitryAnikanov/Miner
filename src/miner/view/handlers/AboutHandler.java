package miner.view.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import miner.view.dialogs.InfoDialog;

public class AboutHandler {

	@Execute
	public void execute(Shell shell) {		
		new InfoDialog();
	}
}
