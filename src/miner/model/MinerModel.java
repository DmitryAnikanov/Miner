package miner.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import miner.exceptions.DataProcessingException;
import miner.model.mines.MineFactory;

public class MinerModel {

	private boolean isWin = false;
	private boolean isGameOver = false;
	
	private MinerModelCell activeCell;

	private boolean isFirstStep = true;
	
	private MinerData data;
	private MineFactory mineFactory = new MineFactory();
	private ArrayList<MinerModelCell> activeCells = new ArrayList<MinerModelCell>();
	public MinerModel(MinerData data) throws DataProcessingException {
		this.data = data;
	}
	
	public MinerData getData() {
		return data;
	}
	
	public GameSettings getGameSettings() {
		return data.getGameSettinngs();
	}
	
	private void createEmptyMineField() {		
		createEmptyCellsForMineField();
	}
	
	private void createEmptyCellsForMineField() {
		for (int col = 0; col < data.getColsCount(); col++) {
			for (int row = 0; row < data.getRowsCount(); row++)
				data.getMineField()[col][row] = new MinerModelCell(col, row);
		}
	}
	
	private void allocateMines(int currentCol, int currentRow, Class mineClass) throws DataProcessingException {
		if (currentCol < 0 && currentRow < 0)
			throw new DataProcessingException("Couldn't allocate mines around negative coordinates "
											+ "currentCol = " + currentCol + "currentRow = " + currentRow);
		ArrayList<MinerModelCell> mineFieldAsList = convertMineFieldArrayToList();
		mineFieldAsList.remove(getCell(currentCol, currentRow));
		
		ArrayList<Integer> indexesList = generateRandomIndexList(mineFieldAsList);
		createMines(indexesList, mineFieldAsList, mineClass);
	}
	
	private ArrayList<MinerModelCell> convertMineFieldArrayToList() {
		List<MinerModelCell[]> columns = Arrays.asList(getMineField());
		ArrayList<MinerModelCell> mineFieldAsList = new ArrayList<MinerModelCell>();
		for (MinerModelCell[] cell : columns) {
			mineFieldAsList.addAll(Arrays.asList(cell));
		}
		return mineFieldAsList;
	}
	
	private ArrayList<Integer> generateRandomIndexList(ArrayList<MinerModelCell> mineFieldAsList) {
		ArrayList<Integer> indexesList = new ArrayList<Integer>();
		Random random = new Random();
	    while (indexesList.size() < getGameSettings().getMinesCount()) { 
	        int index = random.nextInt(mineFieldAsList.size()); 
	        if (!indexesList.contains(index)) {
	        	indexesList.add(index);
	        }
	    }
	    return indexesList;
	}
	
	private void createMines(ArrayList<Integer> indexesList, ArrayList<MinerModelCell> mineFieldAsList, Class mineClass) {
		for (Integer index : indexesList)
			 mineFieldAsList.get(index).setMine(mineFactory.createMine(mineClass));
	}
	
	private void calculateMinesAroundEveryCell() {
		for (int col = 0; col < data.getColsCount(); col++) {
			for (int row = 0; row < data.getRowsCount(); row++) {
				if (!data.getMineField()[col][row].isMined()) {					
					data.getMineField()[col][row].setMinesAroundCount(calculateMinesAround(col, row));					
				}
			}
		}
	}
	
	private int calculateMinesAround(int col, int row) {
		int minesAroundCount = 0;
		for (MinerModelCell cell : defineCellsAroundList(col, row))
			if (cell.isMined()) {
				minesAroundCount+=1;
			}
		return minesAroundCount;
	}
	
	private List<MinerModelCell> defineCellsAroundList(int x, int y) {
		List<MinerModelCell> cells = new ArrayList<MinerModelCell>();
		for (int col = x - 1; col <= x + 1; col++) {
			for (int row = y - 1; row <= y + 1 ; row++) {
				 try {
					 cells.add(data.getMineField()[col][row]);
				 } catch (Exception e) {
					 
				 }
			}
		}
		cells.remove(data.getMineField()[x][y]);
		return cells;
	}
	
	public void iterateCellType(int x, int y) throws DataProcessingException {
		isFirstStep = false;
		data.getMineField()[x][y].iterateType();
		activeCells.remove(getCell(x, y));
		activeCells.add(getCell(x, y));
	}

	public void init(int currentCol, int currentRow, Class mineClass) throws DataProcessingException {
		createEmptyMineField();
		allocateMines(currentCol, currentRow, mineClass);
		calculateMinesAroundEveryCell();
		isFirstStep = false;
	}

	public void openCell(int x, int y) throws DataProcessingException {
		isFirstStep = false;
		activeCell = getCell(x, y);
		activeCells.add(activeCell);
		if(!activeCell.isFlagged() && !activeCell.isQuestioned()) {
			activeCell.open();
			if (activeCell.isMined() && activeCell.getMine().explode()) {
				isGameOver = true;
				activeCells.clear();
				return;
			}
			tryToOpenEmptyCells();
		} 
		
	}
	
	private void tryToOpenEmptyCells() throws DataProcessingException {
		if (!activeCell.hasMinesAround()) {
			for (MinerModelCell neighboor : defineCellsAroundList(activeCell.getX(), activeCell.getY())) {
				if (neighboor.isClosed())
					openCell(neighboor.getX(), neighboor.getY());
			}
		}
	}

	public MinerModelCell getActiveCell() {
		return activeCell;
	}
	
	public MinerModelCell[][] getMineField() {
		return data.getMineField();
	}
	
	public boolean isWin() {
		Integer hiddenCellsCount = 0;
		for (int col = 0; col < data.getColsCount(); col++) {
			for (int row = 0; row < data.getRowsCount(); row++) {
				MinerModelCell cell = data.getMineField()[col][row];				
				if (cell.isClosed() || cell.isFlagged() && cell.isMined())
					hiddenCellsCount++;
			}
		}
		if (hiddenCellsCount.equals(data.getGameSettinngs().getMinesCount())) {
			isWin = true;
			activeCells.clear();
		} else isWin = false;
		return isWin;
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public MinerModelCell getCell(int col, int row) throws DataProcessingException {
		try {
			return  data.getMineField()[col][row];
		} catch (Exception e) {
			throw new DataProcessingException("Wrong coordinares of mineField");
		}
	}
	
	public boolean isFirstStep() {
		return isFirstStep;
	}
	
	public void isFirstStep(boolean isFirstStep) {
		this.isFirstStep = isFirstStep;
	}
	
	public ArrayList<MinerModelCell> getActiveCells() {
		return activeCells;
	}
	
	
}
