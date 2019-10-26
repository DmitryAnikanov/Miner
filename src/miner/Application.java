package miner;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.nls.Translation;

import miner.exceptions.DataProcessingException;
import miner.model.GameSettings;
import miner.view.gui.internatiolization.Messages;

public class Application {
	
	public static IEclipseContext context;

	private GameSettings gameSettings;
	
	private GameSettings beginnerGameSettings;
	private GameSettings amatureGameSettings;
	private GameSettings professionalGameSettings;
	public @Inject @Translation Messages messages;
	
	public static final Application INSTANCE = new Application(); 

	public void setContext(IEclipseContext context) {
		this.context = context;
	}

	public GameSettings getGameSettings() {
		return gameSettings;
	}
	
	public void setDefaultGameSettings() {
		gameSettings = getBeginnerSettings();
	}

	
	public void setGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}
	
	public GameSettings getBeginnerSettings() {
		try {
			return beginnerGameSettings = new GameSettings(9, 9, 10);
		} catch (DataProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return beginnerGameSettings;
	}
	
	public GameSettings getAmatureSettings() {
		try {
			return amatureGameSettings  = new GameSettings(16, 16, 40);
		} catch (DataProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return amatureGameSettings;
	}
	
	public GameSettings getProfessionalGameSettings() {
		try {
			return professionalGameSettings  = new GameSettings(30, 16, 99);
		} catch (DataProcessingException e) {
			e.printStackTrace();
		}
		return professionalGameSettings;
	}
}	
	

	

	
	
