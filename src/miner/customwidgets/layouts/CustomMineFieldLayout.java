package miner.customwidgets.layouts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import miner.customwidgets.MineFieldCellWidget;

public class CustomMineFieldLayout extends Layout {
    
	private int cols;
	private int rows;
	
	public CustomMineFieldLayout(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
	}

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean changed) {
		int width = 0;
		int height = 0;
		Point point;
		Control [] cells = composite.getChildren();
		if (cells.length > 0)
			if (changed) {
				point = cells[0].computeSize(SWT.DEFAULT, SWT.DEFAULT);
				width = point.x * cols;
				height = point.y * rows; 
				
			}
	    return new Point(width, height);
	}

	@Override
	protected void layout(Composite composite, boolean changed) {
		Control [] cells = composite.getChildren();
		if (cells.length > 0) {
			int cellW = cells[0].computeSize(SWT.DEFAULT, SWT.DEFAULT, false).x;
	     	int cellH = cells[0].computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y;
	     	for (Control cell : cells) {
	     		if (cell instanceof MineFieldCellWidget) {
	     			MineFieldCellWidget widget = (MineFieldCellWidget) cell;
	     			cell.setBounds(widget.getX()*cellW, widget.getY()*cellH, cellW, cellH);
	     		}
	     	}
		}
	}
}
