package edu.gcc.processing.gui;

import processing.core.*;

/**
 * Send an unobtrusive alert to the user in the form of a small tab
 * containing a short message in the top, center of their window.
 *
 * @category   GUI
 * @package    edu.gcc.processing.gui
 * @access     public
 * @since      v0.1 Dev
 */

public class TabAlerts {
/**
 * A private reference to the PApplet class which is passed to this 
 * class upon instantiation.
 *
 * @access     private
 * @var        PApplet
 */
	private PApplet parent;
	
/**
 * A hexadecimal color to be used for the border of the alert tab. Note
 * that the color must start with "0xFF", before the color value.
 *
 * @access     public
 * @var        int
 */
	public int borderColor = 0xFF000000;
	
/**
 * A hexadecimal color to be used for the fill of the alert tab. Note that
 * the color must start with "0xFF", before the color value.
 *
 * @access     public
 * @var        int
 */
	public int fillColor = 0xFFFFFFFF;
	
/**
 * A hexadecimal color to be used for the color of the text. Note that the 
 * color must start with "0xFF", before the color value.
 *
 * @access     public
 * @var        int
 */
	public int textColor = 0xFF000000;
	
/**
 * The message to display inside of the tab.
 *
 * @access     public
 * @var        String
 */
	public String message = "Place alert message here";
	
/**
 * The constructor method, which simply globalizes access to the
 * PApplet class for later use.
 *
 * @access     public
 * @param      PApplet     reference   A reference to the object which extends the PApplet class
 * @return     void
 * @since      v0.1 Dev
 */
	
	public TabAlerts(PApplet reference) {
		this.parent = reference;
	}
	
	public void build() {
	//Grab dimensional information about the stage and given text
		int stageWidth  = this.parent.width;
		long textWidth = Math.round(this.parent.textWidth(this.message));
		int halfStage = stageWidth / 2;
		long halfText = textWidth / 2;
		
	//Apply the default fills and colors to the object being drawn
		this.parent.fill(this.fillColor);
		this.parent.stroke(this.borderColor);
		
	//Create the rounded edges on the bottom
		this.parent.arc(halfStage - halfText, 15, 12, 10, this.parent.PI/2, this.parent.PI);
		this.parent.arc(halfStage + halfText, 15, 12, 10, 0, this.parent.PI/2);
		
	//Remove the stroke, since we will only want the stroke visible on the outer rim of the entire tab
		this.parent.noStroke();
		
	//Draw the rectangle connecting the rounded edges
		this.parent.rect(halfStage - halfText, 15, textWidth + 1, 5);
		
	//Draw the main body rectangle
		this.parent.rect(halfStage - halfText - 6, 0, textWidth + 2 * 6, 15);
		
	//Manually draw the border around the straight edges (processing took care of the rounded edges)
		this.parent.stroke(this.borderColor);
		this.parent.line(halfStage - halfText - 6, 0, halfStage - halfText - 6, 15);
		this.parent.line(halfStage + halfText + 6, 0, halfStage + halfText + 6, 15);
		this.parent.line(halfStage - halfText, 15 + 5, halfStage + halfText, 15 + 5);
		
	//Create the text, and place it on top of the tab
		this.parent.fill(this.textColor);
		this.parent.textAlign(this.parent.CENTER);
		this.parent.text(this.message, 2, 1, stageWidth, 15);
	}
}
