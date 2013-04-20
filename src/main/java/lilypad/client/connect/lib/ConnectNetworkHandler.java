package lilypad.client.connect.lib;

import java.net.InetSocketAddress;

import lilypad.client.connect.api.MessageEvent;
import lilypad.client.connect.api.RedirectEvent;
import lilypad.client.connect.api.ServerAddEvent;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.packet.common.Packet;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.MessagePacket;
import lilypad.packet.connect.impl.RedirectPacket;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.packet.connect.impl.ServerAddPacket;
import lilypad.packet.connect.impl.ServerPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

public class ConnectNetworkHandler extends ChannelInboundMessageHandlerAdapter<Packet> {
	
	private ConnectImpl connect;
	
	public ConnectNetworkHandler(ConnectImpl connect) {
		this.connect = connect;
	}
	
	public void messageReceived(ChannelHandlerContext context, Packet packet) throws Exception {
		switch(packet.getOpcode()) {
		case 0x00:
			context.write(packet);
			break;
		case 0x02:
			ResultPacket resultPacket = (ResultPacket) packet;
			StatusCode statusCode;
			switch(resultPacket.getStatusCode()) {
			case ConnectPacketConstants.statusSuccess:
				statusCode = StatusCode.SUCCESS;
				break;
			case ConnectPacketConstants.statusInvalidRole:
				statusCode = StatusCode.INVALID_ROLE;
				break;
			case ConnectPacketConstants.statusInvalidGeneric:
			default:
				statusCode = StatusCode.INVALID_GENERIC;
				break;
			}
			this.connect.dispatchResult(resultPacket.getId(), statusCode, resultPacket.getPayload());
			break;
		case 0x03:
			MessagePacket messagePacket = (MessagePacket) packet;
			this.connect.dispatchMessageEvent(new MessageEvent(messagePacket.getSender(), messagePacket.getChannel(), messagePacket.getPayload().array()));
			break;
		case 0x04:
			RedirectPacket redirectPacket = (RedirectPacket) packet;
			this.connect.dispatchRedirectEvent(new RedirectEvent(redirectPacket.getServer(), redirectPacket.getPlayer()));
			break;
		case 0x05:
			ServerPacket serverPacket = (ServerPacket) packet;
			if(serverPacket.isAdding()) {
				ServerAddPacket serverAddPacket = (ServerAddPacket) serverPacket;
				this.connect.dispatchServerAddEvent(new ServerAddEvent(serverAddPacket.getServer(), serverAddPacket.getSecurityKey(), new InetSocketAddress(serverAddPacket.getAddress(), serverAddPacket.getPort())));
			} else {
				this.connect.dispatchServerRemoveEvent(serverPacket.getServer());
			}
			break;
		default:
			context.close(); // invalid packet
			break;
		}
	}
	
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		Channel channel = context.channel();
		cause.printStackTrace();
		if(channel.isOpen()) {
			channel.close();
		}
	}
	
}
