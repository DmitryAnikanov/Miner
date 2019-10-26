package miner.controller;

import miner.exceptions.DataLoadException;
import miner.exceptions.DataProcessingException;
import miner.exceptions.MineFieldWidgetCellException;
import miner.model.MinerModel;
import miner.model.MinerModelCell;
import miner.model.mines.SimpleMine;
import miner.view.MineFieldView;

public class MouseController extends Controller {
	
	private MineFieldView view;
	private boolean gameInProcess = true;
	private int flaggedCellsCount;
	private Class<SimpleMine> mineClass;
	
	public MouseController (MinerModel model) {
		name = "Mouse controller";
		super.model = model;
		this.mineClass = SimpleMine.class;
	}
	
	public void setView(MineFieldView view) {
		this.view = view;
	}

	@Override
	public synchronized void openCell(int x, int y) throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		if (!gameInProcess) return;
		checkIfFirstStep(x, y);
		model.openCell(x, y);
		if (model.isWin()) {
			gameInProcess = false;
			view.stopTimer();
			openAllCells();
			view.showWinMessage();			
		} else if (model.isGameOver()) {
			gameInProcess = false;
			view.stopTimer();
			view.sendGameOverEvent();
			openAllCells();
			view.showGameOverMessage();
		} else {
			synchronizeModelAndView();
		}
	}

	@Override
	public synchronized void closeCell(int x, int y) throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		if (!gameInProcess) return;
		checkIfFirstStep(x, y);
		model.iterateCellType(x, y);
		synchronizeModelAndView();
	}

	private void checkIfFirstStep(int x, int y) throws DataProcessingException {
		if (model.isFirstStep()) {
			model.init(x, y, mineClass);
			view.runTimer();
		}
	}
	
	private void synchronizeModelAndView() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		flaggedCellsCount = 0;
		for (int col = 0; col < model.getData().getColsCount(); col++) {
			for (int row = 0; row < model.getData().getRowsCount(); row++) {
				displayModelOnControlWidget(col, row);						
			}
		}
		view.sendFlaggedCellsCountEvent(flaggedCellsCount);
	}

	private void displayModelOnControlWidget(int col, int row) throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		MinerModelCell modelCell = model.getCell(col, row);
		if (modelCell.isOpened() && model.isGameOver()) {
			view.drawRedAndMined(col, row);						
		} else { 
			if (modelCell.isClosed()) 
				view.drawUnpushed(col, row);
			else if (modelCell.isOpened()) {
				if (modelCell.hasMinesAround())
					view.drawAroundMinesCount(col, row, modelCell.getMinesAroundCount());
				else view.drawPushed(col, row);
			} else if (modelCell.isFlagged()) {
				view.drawFlagged(col, row);
				flaggedCellsCount++;
			}
			else if (modelCell.isQuestioned())
				view.drawQuestioned(col, row);
		}
	}
	
	private void openAllCells() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		for (int col = 0; col < model.getData().getColsCount(); col++) {
			for (int row = 0; row < model.getData().getRowsCount(); row++) {
				MinerModelCell modelCell = model.getCell(col, row);
				if (modelCell.isMined())
					if (modelCell.equals(model.getActiveCell()))
						view.drawRedAndMined(col, row);
					else view.drawMined(col, row);
				if (modelCell.hasMinesAround())
					view.drawAroundMinesCount(col, row, modelCell.getMinesAroundCount());
				if (!modelCell.isMined() & !modelCell.hasMinesAround())
					view.drawPushed(col, row);;
			}
		}
	}
	
}
