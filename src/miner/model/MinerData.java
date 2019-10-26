package miner.model;

import miner.exceptions.DataProcessingException;

public class MinerData {

	private MinerModelCell[][] minefield;
	private int colsCount;
	private int rowsCount;
	private GameSettings gameSettings;
	
	public MinerData(GameSettings settings) throws DataProcessingException {
		if (settings == null)
			throw new DataProcessingException ("Game settings is null");
		this.colsCount = settings.getColsCount();
		this.rowsCount = settings.getRowsCount();
		this.gameSettings = settings;
		init();
	}
	
	private void init() {
		minefield = new MinerModelCell[colsCount][rowsCount];
	}
	
	public void createEmptyCells() {
		
	}
	
	public int getColsCount() {
		return colsCount;
	}
	
	public int getRowsCount() {
		return rowsCount;
	}
	
	public MinerModelCell[][] getMineField() {
		return minefield;
	}
	
	public GameSettings getGameSettinngs() {
		return gameSettings;
	}
}
