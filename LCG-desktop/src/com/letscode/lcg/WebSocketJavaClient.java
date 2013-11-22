package com.letscode.lcg;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17; //This is the Standard WebSocket Implementation
import org.java_websocket.handshake.ServerHandshake;

import com.letscode.lcg.network.WebSocketClientInterface;

public class WebSocketJavaClient extends WebSocketClientInterface {
	private WebSocketClient wsc;
	private boolean _isConnected = false;

	public WebSocketJavaClient() {
		WebSocket.DEBUG = false;
	}

	public void connect(String ip, int port) {
		if (!ip.isEmpty()) {
			URI url = null;
			try {
				url = new URI("ws://" + ip + ":" + port + "/");
			} catch (URISyntaxException e) {
				e.printStackTrace();
				onError();
			}

			final WebSocketClientInterface that = this;
			final Draft standard = new Draft_17();

			wsc = new WebSocketClient(url, standard) {
				@Override
				public void onOpen(ServerHandshake handshake) {
					_isConnected = true;
				}

				@Override
				public void onMessage(String message) {
					onMessageReceived(message);
				}

				@Override
				public void onError(Exception ex) {
					that.onError();
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					if (_isConnected) {
						onDisconnected();
					}
					else {
						that.onError();
					}
					
					_isConnected = false;
				}
			};
			wsc.connect();
		}
	}

	public boolean send(String msg) {
		if (_isConnected) {
			wsc.send(msg);
		}

		return _isConnected;
	}

	public boolean isConnected() {
		return _isConnected;
	}

	public void close() {
		wsc.close();
		_isConnected = false;
	}
}
