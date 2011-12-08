package edu.gcc.processing.exceptions.multicaster;

import edu.gcc.processing.exceptions.ExceptionBase;

/**
 * A specific exception to be thrown inside of the Multicaster class, whenever
 * connection to a specific IP address and port fails and all possible error
 * resolution methods have been tried
 * 
 * @category   Exceptions
 * @package    edu.gcc.processing.exceptions.multicaster
 * @access     public
 * @since      v0.1 Dev
 */

public class MulticasterInitException extends ExceptionBase {
/**
 * A constructor method which passes a message to the super constructor
 *
 * @param      String      message     The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public MulticasterInitException(String message, Exception e) {
		super(message, new Exception("System message not avaliable."));
	}
}