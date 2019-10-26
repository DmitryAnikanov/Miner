package miner.view.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.widgets.Shell;

import miner.Application;
import miner.view.gui.GuiEvents;

public class NewGameStartingHandler {
	
	@Inject IEventBroker broker;
	
	@Execute
	public void execute(Shell shell) {
		broker.send(GuiEvents.NEW_GAME_STARTING, Application.INSTANCE.getGameSettings());
		System.out.println("new game handler executed");
	}
}
