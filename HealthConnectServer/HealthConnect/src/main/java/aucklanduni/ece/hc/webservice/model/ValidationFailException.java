package aucklanduni.ece.hc.webservice.model;

public class ValidationFailException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationFailException(String s) {
		super(s);
	}
}