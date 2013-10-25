package lilypad.packet.connect;

import lilypad.packet.common.PacketCodecRegistry;
import lilypad.packet.connect.impl.KeepalivePacketCodec;
import lilypad.packet.connect.impl.MessagePacketCodec;
import lilypad.packet.connect.impl.RedirectPacketCodec;
import lilypad.packet.connect.impl.RequestPacketCodec;
import lilypad.packet.connect.impl.ResultPacketCodec;
import lilypad.packet.connect.impl.ServerPacketCodec;

public class ConnectPacketCodecRegistry extends PacketCodecRegistry {

	public static final ConnectPacketCodecRegistry instance = new ConnectPacketCodecRegistry();
	
	public ConnectPacketCodecRegistry() {
		this.submit(new KeepalivePacketCodec());
		this.submit(new RequestPacketCodec());
		this.submit(new ResultPacketCodec());
		this.submit(new MessagePacketCodec());
		this.submit(new RedirectPacketCodec());
		this.submit(new ServerPacketCodec());
	}
	
}
