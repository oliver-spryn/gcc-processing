package edu.gcc.processing.exceptions;

import edu.gcc.processing.develop.Message;

/**
 * A general exception class which will be extended by all other specific
 * exception classes. However, this class will ensure that a custom message
 * from the user and Java will be automatically output to the console, for
 * call stack debugging.
 *
 * @category   Exceptions
 * @package    edu.gcc.processing.exceptions
 * @access     public
 * @since      v0.1 Dev
 */

public class ExceptionBase extends Exception {
/**
 * A constructor method which passes a message to the super constructor and
 * a detailed message to the console
 *
 * @param      String      message     The message to send to the console
 * @param      Exception   e           A reference to the thrown exception class
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public ExceptionBase(String message, Exception e) {
	//Call the super constructor
		super(message);
		
	//Output the message to the console
		new Message("[Custom error message] " + message + "\n[Java error message]   " + e.getMessage() + "\n-----------------------------------------------");
	}
}