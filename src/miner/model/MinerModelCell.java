package miner.model;

import java.util.Arrays;
import java.util.List;

import miner.model.mines.Mine;

public class MinerModelCell {

	private Mine mine;
	private boolean isMined;
	
	private int x;
	private int y;
	private int minesAroundCount;
	
	private CellType cellType;
	public static enum CellType {Opened, Closed, Flagged, Questioned;}
	
	public MinerModelCell(int x, int y) {
		isMined = false;
		cellType = CellType.Closed;
		this.x = x;
		this.y = y;
	}

	public void setType(CellType cellType) {
		this.cellType = cellType;
	}
	
	public void setMine(Mine mine) {
		isMined = true;
		this.mine = mine;
	}
	
	public Mine getMine() {
		return mine;
	}
	
	public void setMinesAroundCount(int minesAroundCount) {
		this.minesAroundCount = minesAroundCount;
	}
	
	public int getMinesAroundCount() {
		return minesAroundCount;
	}
	
	public boolean hasMinesAround() {
		if (minesAroundCount > 0) return true;
		else return false;
	}
	
	public boolean isMined() {
		return isMined;
	}
	
	
	public void open() {
		if (cellType != CellType.Flagged)
			cellType = CellType.Opened;
	}
	
	public void iterateType() {
		int index;
		List<CellType> typeValues = Arrays.asList(CellType.values()).subList(1, CellType.values().length);
		if (cellType != CellType.Opened) {
			index = typeValues.indexOf(cellType);
			index++;
			if (index <= typeValues.size()) {
				if (index == typeValues.size())
					index = 0;
				cellType = typeValues.get(index);
			}
		}					
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean isClosed() {
		return checkType(CellType.Closed);
	}
	
	public boolean isOpened() {
		return checkType(CellType.Opened);
	}
	
	public boolean isFlagged() {
		return checkType(CellType.Flagged);
	}
	
	public boolean isQuestioned() {
		return checkType(CellType.Questioned);
	}
	
	private boolean checkType(CellType type) {
		if (cellType.equals(type))
				return true;
		else return false;
	}
}
