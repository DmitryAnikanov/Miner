package miner.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import miner.exceptions.DataProcessingException;
import miner.model.MinerModelCell.CellType;
import miner.model.mines.SimpleMine;

public class MinerModelTest {

	private GameSettings settings;
	private MinerData data;

	@Before
	public void setUp() {
		mineClass = SimpleMine.class;
	}
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	private Class<SimpleMine> mineClass;
	
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
	
 private MinerModel create_N_By_3ModelOnlyMined(int colsCount, boolean isFirstStep) throws DataProcessingException {
		
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
		model.getCell(1, 1).setMine(new SimpleMine());
		model.getCell(2, 2).setMine(new SimpleMine());
				
		return model;
	}
	
	private MinerModel create3By3Model(boolean isFirstStep) throws DataProcessingException {
		return create_N_By_3Model(3,isFirstStep);
	}
	
	private MinerModel create3By4Model(boolean isFirstStep) throws DataProcessingException {
		return create_N_By_3Model(4,isFirstStep);
	}
	
	private MinerModel create3By3ModelOnlyMined(boolean isFirstStep) throws DataProcessingException {
		return create_N_By_3ModelOnlyMined(3,isFirstStep);
	}
	
	@Test 
	public void init_negativeCoords__allocateMines() throws DataProcessingException  {
		MinerModel model = null;
		model = create3By3Model(false);
		exception.expect(DataProcessingException.class);	
		model.init(-1, -2, mineClass);
	}
	
	@Test 
	public void init_normalCoords__allocateMines() throws DataProcessingException  {
		boolean isFailed = true;
		int minesCount = 0;
		MinerModel model = null;
		model = create3By3ModelOnlyMined(false);	
		model.init(0, 0, mineClass);
		for (MinerModelCell[] column : model.getMineField()) {
			for(MinerModelCell cell : column) {
				if (cell.isMined())
					minesCount++;
			}
		}
		if (minesCount == model.getGameSettings().getMinesCount())
			isFailed = false;
		assertFalse(isFailed);
	}
	
	@Test 
	public void init__calculateMinesAroundEveryCell() throws Exception  {
		boolean isFailed = true;
		int cellsWithMinesAround = 0;
		settings = new GameSettings(2, 2, 1, true);
		data = new MinerData(settings);
		MinerModel model = new MinerModel(data);
		model.init(0, 0, mineClass);
		for (MinerModelCell[] column : model.getMineField()) {
			for(MinerModelCell cell : column) {
				if (cell.hasMinesAround()) {
					if (cell.getMinesAroundCount() == 1);
					cellsWithMinesAround++;
				}
			}
		}
		if (cellsWithMinesAround == 3)
			isFailed = false;
		assertFalse(isFailed);
	}
	
	@Test
	public void iterateCellType_closedCell_ToFlagged__iterate() throws DataProcessingException {
		boolean isIterated = false;
		MinerModel model = create3By3Model(false);
		if (model.getCell(0, 0).isClosed())
			model.iterateCellType(0, 0);
		if(model.getCell(0, 0).isFlagged())
			isIterated = true;
		assertTrue(isIterated);
	}
	
	@Test
	public void iterateCellType_openedCell_ToFlagged__iterate() throws DataProcessingException {
		boolean isIterated = false;
		MinerModel model = create3By3Model(false);
		model.getCell(0, 0).setType(CellType.Opened);
		if(model.getCell(0, 0).isFlagged())
			isIterated = true;
		assertFalse(isIterated);
	}

	@Test
	public void iterateCellType_questionedCell_ToClosed__iterate() throws DataProcessingException {
		boolean isIterated = false;
		MinerModel model = create3By3Model(false);
		model.getCell(0, 0).setType(CellType.Questioned);
		if(model.getCell(0, 0).isClosed())
			isIterated = true;
		assertFalse(isIterated);
	}
	
	@Test
	public void openCell_negativeCoordinates() throws DataProcessingException {
		MinerModel model = create3By3Model(false);
		model.init(0, 0, mineClass);
		exception.expect(DataProcessingException.class);	
		model.openCell(-1, -8);
	}
	
	@Test
	public void openCell__isWin() throws DataProcessingException {
		boolean isFailed = true;
		MinerModel model = create3By3Model(false);
			
		model.openCell(0, 1);
		model.openCell(0, 2);
		
		model.openCell(1, 0);
		model.openCell(1, 2);
		
		model.openCell(2, 0);
		model.openCell(2, 1);
		
		if (model.isWin())
			isFailed = false;
		assertFalse(isFailed);
	}
	
	@Test
	public void isWin() throws DataProcessingException {
		boolean isFailed = true;
		MinerModel model = create3By4Model(false);
		model.openCell(3, 0);
		if (model.getCell(3, 1).isOpened() && model.getCell(3, 2).isOpened())
			isFailed = false;
		assertFalse(isFailed);
	}
	
	@Test
	public void isGameOver() throws DataProcessingException {
		boolean isFailed = true;
		MinerModel model = create3By3Model(false);
		model.openCell(0, 0);
		if (model.isGameOver())
			isFailed = false;
		assertFalse(isFailed);
	}
}
