package edu.gcc.processing.events;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * This interface is part of the events package, which contains declarations
 * for all of the events which this application will need to broadcast.
 *
 * @category   Events
 * @package    edu.gcc.processing.events
 * @access     public
 * @since      v0.1 Dev
 */

public interface IPacketRecievedListener extends EventListener {
	public void packetRecieved(PacketRecieved e, ArrayList data);
	public void userJoined(PacketRecieved e, ArrayList data);
	public void userLeft(PacketRecieved e, ArrayList data);
	public void roomClosed(PacketRecieved e, ArrayList data);
}