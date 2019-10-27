package miner.customwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import miner.exceptions.DataLoadException;
import miner.view.gui.Gui;

public class MineFieldCellWidget extends ImageButtonWidget {

	private Image cellFlagged;
	private Image cellQuestioned;
	private Image cellMined;
	private Image cellRedAndMined;
	 
	private final String cellUnpushedUrl = "images/cellUnpressed.bmp";
	private final String cellPushedUrl = "images/cellPressed.bmp";
	private final String cellFlaggedUrl = "images/cellFlagged.bmp";
	private final String cellQuestionedUrl = "images/cellQuestioned.bmp";
	private final String cellMinedUrl  = "images/cellMined.bmp";
	private final String cellRedAndMinedUrl = "images/cellRedAndMined.bmp";
	
	private int x;
	private int y;
	
	private Gui gui = new Gui();
	
	public MineFieldCellWidget(Composite parent, int style, int x, int y) {
		super(parent, style);
		this.x = x;
    	this.y = y;
		loadImages();
		super.addListener(SWT.Dispose, event-> {
			MineFieldCellWidget.this.widgetDisposed(event);		         
	    });
	}
	
	@Override
	protected void widgetDisposed(Event event) {
    	btnUnpushed.dispose();
	    btnPushed.dispose();
	    cellFlagged.dispose();
	    cellQuestioned.dispose();
	    cellMined.dispose();
	    cellRedAndMined.dispose();
	}
    @Override
	protected void loadImages() {
    	try {
    		btnUnpushed = gui.loadImage(cellUnpushedUrl);
    		btnPushed = gui.loadImage(cellPushedUrl);
    		cellFlagged = gui.loadImage(cellFlaggedUrl);
    		cellQuestioned = gui.loadImage(cellQuestionedUrl);
    		cellMined  = gui.loadImage(cellMinedUrl);
    		cellRedAndMined = gui.loadImage(cellRedAndMinedUrl);
    	} catch (Exception e) {
    		
    	}
    }
	
	public void drawFlagged() {
		 redraw(cellFlagged);
    }
    
    public void drawQuestioned() {
    	redraw(cellQuestioned);
    }
    
    public void drawMined() {
    	redraw(cellMined);
    }
    
    public void drawUnpushed() {
    	
    	redraw(btnUnpushed);
    }
    
    public void drawPushed() {
    	redraw(btnPushed);
    }
    
    public void drawEmpty() {
    	redraw(btnPushed);
    }
    
    public void drawRedAndMined() {
    	redraw(cellRedAndMined);
    }
    
    public void drawAroundMinesCount(int count) throws DataLoadException {
    	redraw(btnPushed, count);
    }
    
    private void redraw (Image image, int count) throws DataLoadException {
    	redraw(gui.createGCWithTextOnImage(cellPushedUrl, count));
    }
    
    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }
}
