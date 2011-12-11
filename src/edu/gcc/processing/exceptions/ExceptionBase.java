package edu.gcc.processing.exceptions;

import processing.core.PApplet;
import edu.gcc.processing.develop.Message;
import edu.gcc.processing.gui.TabAlerts;

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
 * A constructor method which passes a message to the super constructor,
 * a detailed message to the console, and a friendly message to the user
 *
 * @param      String      message     The message to send to the console
 * @param      Exception   e           A reference to the thrown exception class
 * @param      PApplet     applet      A reference to the object which extends the PApplet class
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public ExceptionBase(String message, Exception e, PApplet applet) {
	//Call the super constructor
		super(message);
		
	//Output the message to the console
		new Message("[Custom error message] " + message + "\n[Java error message]   " + e.getMessage() + "\n-----------------------------------------------");
		
	//Output this message to the user
		TabAlerts error = new TabAlerts(applet);
		error.borderColor = 0xFFCC0033;
		error.fillColor = 0xFFFF3333;
		error.textColor = 0xFFFFFFFF;
		error.message = message;
		error.build();
	}
}