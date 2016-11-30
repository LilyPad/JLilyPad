package lilypad.client.connect.lib;

import java.io.IOException;
import java.net.InetSocketAddress;

import lilypad.client.connect.api.event.*;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.packet.common.Packet;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;

@SuppressWarnings("deprecation")
public class ConnectNetworkHandler extends SimpleChannelInboundHandler<Packet> {

	private ConnectImpl connect;

	public ConnectNetworkHandler(ConnectImpl connect) {
		this.connect = connect;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext context, Packet packet) throws Exception {
		switch(packet.getOpcode()) {
		case 0x00:
			context.writeAndFlush(packet);
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
			MessageEvent messageEvent = new MessageEvent(messagePacket.getSender(), messagePacket.getChannel(), messagePacket.getPayload().array());
			messagePacket.getPayload().release();
			this.connect.dispatchMessageEvent(new lilypad.client.connect.api.MessageEvent(messageEvent));
			this.connect.dispatchEvent(messageEvent);
			break;
		case 0x04:
			RedirectPacket redirectPacket = (RedirectPacket) packet;
			RedirectEvent redirectEvent = new RedirectEvent(redirectPacket.getServer(), redirectPacket.getPlayer());
			this.connect.dispatchRedirectEvent(new lilypad.client.connect.api.RedirectEvent(redirectEvent));
			this.connect.dispatchEvent(redirectEvent);
			break;
		case 0x05:
			ServerPacket serverPacket = (ServerPacket) packet;
			if(serverPacket.isAdding()) {
				ServerAddPacket serverAddPacket = (ServerAddPacket) serverPacket;
				ServerAddEvent serverAddEvent = new ServerAddEvent(serverAddPacket.getServer(), serverAddPacket.getSecurityKey(), new InetSocketAddress(serverAddPacket.getAddress(), serverAddPacket.getPort()));
				this.connect.dispatchServerAddEvent(new lilypad.client.connect.api.ServerAddEvent(serverAddEvent));
				this.connect.dispatchEvent(serverAddEvent);
			} else {
				ServerRemoveEvent serverRemoveEvent = new ServerRemoveEvent(serverPacket.getServer());
				this.connect.dispatchServerRemoveEvent(serverPacket.getServer());
				this.connect.dispatchEvent(serverRemoveEvent);
			}
			break;
		case 0x06:
			PlayerPacket playerPacket = (PlayerPacket) packet;
			if(playerPacket.isJoining()) {
				PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(playerPacket.getPlayerName(), playerPacket.getPlayerUUID());
				this.connect.dispatchEvent(playerJoinEvent);
			} else {
				PlayerLeaveEvent playerLeaveEvent = new PlayerLeaveEvent(playerPacket.getPlayerName(), playerPacket.getPlayerUUID());
				this.connect.dispatchEvent(playerLeaveEvent);
			}
			break;
		default:
			context.close(); // invalid packet
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		Channel channel = context.channel();
		if(cause instanceof IOException) {
			if(!cause.getMessage().equals("Connection reset by peer")) {
				cause.printStackTrace();
			}
		} else if (!(cause instanceof ReadTimeoutException)) {
			cause.printStackTrace();
		}
		if(channel.isOpen()) {
			channel.close();
		}
	}

}
