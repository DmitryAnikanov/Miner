package miner.model;

import miner.Application;
import miner.exceptions.DataProcessingException;

public class GameSettings {
	
	private int colsCount;
	private int rowsCount;
	private int cellsNumber;
	
	private int minesCount;
	private final int max = 30;
	private final int min = 8;
	
	public GameSettings(int cols, int rows, int minesCount) throws DataProcessingException {
		this.colsCount = cols;
		this.rowsCount = rows;
		this.minesCount = minesCount;
		this.cellsNumber = cols*rows;
		checkMinesCount();
		checkFieldSize(colsCount);
		checkFieldSize(rowsCount);
	}
	
	public GameSettings(int cols, int rows, int minesCount, boolean skipLimitsCheking) throws DataProcessingException {
		this.colsCount = cols;
		this.rowsCount = rows;
		this.minesCount = minesCount;
		this.cellsNumber = cols*rows;
		checkMinesCount();
		if (!skipLimitsCheking) {
			checkFieldSize(colsCount);
			checkFieldSize(rowsCount);
		}
	}
		
	private void checkMinesCount() throws DataProcessingException {
		if (minesCount >= cellsNumber)
			throw new DataProcessingException(Application.INSTANCE.messages.warning_wrong_mines_count);
	}
	
	private void checkFieldSize(int size) throws DataProcessingException {
		if ( size > max || size < min)
			throw new DataProcessingException(Application.INSTANCE.messages.warning_wrong_mineField_size);
	}
	
	public Integer getMinesCount() {
		return minesCount;
	}
	
	public int getColsCount() {
		return colsCount;	
	}
	
	public int getRowsCount() {
		return rowsCount;
	}
	
}
