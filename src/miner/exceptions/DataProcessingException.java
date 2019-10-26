package miner.exceptions;

public class DataProcessingException extends Exception {
	

	private static final long serialVersionUID = -3337526217585253921L;
	private String message;
	 
    public DataProcessingException(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
