package miner.view.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import miner.Application;
import miner.exceptions.DataLoadException;

public class Gui {
	
	public static final int MOUSE_DEFAULT = 0;
	public static final int MOUSE_LEFT = 1;
	public static final int MOUSE_RIGHT_MASKED = 33;
	public static final int MOUSE_RIGHT = 3;
	public static final int MOUSE_MIDDLE = 2;
	
	//public static final Gui INSTANCE = new Gui(); 
	
	/*public Gui() {
		
	}*/
	
	public  void centerShell(Shell shell) {
		try {			
			Monitor primary = Display.getDefault().getPrimaryMonitor();
		    Rectangle bounds = primary.getBounds();
		    Rectangle rect = shell.getBounds();
		    int x = bounds.x + (bounds.width - rect.width) / 2;
		    int y = bounds.y + (bounds.height - rect.height) / 2;
		    shell.setLocation(x, y);		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public Image loadImage(String url) throws DataLoadException {
		Image newImage = null;
	    try {	       
	        //newImage = new Image(null, Application.class.getClassLoader().getResourceAsStream(url));
	    	newImage = new Image(Display.getDefault(), Application.class.getClassLoader().getResourceAsStream(url));
	    } catch(SWTException ex) {
	        throw new DataLoadException(ex.getMessage());
	    }
	    return newImage;
    }
	
	public Listener getDigitListener(){
		return new Listener () {
	        public void handleEvent (Event e) {
	            String string = e.text;
	            char [] chars = new char [string.length ()];
	            string.getChars (0, chars.length, chars, 0);
	            for (int i=0; i<chars.length; i++) {
	                if (!('0' <= chars [i] && chars [i] <= '9')) {
	                	e.doit = false;
	                  return;
	                }
	            }
	        }
		};
	}
	
	public Shell createOsSpecificDialog () {
		
		String operatingSystem = System.getProperty("os.name");   
	    if (operatingSystem.contains("Linux")) {
	    	return new Shell(Display.getDefault(), SWT.DIALOG_TRIM|SWT.APPLICATION_MODAL);//Linux kde,gnome  	
	    }
	    else if (operatingSystem.contains("Windows")) {
	    	//return new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.TOOL  | style);
	    	return new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.TOOL | SWT.ON_TOP |SWT.APPLICATION_MODAL );	        
	    }
	    else {
	    	return new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.TOOL | SWT.ON_TOP |SWT.APPLICATION_MODAL );
	        //throw new RuntimeException("Unsupported operating system.");
	    }
	}
	
	public GridData getGridData() {
		return new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
	}
	
	public GridLayout getGridLayout(int numColumns, boolean isEqual) {
		GridLayout gridLayout = new GridLayout(numColumns, isEqual);
		gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.marginBottom = 0;
        return gridLayout;
	}
	
	public Image createGCWithTextOnImage(String imageUrl, int count) throws DataLoadException {
    	Image image = loadImage(imageUrl);	    
    	GC gc = new GC(image);
    	int fontSize = 9;
    	Font font = new Font(Display.getDefault(), "Tahoma", fontSize, SWT.BOLD);
    	gc.setFont(font);
    	switch (count) {
	    	case 1:
	    		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
	    		break;
	    	case 2:
	    		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN));
	    		break;
	    	case 3:
	    		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
	    		break;
	    	case 4:
	    		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE));
	    		break;
	    	case 5:
	    		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_MAGENTA));
	    		break;
	    	case 6:
	    		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
	    		break;
	    	case 7:
	    		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED));
	    		break;
	    	case 8:
	    		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_CYAN));
	    		break;
    	}
    	gc.drawText(String.valueOf(count), image.getBounds().width/2 - fontSize/2, 0, true);
    	gc.dispose();
    	font.dispose();
    	return image;
    }
	
	public Color getOriginalMineSweeperGrayColor() {
		return new Color(Display.getDefault(), 192, 192, 192);
	}
	
	public Color getOriginalMineSweeperRedColor() {
		return new Color(Display.getDefault(), 255, 0, 0);
	}
	
	
}
