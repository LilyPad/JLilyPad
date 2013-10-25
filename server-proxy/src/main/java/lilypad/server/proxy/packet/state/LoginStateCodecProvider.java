package lilypad.server.proxy.packet.state;

import lilypad.packet.common.PacketCodecProvider;
import lilypad.server.proxy.packet.MinecraftPacketCodecRegistry;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair.StateCodecProvider;
import lilypad.server.proxy.packet.impl.LoginDisconnectPacketCodec;
import lilypad.server.proxy.packet.impl.LoginEncryptRequestPacketCodec;
import lilypad.server.proxy.packet.impl.LoginEncryptResponsePacketCodec;
import lilypad.server.proxy.packet.impl.LoginStartPacketCodec;
import lilypad.server.proxy.packet.impl.LoginSuccessPacketCodec;

public class LoginStateCodecProvider implements StateCodecProvider {

	public static final LoginStateCodecProvider instance = new LoginStateCodecProvider();
	
	private MinecraftPacketCodecRegistry clientBound = new MinecraftPacketCodecRegistry();
	private MinecraftPacketCodecRegistry serverBound = new MinecraftPacketCodecRegistry();
	
	private LoginStateCodecProvider() {
		this.clientBound.register(new LoginDisconnectPacketCodec());
		this.clientBound.register(new LoginEncryptRequestPacketCodec());
		this.clientBound.register(new LoginSuccessPacketCodec());
		this.serverBound.register(new LoginStartPacketCodec());
		this.serverBound.register(new LoginEncryptResponsePacketCodec());
	}
	
	public PacketCodecProvider getClientBound() {
		return this.clientBound;
	}

	public PacketCodecProvider getServerBound() {
		return this.serverBound;
	}

}
