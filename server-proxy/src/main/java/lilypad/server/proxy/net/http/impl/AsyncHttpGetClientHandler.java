package lilypad.server.proxy.net.http.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class AsyncHttpGetClientHandler extends SimpleChannelInboundHandler<HttpObject> {

	private AsyncHttpGetClient httpGetClient;
	private StringBuilder response = new StringBuilder();

	public AsyncHttpGetClientHandler(AsyncHttpGetClient httpGetClient) {
		this.httpGetClient = httpGetClient;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext context, HttpObject httpObject) throws Exception {
		if (httpObject instanceof HttpResponse) {
			HttpResponse httpResponse = (HttpResponse) httpObject;
			int statusCode = httpResponse.getStatus().code();
			if(statusCode != 200) {
				throw new IllegalStateException("Unexpected status code: " + statusCode);
			}
		} else if(httpObject instanceof HttpContent) {
			HttpContent httpContent = (HttpContent) httpObject;
			this.response.append(httpContent.content().toString(CharsetUtil.UTF_8));
			if(httpObject instanceof LastHttpContent) {
				try {
					this.httpGetClient.dispatchHttpResponse(this.response.toString());
				} finally {
					context.close();
				}
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		Channel channel = context.channel();
		if(channel.isOpen()) {
			channel.close();
		}
		this.httpGetClient.dispatchExceptionCaught(cause);
	}

}
