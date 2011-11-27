package edu.gcc.processing.exceptions.multicaster;

import edu.gcc.processing.exceptions.ExceptionBase;

/**
 * A specific exception to be thrown inside of the Multicaster class, whenever
 * sending data to a multicast group fails
 *
 * @category   Exceptions
 * @package    edu.gcc.processing.exceptions.multicaster
 * @access     public
 * @since      v0.1 Dev
 */

public class MulticasterSendException extends ExceptionBase {
/**
 * A constructor method which passes a message to the super constructor
 *
 * @param      String      message     The message to send to the console
 * @param      Exception   e           A reference to the thrown exception class
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public MulticasterSendException(String message, Exception e) {
		super(message, e);
	}
}
