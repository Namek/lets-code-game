package com.letscode.lcg.client;

import com.badlogic.gdx.Gdx;
import com.letscode.lcg.network.WebSocketClientInterface;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;

public class GwtWebSocketClient extends WebSocketClientInterface {
	private Websocket _socket;
	private boolean _isConnected = false;
	
	@Override
	public void connect(String hostname, int port) {
		_socket = new Websocket("ws://" + hostname + ":" + port + "/");
		_socket.addListener(webSocketListener);
		_socket.open();
	}

	@Override
	public boolean send(String msg) {
		_socket.send(msg);
		return isConnected();
	}

	@Override
	public boolean isConnected() {
		return _isConnected;//socket.getState() != 2;
	}
	
	@Override
	public void close() {
		_socket.close();
	}

	private WebsocketListener webSocketListener = new WebsocketListener() {
		@Override
	    public void onOpen() {
	    	_isConnected = true;
	    	onConnected();
	    }
		
	    @Override
	    public void onMessage(String msg) {
	    	Gdx.app.log("network-websocket", "received: " + msg);
	    	onMessageReceived(msg);
	    }
	   
		@Override
	    public void onClose() {
			if (_isConnected) {
				onDisconnected();
			}
			else {
				onError();
			}
			
			_isConnected = false;
	    }
	};
}
