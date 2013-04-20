package lilypad.server.query.udp.net;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lilypad.server.common.IPlayable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

public class QueryUdpHandler extends ChannelInboundMessageHandlerAdapter<DatagramPacket> {

	private IPlayable playable;
	private Map<SocketAddress, QueryUdpIdentification> addressToIdentification = new HashMap<SocketAddress, QueryUdpIdentification>();

	public QueryUdpHandler(IPlayable playable) {
		this.playable = playable;
	}

	public void messageReceived(ChannelHandlerContext context, DatagramPacket packet) throws Exception {
		ByteBuf buffer = packet.data();
		if(buffer.readableBytes() < 3) {
			return;
		}
		if(buffer.readUnsignedByte() != 0xFE || buffer.readUnsignedByte() != 0xFD) {
			return;
		}
		ByteBuf response;
		int opcode = buffer.readUnsignedByte();
		int requestId = buffer.readInt();
		switch(opcode) {
		case 0:
			if(!this.verifyIdentification(packet.remoteAddress(), requestId, buffer.readInt())) {
				return;
			}
			response = Unpooled.buffer();
			Set<String> players = this.playable.getPlayers();
			if(buffer.isReadable()) {
				response.writeByte(0x00);
				response.writeInt(requestId);
				response.writeBytes("splitnum".getBytes()); response.writeByte(0x00);
				response.writeByte(128);
				response.writeByte(0);
				response.writeBytes("hostname".getBytes()); response.writeByte(0x00);
				response.writeBytes(this.playable.getMotd().getBytes()); response.writeByte(0x00);
				response.writeBytes("gametype".getBytes()); response.writeByte(0x00);
				response.writeBytes("SMP".getBytes()); response.writeByte(0x00);
				response.writeBytes("game_id".getBytes()); response.writeByte(0x00);
				response.writeBytes("MINECRAFT".getBytes()); response.writeByte(0x00);
				response.writeBytes("version".getBytes()); response.writeByte(0x00);
				response.writeBytes(this.playable.getVersion().getBytes()); response.writeByte(0x00);
				response.writeBytes("plugins".getBytes()); response.writeByte(0x00);
				response.writeBytes("LilyPad".getBytes()); response.writeByte(0x00);
				response.writeBytes("map".getBytes()); response.writeByte(0x00);
				response.writeBytes("LilyPad".getBytes()); response.writeByte(0x00);
				response.writeBytes("numplayers".getBytes()); response.writeByte(0x00);
				response.writeBytes(Integer.toString(players.size()).getBytes()); response.writeByte(0x00);
				response.writeBytes("maxplayers".getBytes()); response.writeByte(0x00);
				response.writeBytes(Integer.toString(this.playable.getPlayerMaximum()).getBytes()); response.writeByte(0x00);
				response.writeBytes("hostport".getBytes()); response.writeByte(0x00);
				response.writeBytes(Integer.toString(this.playable.getBindAddress().getPort()).getBytes()); response.writeByte(0x00);
				response.writeBytes("hostip".getBytes()); response.writeByte(0x00);
				response.writeBytes(this.playable.getBindAddress().getHostName().getBytes()); response.writeByte(0x00);
				response.writeByte(0x00);
				response.writeByte(0x01);
				response.writeBytes("player_".getBytes()); response.writeByte(0x00);
				response.writeByte(0x00);
				for(String player : players) {
					response.writeBytes(player.getBytes()); response.writeByte(0x00);
				}
				response.writeByte(0x00);
			} else {
				response.writeByte(0x01);
				response.writeInt(requestId);
				response.writeBytes(this.playable.getMotd().getBytes()); response.writeByte(0x00);
				response.writeBytes("SMP".getBytes()); response.writeByte(0x00);
				response.writeBytes("LilyPad".getBytes()); response.writeByte(0x00);
				response.writeBytes(Integer.toString(players.size()).getBytes()); response.writeByte(0x00);
				response.writeBytes(Integer.toString(this.playable.getPlayerMaximum()).getBytes()); response.writeByte(0x00);
				response.writeShort(Short.reverseBytes((short) this.playable.getBindAddress().getPort()));
				response.writeBytes(this.playable.getBindAddress().getHostName().getBytes()); response.writeByte(0x00);
			}
			context.write(new DatagramPacket(response, packet.remoteAddress()));
			break;
		case 9:
			QueryUdpIdentification identification = new QueryUdpIdentification(requestId);
			this.addressToIdentification.put(packet.remoteAddress(), identification);
			response = Unpooled.buffer();
			response.writeByte(0x09);
			response.writeInt(identification.getRequestId());
			response.writeBytes(Integer.toString(identification.getChallenge()).getBytes()); response.writeByte(0x00);
			context.write(new DatagramPacket(response, packet.remoteAddress()));
			break;
		}
	}

	public boolean verifyIdentification(SocketAddress remoteAddress, int requestId, int challenge) {
		this.reapIdentifications();
		QueryUdpIdentification identification = this.addressToIdentification.get(remoteAddress);
		if(identification == null) {
			return false;
		}
		if(requestId != identification.getRequestId()) {
			return false;
		}
		if(challenge != identification.getChallenge()) {
			return false;
		}
		identification.updateTime();
		return true;
	}

	public void reapIdentifications() {
		Iterator<QueryUdpIdentification> identifications = this.addressToIdentification.values().iterator();
		while(identifications.hasNext()) {
			if(!identifications.next().hasExpired()) {
				continue;
			}
			identifications.remove();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

}
