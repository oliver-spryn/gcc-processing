package gcc.edu.processing.events;

import java.util.EventListener;

public interface IPacketRecievedListener extends EventListener {
	public void packetRecieved(PacketRecieved e);
}