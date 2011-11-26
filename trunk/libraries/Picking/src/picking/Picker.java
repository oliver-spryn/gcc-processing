/*
  Provides an easy way to pick an object in a 3D scene.
  
  (c) copyright
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package picking;

import processing.core.*;

/**
 * A Picker object helps you to process picking.
 * 
 * @example Simple
 * @author Nicolas Clavaud <antiplastik@gmail.com>
 *
 */
public class Picker {
	
	/**
	 * Processing applet
	 */
	protected PApplet parent;
	
	/**
	 * Main picking buffer
	 * Direct access to the buffer is allowed so you can draw shapes that
	 * you wouldn't like to draw on the main screen (like bounding boxes).
	 */
	public Buffer buffer;
	
	public Picker(PApplet parent) {
		this.parent = parent;
		buffer = (Buffer) parent.createGraphics(parent.width, parent.height, "picking.Buffer");
		buffer.callCheckSettings();
		if (parent.recorder == null) {
			parent.recorder = buffer;
		}
		buffer.background(0);
		parent.registerPre(this);
		parent.registerDraw(this);
	}
	
	public void pre() {
		buffer.beginDraw();
		if (parent.recorder == null) {
			parent.recorder = buffer;
		}
	}
	
	public void draw() {
		buffer.endDraw();
	}
	
	/**
	 * Begins recording object(s)
	 * 
	 * @param i Object ID
	 */
	public void start(int i) {
		if (i < 0 || i > 16777214) {
			PApplet.println("[Picking error] start(): ID out of range");
			return;
		}
		if (parent.recorder == null) {
			parent.recorder = buffer;
		}
		buffer.setCurrentId(i);
	}
	
	/**
	 * Stops/pauses recording object(s)
	 */
	public void stop() {
		parent.recorder = null;
	}
	
	/**
	 * Resumes recording object(s)
	 */
	public void resume() {
		if (parent.recorder == null) {
			parent.recorder = buffer;
		}
	}
	
	/**
	 * Reads the ID of the object at point (x, y)
	 * -1 means there is no object at this point
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return Object ID
	 */
	public int get(int x, int y) {
		return buffer.getId(x, y);
	}
	
	/**
	 * Get the buffer
	 * @return Buffer
	 */
	public PGraphics getBuffer() {
		return buffer;
	}
	
}
