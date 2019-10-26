package miner.view.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.Shell;

import miner.Application;
import miner.view.gui.GuiEvents;

public class AmatureSettingsHandler {
	
	@Inject IEventBroker broker;
	
	@Execute
	public void execute(Shell shell) {		
		Application.INSTANCE.setGameSettings(Application.INSTANCE.getAmatureSettings());
		broker.send(GuiEvents.SETTINGS_CHANGED, Application.INSTANCE.getAmatureSettings());
	}
}
