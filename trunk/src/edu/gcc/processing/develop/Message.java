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
 * @param      boolean     text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(boolean text) {
		System.out.print(text + "\n");
	}	
	
/**
 * Send the given message to the console
 *
 * @param      char        text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(char text) {
		System.out.print(text + "\n");
	}
	
/**
 * Send the given message to the console
 *
 * @param      char[]      text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(char[] text) {
		System.out.print(text + "\n");
	}
	
/**
 * Send the given message to the console
 *
 * @param      double      text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(double text) {
		System.out.print(text + "\n");
	}
	
/**
 * Send the given message to the console
 *
 * @param      float       text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(float text) {
		System.out.print(text + "\n");
	}
	
/**
 * Send the given message to the console
 *
 * @param      int         text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(int text) {
		System.out.print(text + "\n");
	}
	
/**
 * Send the given message to the console
 *
 * @param      long        text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(long text) {
		System.out.print(text + "\n");
	}
	
/**
 * Send the given message to the console
 *
 * @param      Object      text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(Object text) {
		System.out.print(text + "\n");
	}

/**
 * Send the given message to the console
 *
 * @param      String      text        The message to send to the console
 * @access     public
 * @return     void
 * @since      v0.1 Dev
 */
	public Message(String text) {
		System.out.print(text + "\n");
	}
}