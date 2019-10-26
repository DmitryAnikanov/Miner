package miner.exceptions;

public class MineFieldWidgetCellException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 121201272795558617L;
	private String message;

	public MineFieldWidgetCellException(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
