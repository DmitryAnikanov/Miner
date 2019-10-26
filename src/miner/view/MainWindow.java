package miner.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import miner.Application;
import miner.customwidgets.ImageButtonWidget;
import miner.exceptions.DataProcessingException;
import miner.model.GameSettings;
import miner.view.gui.Gui;
import miner.view.gui.GuiEvents;

public class MainWindow {

	private MineFieldView view;
	private Composite mineFieldComposite;
	private Composite parent;
	private ImageButtonWidget gameStartButton;
	private Composite counterPanel;
	private Label flaggedCellsCountLabel;
	@Inject IEclipseContext context;
	private Label timerLabelValue;
	private Gui gui = new Gui();
	private Font font;
	
	@PostConstruct
	public void createComposite(final Composite parent) throws DataProcessingException {
		this.parent = parent;
		parent.setLayout(gui.getGridLayout(1, false));
		createCountersPanel();
		createControls(Application.INSTANCE.getGameSettings());
		ContextInjectionFactory.inject(Application.INSTANCE, context);
		addCloseListener(parent);
	}
	
	
	private void createCountersPanel() {
		counterPanel = new  Composite(parent, SWT.NONE);
		counterPanel.setBackground(gui.getOriginalMineSweeperGrayColor());

		counterPanel.setLayout(gui.getGridLayout(3, true));
		
		GridData gdata = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gdata.minimumHeight = 40;

		counterPanel.setLayoutData(gdata);
		
		flaggedCellsCountLabel = new Label(counterPanel, SWT.NONE);
		
		font = new Font(Display.getDefault(), "Tahoma", 12, SWT.BOLD);
		flaggedCellsCountLabel.setFont(font);
		flaggedCellsCountLabel.setForeground(gui.getOriginalMineSweeperRedColor());
		flaggedCellsCountLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		flaggedCellsCountLabel.setText("000");
		flaggedCellsCountLabel.pack();
		
		gameStartButton = new ImageButtonWidget (counterPanel, SWT.NONE);
		gameStartButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		gameStartButton.addListener(SWT.MouseDown, e-> {
			try {
				createControls(Application.INSTANCE.getGameSettings());
			} catch (DataProcessingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		timerLabelValue = new Label(counterPanel, SWT.NONE);
		timerLabelValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		timerLabelValue.setText("99");
		timerLabelValue.pack();		
		
		timerLabelValue.setFont(font);
		timerLabelValue.setForeground(gui.getOriginalMineSweeperRedColor());
		counterPanel.pack();
	}

	private void createControls(GameSettings gameSettings) throws DataProcessingException {
		setTimerLabelAndStartButtonToDefaultState();
		setFlaggedCellsRemainCountLabelValue(0);
		createGameBoard(gameSettings);
		setActualShellSize();
		gui.centerShell(parent.getShell());
	}
	
	private void addCloseListener(Composite parent) {
		parent.addListener(SWT.Close, event-> {
			font.dispose();
		});
	}
	
	private void createGameBoard(GameSettings gameSettings) throws DataProcessingException {
		createMineFieldComposite();
		view = new MineFieldView(mineFieldComposite, gameSettings);
		view.init(context);
	}
	
	private void createMineFieldComposite() {
		if(mineFieldComposite != null && !mineFieldComposite.isDisposed())
			mineFieldComposite.dispose();
		mineFieldComposite = new  Composite(parent, SWT.NONE);
		mineFieldComposite.setLayout(new FillLayout());
	}
	
	private void setActualShellSize() {
		Shell shell = parent.getShell();
		Point shell_size = shell.getSize();
	    
		Rectangle client_area = shell.getClientArea();
		int frameWidth = shell_size.x - client_area.width;
		int frameHeight = shell_size.y - client_area.height;
		
		int counterPanelHeight = counterPanel.getSize().y;
		parent.getShell().pack();
		shell.setSize(view.getSize().x + frameWidth, 
				view.getSize().y + frameHeight + counterPanelHeight);

	}
	
	/*@Focus
    public void onFocus() {
		setActualShellSize();
    }*/
	
	@Inject @Optional
    public void handleNewGameStarting(@UIEventTopic(GuiEvents.NEW_GAME_STARTING) GameSettings gameSettings) throws DataProcessingException {
		createControls(gameSettings);
	}
	
	private void setTimerLabelAndStartButtonToDefaultState() {
		gameStartButton.drawDefault();
		setIntToTextLabel(timerLabelValue, 0);
	}
	
	@Inject @Optional
    public void handleGameSettingsChanging(@UIEventTopic(GuiEvents.SETTINGS_CHANGED) GameSettings gameSettings) throws DataProcessingException {
		createControls(gameSettings);
	}
	
	@Inject @Optional
    public void handleCellIsFlagged(@UIEventTopic(GuiEvents.CELL_FLAGGED) int flaggedCellsCount) throws DataProcessingException {		
		setFlaggedCellsRemainCountLabelValue(flaggedCellsCount);
	}
	
	@Inject @Optional
    public void handleTimerEvent(@UIEventTopic(GuiEvents.TIMER_EVENT) int seconds) throws DataProcessingException {				
		Display.getDefault().asyncExec(new Runnable() {  
        	public void run() {  
        		try {        		
        			setIntToTextLabel(timerLabelValue, seconds);
            	 } catch (Exception e) {
            		 
            	 }
        	}  
        });
	}
	
	@Inject @Optional
    public void handleGameOver(@UIEventTopic(GuiEvents.GAME_OVER) Object View) throws DataProcessingException {		
		gameStartButton.drawGameIsOver();
	}
	
	private void setFlaggedCellsRemainCountLabelValue(int flaggedCellsCount) {
		
		setIntToTextLabel(flaggedCellsCountLabel, 
				Application.INSTANCE.getGameSettings().getMinesCount() - flaggedCellsCount);
	}
	
	private void setIntToTextLabel(Label label, int value) {
		label.setText(String.valueOf(value));
		label.pack();
		label.redraw();
	}
	
}
