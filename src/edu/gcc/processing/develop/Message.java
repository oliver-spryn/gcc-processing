package edu.gcc.processing.develop;

import java.lang.System;

/**
 * Send a printed message to the console. For development use only.
 *
 * @category   Development/Debugger
 * @package    edu.gcc.processing.develop
 * @access     public
 * @since      v0.1 Dev
 */

public class Message {
/**
 * Send the given message to the console
 *
 * @param      String      text        The message to send to the console
 * @access     public
 * @since      v0.1 Dev
 */
	public Message(String text) {
		System.out.print(text);
	}
}
