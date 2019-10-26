package miner.customwidgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import miner.customwidgets.events.MineFieldWidgetSelectionEvent;
import miner.customwidgets.layouts.CustomMineFieldLayout;
import miner.customwidgets.listeners.MineFieldWidgetSelectionListener;
import miner.exceptions.MineFieldWidgetCellException;

public class MineFieldWidget extends Composite {
	
	private MineFieldCellWidget[][] cells;
	private int cols;
	private int rows;
	private MineFieldCellWidget activeCell;
	private List<MineFieldWidgetSelectionListener> listeners = new ArrayList<MineFieldWidgetSelectionListener>();;
	private CustomMineFieldLayout layout;
	
	public MineFieldWidget(Composite parent, int style, int cols, int rows) {
		super(parent, style);
		this.cols = cols;
		this.rows = rows;
		createArrayOfMineFieldCells();
		createDisposeListener();
		this.layout = new CustomMineFieldLayout(cols, rows);
		this.setLayout(layout);
	}
		
	private void createArrayOfMineFieldCells() {
		cells = new MineFieldCellWidget[cols][rows];
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				cells[col][row] = new MineFieldCellWidget(this, 0, col, row);
				cells[col][row].pack();
				int x = col; int y = row;
				cells[col][row].addListener(SWT.MouseDown, event-> {	
					activeCell = cells[x][y];					
					fireImageButtonSelectionEvent(new MineFieldWidgetSelectionEvent(activeCell, event.button));
				});
			}
		}
	}
	
	private void createDisposeListener() {
		this.addListener(SWT.Dispose, event-> {			
			MineFieldWidget.this.widgetDisposed(event);		         
		});
	}
	
	private void widgetDisposed(Event event) {
		
	}

	public MineFieldCellWidget getActiveCell() {
		return activeCell;
	}
	
	public MineFieldCellWidget[][] getCells() {
		return cells;
	}
	
	protected PaintListener createPaintListener() {
		return new PaintListener() {
			public void paintControl(PaintEvent e) {
				redraw();
	    	}
	    };
	}
	
	protected void fireImageButtonSelectionEvent(MineFieldWidgetSelectionEvent selectionEvent) {
        try {
	    	for (MineFieldWidgetSelectionListener listener : listeners) {
	        	listener.mineFieldWidgetSelected(selectionEvent);
	        }
        } catch (Exception e) {
        	
        }
    }
    
    public void addSelectionListener(MineFieldWidgetSelectionListener l) {
		listeners.add(l);
	}
    
    public List<MineFieldWidgetSelectionListener> getSelectionListeners() {
    	return listeners;
    }
    
    public void clearSelectionListeners() {
    	listeners.clear();
    	listeners = new ArrayList<MineFieldWidgetSelectionListener>();
    }
    
    public MineFieldCellWidget getCell(int col, int row) throws MineFieldWidgetCellException {
    	if (col <= cols && row <= rows && col >= 0 && row >= 0) {
	    	if(cells.length != 0) {
	    		return cells[col][row];
	    	}
    	} else throw new MineFieldWidgetCellException("Wrong col & row argument");
		return null;
    }
  
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {		   
		int width = 0;
		int height = 0;
		if (cells.length != 0) {
			 width = cells[0][0].computeSize(0, 0).x*cols;
			 height = cells[0][0].computeSize(0, 0).y*rows;
		} 
		return new Point(width, height);
	}
}
