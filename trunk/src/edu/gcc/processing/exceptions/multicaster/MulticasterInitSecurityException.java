package edu.gcc.processing.exceptions.multicaster;

import processing.core.PApplet;
import edu.gcc.processing.exceptions.ExceptionBase;

/**
 * A specific exception to be thrown inside of the Multicaster class, whenever
 * connection to a specific IP address and port fails due to a security issue
 *
 * @category   Exceptions
 * @package    edu.gcc.processing.exceptions.multicaster
 * @access     public
 * @since      v0.1 Dev
 */

public class MulticasterInitSecurityException extends ExceptionBase {
/**
 * A constructor method which passes a message to the super constructor
 *
 * @param      String      message     The message to send to the console
 * @param      Exception   e           A reference to the thrown exception class
 * @param      PApplet     applet      A reference to the object which extends the PApplet class
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public MulticasterInitSecurityException(String message, Exception e, PApplet applet) {
		super(message, e, applet);
	}
}