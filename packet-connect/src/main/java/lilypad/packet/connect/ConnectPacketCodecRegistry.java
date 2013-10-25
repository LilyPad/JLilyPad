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
		this.register(new KeepalivePacketCodec());
		this.register(new RequestPacketCodec());
		this.register(new ResultPacketCodec());
		this.register(new MessagePacketCodec());
		this.register(new RedirectPacketCodec());
		this.register(new ServerPacketCodec());
	}
	
}
