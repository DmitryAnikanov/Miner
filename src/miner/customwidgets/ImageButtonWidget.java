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
	private String btnUnpushedURL;
    private String btnPushedURL;
    private Gui gui = new Gui();
    
    protected ImageButtonWidget(Composite parent, int style) {
        super(parent, style);
        initListeners();		    
    }
    
    public ImageButtonWidget(Composite parent, int style, String btnUnpushedURL, String btnPushedURL) {
        super(parent, style);
        this.btnUnpushedURL = btnUnpushedURL;
        this.btnPushedURL = btnPushedURL;
        loadImages();
        initListeners();		    
    }
 
    protected void loadImages() {
    	try {
    		btnUnpushed = gui.loadImage(btnUnpushedURL);
    		btnPushed = gui.loadImage(btnPushedURL);    		
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
    
    public void draw(Image image) {
    	redraw(image);
    }
    
    public void drawUnpushed() {
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
