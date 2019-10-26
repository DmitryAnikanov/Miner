package miner.customwidgets.events;

import java.util.EventObject;

import miner.customwidgets.MineFieldCellWidget;

public class MineFieldWidgetSelectionEvent extends EventObject {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4032720919082216023L;
	private MineFieldCellWidget cell;
	private int button;

	public MineFieldWidgetSelectionEvent(MineFieldCellWidget cell, int button) {
        super(cell);
        this.cell = cell;
        this.button = button;
    }

    public MineFieldCellWidget getCell() {
        return cell;
    }
    
    public int getButton() {
    	return button;
    }
}