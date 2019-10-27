package miner.customwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import miner.view.gui.Gui;

public  class ImageButtonWidget extends Canvas {

	
	private int mouse = 0;   
	protected Image btnUnpushed;
	protected Image btnPushed;
	protected Image btnGameIsOver;
    private final String btnUnpushedURL = "images/buttonSmileOk_unpushed.bmp";
    private final String btnPushedURL = "images/buttonSmileOk_pushed.bmp";
    private final String btnGameIsOverURL = "images/buttonGameIsOver.bmp";
    private Gui gui = new Gui();
    
    public ImageButtonWidget(Composite parent, int style) {
        super(parent, style);
        loadImages();
        initListeners();		    
    }
 
    protected void loadImages() {
    	try {
    		btnUnpushed = gui.loadImage(btnUnpushedURL);
    		btnPushed = gui.loadImage(btnPushedURL);
    		btnGameIsOver = gui.loadImage(btnGameIsOverURL);
    	} catch (Exception e) {
    		
    	}
    }
    
    protected void initListeners() {
    	this.addPaintListener(createPaintListener());	   
	    this.addMouseListener(createMouseAdapter());
	    this.addListener(SWT.Dispose, event-> {
	    	ImageButtonWidget.this.widgetDisposed(event);		         
	    });
    }
    
    protected void widgetDisposed(Event event) {
    	btnUnpushed.dispose();
    	btnPushed.dispose();
    	btnGameIsOver.dispose();
    }
     
    protected PaintListener createPaintListener() {
    	return new PaintListener() {
    		public void paintControl(PaintEvent e) {
    			switch (mouse) {
        			case Gui.MOUSE_DEFAULT:
        				paintUnpushed(e);        					 
        				break;
        			case Gui.MOUSE_LEFT:
        				paintPushed(e);	        				
        				break;
        			case Gui.MOUSE_RIGHT_MASKED:        				
        				paintPushed(e);
        				break;
        			case Gui.MOUSE_MIDDLE:
        				paintPushed(e);        				
        				break;	        			
                }
    		}
    	};
    }
    	 	    
    protected MouseAdapter createMouseAdapter() {
    	return new MouseAdapter() {
            public void mouseDown(MouseEvent e) {            	
            	mouse = maskIfMouseRight(e.button);
            	redraw();
            }
            public void mouseUp(MouseEvent e) {            	
            	notifyListeners(maskIfMouseRight(e.button), new Event());
            	mouse = Gui.MOUSE_DEFAULT;
            	redraw();
            }
        };
    }
    
    private int maskIfMouseRight (int code) {
    	if (code == 3)
    		return 33;
    	return code;
    }
    
    protected void drawImage(PaintEvent event, Image image) {
    	try {
    		event.gc.drawImage(image, 0, 0);
    	} catch (Exception e) {
    		
    	}
    }
    
    protected void paintUnpushed(PaintEvent event) {
    	drawImage(event, btnUnpushed);	    		    	
    }
    protected void paintPushed(PaintEvent event) {
    	drawImage(event, btnPushed);	    
    }
    
    public void drawGameIsOver() {
    	redraw(btnGameIsOver);
    }
    
    public void drawDefault() {
    	redraw(btnUnpushed);
    }
    
    protected void redraw (Image image) {
	    this.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
            	drawImage(e, image);
            }
	    });
	    redraw();
    	
    }
    
    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
    	int width = 0, height = 0;
    	if (btnUnpushed != null) {
    		Rectangle bounds = btnUnpushed.getBounds();
    		width = bounds.width;
    		height = bounds.height;
    	}	     
        return new Point(width, height);
    } 
 
}
