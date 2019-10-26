package miner.exceptions;

public class DataLoadException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4083855739613755405L;
	private String message;
	 
    public DataLoadException(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
