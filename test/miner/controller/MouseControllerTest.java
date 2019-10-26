package miner.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import miner.exceptions.DataLoadException;
import miner.exceptions.DataProcessingException;
import miner.exceptions.MineFieldWidgetCellException;
import miner.model.GameSettings;
import miner.model.MinerData;
import miner.model.MinerModel;
import miner.model.MinerModelCell;
import miner.model.MinerModelCell.CellType;
import miner.model.mines.SimpleMine;
import stubs.MineFieldViewStub;

public class MouseControllerTest {

	private GameSettings settings;
	private MinerData data;
	private MinerModel model;
	private MouseController controller;
	private MineFieldViewStub viewStub;
	
	@Before
	public void setUp() throws DataProcessingException {
		model = create3By3Model(false);
		controller = new MouseController(model);
		viewStub = new MineFieldViewStub(null, settings);
		controller.setView(viewStub);
	}
	
	@After 
	public void tearDown() {
		settings = null;
		data = null;
		model = null;
		controller = null;
	}
	
	public void setUpController(MinerModel model) {
		controller = new MouseController(model);
		viewStub = new MineFieldViewStub(null, settings);
		controller.setView(viewStub);
	}
	
	private MinerModel create_N_By_3Model(int colsCount, boolean isFirstStep) throws DataProcessingException {
		
		settings = new GameSettings(colsCount, 3, 3, true);
		data = new MinerData(settings);
		MinerModel model = new MinerModel(data);
		
		model.isFirstStep(isFirstStep);
		for (int col = 0; col < data.getColsCount(); col++) {
			for (int row = 0; row < data.getRowsCount(); row++) {
				data.getMineField()[col][row] = new MinerModelCell(col, row);				
			}
		}
		
		model.getCell(0, 0).setMine(new SimpleMine());
		model.getCell(0, 1).setMinesAroundCount(2);
		model.getCell(0, 2).setMinesAroundCount(1);
		
		model.getCell(1, 0).setMinesAroundCount(2);
		model.getCell(1, 1).setMine(new SimpleMine());
		model.getCell(1, 2).setMinesAroundCount(2);
		
		model.getCell(2, 0).setMinesAroundCount(1);
		model.getCell(2, 1).setMinesAroundCount(2);
		model.getCell(2, 2).setMine(new SimpleMine());
				
		return model;
	}
	
	private MinerModel create_N_By_8_Model(int colsCount, boolean isFirstStep) throws DataProcessingException {
		
		settings = new GameSettings(colsCount, 8, 8);
		data = new MinerData(settings);
		MinerModel model = new MinerModel(data);
		
		model.isFirstStep(isFirstStep);
		for (int col = 0; col < data.getColsCount(); col++) {
			for (int row = 0; row < data.getRowsCount(); row++) {
				data.getMineField()[col][row] = new MinerModelCell(col, row);				
			}
		}
		
		model.getCell(0, 0).setMine(new SimpleMine());
		model.getCell(0, 1).setMinesAroundCount(2);
		model.getCell(0, 2).setMinesAroundCount(1);
		
		model.getCell(1, 0).setMinesAroundCount(2);
		model.getCell(1, 1).setMine(new SimpleMine());
		model.getCell(1, 2).setMinesAroundCount(2);
		
		model.getCell(2, 0).setMinesAroundCount(1);
		model.getCell(2, 1).setMinesAroundCount(2);
		model.getCell(2, 2).setMine(new SimpleMine());
				
		return model;
	}
	
	private MinerModel create3By3Model(boolean isFirstStep) throws DataProcessingException {
		return create_N_By_3Model(3, isFirstStep);
	}
	
	private MinerModel create4By3Model(boolean isFirstStep) throws DataProcessingException {
		return create_N_By_3Model(4, isFirstStep);
	}
	
	@Test
	public void openCell_firstStep__runTimer() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		setUpController(create3By3Model(true));
		controller.openCell(0,0);
		assertTrue(viewStub.runTimerFired);
	}
	
	@Test
	public void openCell_cellWithMine__drawRedAndMined() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		setUpController(model);
		controller.openCell(0,0);
		assertTrue(viewStub.drawRedAndMinedFired);	
	}
	
	@Test
	public void openCell_questionRemoved__drawUnpushed() {
		
	}
	
	@Test
	public void openCell_emptyCell__drawAroundMinesCount() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		setUpController(create3By3Model(false));
		controller.openCell(1,0);
		assertEquals(viewStub.drawAroundMinesCount, 1);
	}
	
	@Test
	public void openCell_emptyCell_noMinesAround__drawPushed() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		setUpController(create4By3Model(false));
		controller.openCell(3,0);
		assertEquals(viewStub.drawPushedCount, 3);
	}
	
	@Test
	public void openCell_cellIsFlagged__drawFlagged() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		MinerModel model = create3By3Model(false);
		model.getCell(2,0).setType(CellType.Flagged);
		setUpController(model);
		controller.openCell(2,0);
		assertTrue(viewStub.drawFlaggedFired);	
	}
	
	@Test
	public void openCell_cellIsQuestioned__drawQuestioned() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		MinerModel model = create3By3Model(false);
		model.getCell(0,0).setType(CellType.Questioned);
		setUpController(model);
		controller.openCell(0,0);
		assertTrue(viewStub.drawQuestionedFired);	
	}

	@Test
	public void openCell__sendFlaggedCellsCount() {
		assertEquals(viewStub.flagCellsCount, 0);
	}
	
	@Test
	public void openCell_isWin__stopTimer() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		setUpController(create3By3Model(false));
		controller.openCell(0, 1);
		controller.openCell(0, 2);
		controller.openCell(1, 0);
		controller.openCell(1, 2);
		controller.openCell(2, 0);
		controller.openCell(2, 1);
		assertTrue(viewStub.stopTimerFired);
	}
	
	@Test
	public void openCell_isWin__showWinMessage() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		setUpController(create3By3Model(false));
		controller.openCell(0, 1);
		controller.openCell(0, 2);
		controller.openCell(1, 0);
		controller.openCell(1, 2);
		controller.openCell(2, 0);
		controller.openCell(2, 1);
		assertTrue(viewStub.showWinMessageFired);
	}
	
	@Test
	public void openCell_isGameOver__stopTimer() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		setUpController(create3By3Model(false));
		controller.openCell(1, 1);
		assertTrue(viewStub.stopTimerFired);
	}
	
	@Test
	public void openCell_isGameOver__sendGameOverEvent() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		setUpController(create3By3Model(false));
		controller.openCell(1, 1);
		assertTrue(viewStub.sendGameOverEventFired);
	}
	
	@Test
	public void openCell_isMineOpened__openAllCells() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		setUpController(create3By3Model(false));
		controller.openCell(0,0);
		
		assertEquals(viewStub.drawRedAndMinedCount, 1);
		assertEquals(viewStub.drawMinesCount, 2);
		assertEquals(viewStub.drawAroundMinesCount, 6);
		assertEquals(viewStub.drawPushedCount, 0);

	}
	
	@Test
	public void openCell_isGameOver__showGameOverMessage() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		setUpController(create4By3Model(false));
		controller.openCell(0,0);
		assertTrue(viewStub.showGameOverMessageFired);
	}
	
	
	@Test
	public void closeCell_firstStep__runTimer() throws MineFieldWidgetCellException, DataProcessingException, DataLoadException {
		setUpController(create4By3Model(true));
		controller.closeCell(0,0);
		assertTrue(viewStub.runTimerFired);
	}
	
	@Test
	public void closeCell_cellWithMine__drawRedAndMined() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		setUpController(create4By3Model(false));
		controller.openCell(0,0);
		assertEquals(viewStub.drawRedAndMinedCount, 1);
	}
	
	@Test
	public void closeCell_questionRemoved__drawUnpushed() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		MinerModel model = create4By3Model(false);
		model.getCell(3, 0).setType(CellType.Questioned);
		setUpController(model);
		controller.closeCell(3,0);
		assertTrue(viewStub.drawUnpushedFired);
	}
	
	@Test
	public void closeCell_emptyCell__drawAroundMinesCount() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		setUpController(create4By3Model(false));
		controller.closeCell(3,0);
		assertEquals(viewStub.drawRedAndMinedCount, 0);
	}
	
	@Test
	public void closeCell_emptyCell_noMinesAround__drawPushed() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		setUpController(create4By3Model(false));
		controller.closeCell(3,0);
		assertEquals(viewStub.drawRedAndMinedCount, 0);
	}
	
	@Test
	public void closeCell_cellIsFlagged__drawFlagged() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		MinerModel model = create3By3Model(false);
		model.getCell(0, 0).setType(CellType.Closed);
		setUpController(model);
		controller.closeCell(0,0);
		assertTrue(viewStub.drawFlaggedFired);
	}
	
	@Test
	public void closeCell_cellIsQuestioned__drawQuestioned() throws DataProcessingException, MineFieldWidgetCellException, DataLoadException {
		MinerModel model = create4By3Model(false);
		model.getCell(0, 0).setType(CellType.Flagged);
		setUpController(model);
		controller.closeCell(0,0);
		assertTrue(viewStub.drawQuestionedFired);
	}
}
