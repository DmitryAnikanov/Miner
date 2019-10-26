package miner.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import miner.Application;
import miner.controller.Controller;
import miner.controller.MouseController;
import miner.customwidgets.MineFieldWidget;
import miner.customwidgets.events.MineFieldWidgetSelectionEvent;
import miner.customwidgets.listeners.MineFieldWidgetSelectionListener;
import miner.exceptions.DataLoadException;
import miner.exceptions.DataProcessingException;
import miner.exceptions.MineFieldWidgetCellException;
import miner.model.GameSettings;
import miner.model.MinerData;
import miner.model.MinerModel;

import miner.view.gui.Gui;
import miner.view.gui.GuiEvents;
import miner.view.gui.TimerThread;
import miner.view.gui.internatiolization.Messages;

public class MineFieldView {

	private Composite parent;
	private Controller controller;
	private MineFieldWidget mineFieldWidget;
	
	@Inject IEventBroker broker;
	private TimerThread timer;

	private Messages messages = Application.INSTANCE.messages;
	private GameSettings gameSettings;

	public MineFieldView(Composite parent, GameSettings gameSettings) {
		this.parent = parent;
		this.gameSettings = gameSettings;
		createDisposeListener();
	}
	
	protected void createDisposeListener() {
		parent.addListener(SWT.Dispose, event-> {
			if (timer != null)
				timer.interrupt();
		});
	}
	
	public void init(IEclipseContext context) throws DataProcessingException {
		ContextInjectionFactory.inject(this, context);
		mineFieldWidget = createMineFieldWidget();
		MinerModel model = new MinerModel(new MinerData(gameSettings));
		controller = new MouseController(model);
		controller.setView(this);
		createTimer(context);				
	}
	
	private void createTimer(IEclipseContext context) {
		if (timer != null)
			stopTimer();
		timer = new TimerThread();
		ContextInjectionFactory.inject(timer, context);
	}
	
	public void runTimer() {
		try {
			timer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopTimer() {
		if (!timer.isInterrupted())
			timer.interrupt();
	}

	public void sendFlaggedCellsCountEvent(int flaggedCellsCount) {
		if (broker != null && flaggedCellsCount >= 0 )
			broker.send(GuiEvents.CELL_FLAGGED, flaggedCellsCount);
	}
	
	public void showWinMessage() {
		MessageDialog.openInformation(parent.getShell(), messages.warning, messages.win_message);
	}

	public void showGameOverMessage() {
		MessageDialog.openWarning(parent.getShell(), messages.warning, messages.game_over_message);
	}
	
	private MineFieldWidget createMineFieldWidget() {
		MineFieldWidget mineFieldWidget = new MineFieldWidget(parent,  SWT.FILL, gameSettings.getColsCount(), gameSettings.getRowsCount());
		mineFieldWidget.addSelectionListener(new MineFieldWidgetSelectionListener() {
			@Override
			public void mineFieldWidgetSelected(MineFieldWidgetSelectionEvent event) {
				int x = event.getCell().getX();
				int y = event.getCell().getY();;
				switch (event.getButton()) {
				case Gui.MOUSE_LEFT:
					try {
						controller.openCell(x, y);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Gui.MOUSE_RIGHT:
					try {
						controller.closeCell(x, y);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}			
		});
		return mineFieldWidget;
	}
	
	public Point getSize() {
		return mineFieldWidget.computeSize(0, 0, true);
	}
	
	public MineFieldWidget getMineFieldWidget() {
		return mineFieldWidget;
	}

	public void sendGameOverEvent() {
		broker.send(GuiEvents.GAME_OVER, this);	
	}

	public void drawUnpushed(int col, int row) throws MineFieldWidgetCellException {
		mineFieldWidget.getCell(col, row).drawUnpushed();
	}

	public void drawAroundMinesCount(int col, int row, int minesAroundCount) throws MineFieldWidgetCellException, DataLoadException {
		mineFieldWidget.getCell(col, row).drawAroundMinesCount(minesAroundCount);
	}

	public void drawFlagged(int col, int row) throws MineFieldWidgetCellException {
		mineFieldWidget.getCell(col, row).drawFlagged();		
	}

	public void drawQuestioned(int col, int row) throws MineFieldWidgetCellException {
		mineFieldWidget.getCell(col, row).drawQuestioned();		
	}
	
	public void drawMined(int col, int row) throws MineFieldWidgetCellException {
		mineFieldWidget.getCell(col, row).drawMined();	
	}
	
	public void drawRedAndMined(int col, int row) throws MineFieldWidgetCellException {
		mineFieldWidget.getCell(col, row).drawRedAndMined();	
	}

	public void drawPushed(int col, int row) throws MineFieldWidgetCellException {
		mineFieldWidget.getCell(col, row).drawPushed();		
	}
}
