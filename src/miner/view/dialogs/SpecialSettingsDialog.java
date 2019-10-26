package miner.view.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import miner.Application;
import miner.exceptions.DataProcessingException;
import miner.model.GameSettings;
import miner.view.gui.Gui;
import miner.view.gui.GuiEvents;
import miner.view.gui.internatiolization.Messages;

public class SpecialSettingsDialog {

	@Inject IEventBroker broker;
	
	private Gui gui = new Gui();
	private Shell dialog;
	private int minesCount;
	private int cols;
	private int rows;

	private Text minesCountText;

	private Composite dataComposite;

	private Text heighText;
	private Text widthText;
	private Messages messages = Application.INSTANCE.messages;
	public SpecialSettingsDialog() {
		createAndConfigurateDialog();
		createDataRecieverComposite();
	    addCloseListener();
	    dialog.pack();
	    dialog.open();
	}
	
	private void createAndConfigurateDialog() {
		dialog = gui.createOsSpecificDialog();
		dialog.setSize(200, 140);
		gui.centerShell(dialog);
		dialog.setText(messages.special_dialog_name);
	    dialog.setLayout(new GridLayout(1, false));
	}
	
	
	private void createDataRecieverComposite() {
		
		dataComposite = new  Composite(dialog, SWT.NONE);
		dataComposite.setLayout(new GridLayout(2, false));
		dataComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Label heightLbl = new Label(dataComposite, SWT.NONE);
		heightLbl.setText(messages.special_dialog_height);
		heightLbl.setLayoutData(gui.getGridData());
		
		heighText = new Text(dataComposite, SWT.BORDER);
		heighText.setLayoutData(gui.getGridData());
		heighText.setText("");
		heighText.addListener(SWT.Verify, gui.getDigitListener());
		
		Label widthLbl = new Label(dataComposite, SWT.NONE);
		widthLbl.setText(messages.special_dialog_width);
		widthLbl.setLayoutData(gui.getGridData());
		
		widthText = new Text(dataComposite, SWT.BORDER);
		widthText.setText("");
		widthText.setLayoutData(gui.getGridData());
		widthText.addListener(SWT.Verify, gui.getDigitListener());
		
		Label minesCountLbl = new Label(dataComposite, SWT.NONE);
		minesCountLbl.setText(messages.special_dialog_mines_count);
		minesCountLbl.setLayoutData(gui.getGridData());
		
		minesCountText = new Text(dataComposite, SWT.BORDER);
		minesCountText.setLayoutData(gui.getGridData());
		minesCountText.setText("");
		minesCountText.addListener(SWT.Verify, gui.getDigitListener());
		
		Button okButton = new Button(dataComposite, SWT.PUSH);
	    okButton.setText("&OK");
	    okButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 2, 1));
	    okButton.addListener(SWT.Selection, event-> {
	    	try {
		    	cols = toInt(widthText.getText());
		    	rows = toInt(heighText.getText());
		    	minesCount = toInt(minesCountText.getText());
		    	GameSettings gameSettings = new GameSettings(cols, rows, minesCount);
		    	Application.INSTANCE.setGameSettings(gameSettings);
		    	broker.send(GuiEvents.SETTINGS_CHANGED, gameSettings);
		    	dialog.close();
	    	} catch (DataProcessingException e) {
	    		MessageDialog.openError(dialog.getShell(), messages.warning, e.getMessage());
			} catch (Exception e) {
				MessageDialog.openError(dialog.getShell(), messages.warning, messages.unresolved_problem);
			}
	    });
	    dataComposite.pack();
	}
	
	private int toInt(String str) {
		return Integer.parseInt(str);
	}
	
	private void addCloseListener() {
		dialog.addListener(SWT.Close, event-> {
			try {
				Application.INSTANCE.setGameSettings(new GameSettings(cols, rows, minesCount));
			} catch (DataProcessingException e) {
									
			}
			dialog.dispose();
		});
	}
}
