package edu.gcc.processing.events;

import java.util.ArrayList;

import edu.gcc.processing.develop.Message;

/**
 * This class is simply a declaration class which will be utilized later to
 * respond to events
 *
 * @category   Events
 * @package    edu.gcc.processing.events
 * @access     public
 * @since      v0.1 Dev
 */

public class PacketRecievedHandler implements IPacketRecievedListener {
	public void packetRecieved(PacketRecieved e, ArrayList data) {}
	public void userJoined(PacketRecieved e, ArrayList data) {}
	public void userLeft(PacketRecieved e, ArrayList data) {}
	public void roomClosed(PacketRecieved e, ArrayList data) {}
}
