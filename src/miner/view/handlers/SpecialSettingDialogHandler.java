package miner.view.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import miner.view.dialogs.SpecialSettingsDialog;

public class SpecialSettingDialogHandler {

	@Inject IEclipseContext context;
	
	@Execute
	public void execute(Shell shell) {
		SpecialSettingsDialog dialog = new SpecialSettingsDialog();
		ContextInjectionFactory.inject(dialog, context);
	}
}
