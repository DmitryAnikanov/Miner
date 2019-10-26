package stubs;

import org.eclipse.swt.widgets.Composite;

import miner.exceptions.MineFieldWidgetCellException;
import miner.model.GameSettings;
import miner.view.MineFieldView;

public class MineFieldViewStub extends MineFieldView {
	
	public boolean runTimerFired = false;
	
	
	public int flagCellsCount;
	
	public boolean drawMinesFired;
	public int     drawMinesCount;
	
	public boolean drawRedAndMinedFired = false;
	public int 	   drawRedAndMinedCount;
	
	public boolean drawPushedFired;
	public int 	   drawPushedCount;
	
	public boolean drawAroundMinesCountFired;
	public int     drawAroundMinesCount;


	public boolean showGameOverMessageFired = false;


	public boolean showWinMessageFired = false;


	public boolean stopTimerFired = false;


	public boolean sendGameOverEventFired = false;


	public boolean drawFlaggedFired = false;


	public boolean drawQuestionedFired = false;


	public boolean drawUnpushedFired = false;
	
	
	public MineFieldViewStub(Composite parent, GameSettings gameSettings) {
		super(parent, gameSettings);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void runTimer() {
		runTimerFired = true;
	}
	
	@Override
	protected void createDisposeListener() {
		
	}
	
	@Override
	public void drawUnpushed(int col, int row) {
		drawUnpushedFired  = true;
	}
	
	@Override
	public void drawAroundMinesCount(int col, int row, int minesAroundCount) throws MineFieldWidgetCellException {
		drawAroundMinesCountFired = true;
		drawAroundMinesCount++;
	}
	@Override
	public void drawFlagged(int col, int row) throws MineFieldWidgetCellException {
		drawFlaggedFired  = true;
	}
	@Override
	public void drawQuestioned(int col, int row) throws MineFieldWidgetCellException {
		drawQuestionedFired = true;
	}
	@Override
	public void drawMined(int col, int row) throws MineFieldWidgetCellException {
		drawMinesFired = true;
		drawMinesCount++;
	}
	
	@Override
	public void drawRedAndMined(int col, int row) throws MineFieldWidgetCellException {
		drawRedAndMinedFired  = true;
		drawRedAndMinedCount++;
	}
	@Override
	public void drawPushed(int col, int row) throws MineFieldWidgetCellException {
		drawPushedFired  = true;
		drawPushedCount++;	
	}
	
	@Override
	public void stopTimer() {
		stopTimerFired  = true;
	}
	
	@Override
	public void sendGameOverEvent() {
		sendGameOverEventFired  = true;
	}
	@Override
	public void showGameOverMessage() {
		showGameOverMessageFired = true;
		
	}
	@Override
	public void showWinMessage() {
		showWinMessageFired = true;
	}
}
