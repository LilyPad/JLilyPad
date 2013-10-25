package lilypad.server.proxy.packet;

import io.netty.util.AttributeKey;
import lilypad.packet.common.PacketCodecProvider;

public class StatefulPacketCodecProviderPair {

	public static AttributeKey<StatefulPacketCodecProviderPair> attributeKey = new AttributeKey<StatefulPacketCodecProviderPair>("statuefulPacketCodecProviderPair");
	
	public interface StateCodecProvider {
		public PacketCodecProvider getClientBound();
		public PacketCodecProvider getServerBound();	
	}
	
	private StateCodecProvider state;
	private ProxyPacketCodecProvider clientBound;
	private ProxyPacketCodecProvider serverBound;
	
	public StatefulPacketCodecProviderPair(StateCodecProvider state) {
		this.state = state;
		this.clientBound = new ProxyPacketCodecProvider(state.getClientBound());
		this.serverBound = new ProxyPacketCodecProvider(state.getServerBound());
	}
	
	public PacketCodecProvider getClientBound() {
		return this.clientBound;
	}
	
	public PacketCodecProvider getServerBound() {
		return this.serverBound;
	}
	
	public StateCodecProvider getState() {
		return this.state;
	}
	
	public void setState(StateCodecProvider state) {
		this.state = state;
		this.clientBound.setProvider(state.getClientBound());
		this.serverBound.setProvider(state.getServerBound());
	}
	
}
