package edu.gcc.processing.exceptions.multicaster;

import edu.gcc.processing.exceptions.ExceptionBase;

/**
 * A specific exception to be thrown inside of the Multicaster class, whenever
 * connection to a specific port fails and nothing else can be done to bind to
 * the desired port
 *
 * @category   Exceptions
 * @package    edu.gcc.processing.exceptions.multicaster
 * @access     public
 * @since      v0.1 Dev
 */

public class MulticasterInitPortFatalException extends ExceptionBase {
/**
 * A constructor method which passes a message to the super constructor
 *
 * @param      String      message     The message to send to the console
 * @param      Exception   e           A reference to the thrown exception class
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public MulticasterInitPortFatalException(String message, Exception e) {
		super(message, e);
	}
}