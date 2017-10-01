package lilypad.packet.connect;

import lilypad.packet.common.PacketCodecRegistry;
import lilypad.packet.connect.impl.*;

public class ConnectPacketCodecRegistry extends PacketCodecRegistry {

	public static final ConnectPacketCodecRegistry instance = new ConnectPacketCodecRegistry();
	
	public ConnectPacketCodecRegistry() {
		this.register(new KeepalivePacketCodec());
		this.register(new RequestPacketCodec());
		this.register(new ResultPacketCodec());
		this.register(new MessagePacketCodec());
		this.register(new RedirectPacketCodec());
		this.register(new ServerPacketCodec());
		this.register(new PlayerPacketCodec());
	}
	
}
