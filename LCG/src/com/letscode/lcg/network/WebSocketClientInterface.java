package com.letscode.lcg.network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class WebSocketClientInterface {
	private final Queue<String> _receivedMessages = new LinkedList<String>();
	private final List<Listener> _listeners = new ArrayList<Listener>();
	
	
	public static interface Listener {
		void onConnected();
		void onError();
		void onDisconnected();
		void onMessageReceived(String message);
	}
	
	public final void addListener(Listener listener) {
		_listeners.add(listener);
	}
	
	public final void removeListener(Listener listener) {
		_listeners.remove(listener);
	}
	
	
	public abstract void connect(String ip, int port);
	public abstract boolean send(String msg);
	public abstract boolean isConnected();
	public abstract void close();
	
	
	// Standad Events
	public final void onConnected() {
		for (int i = 0; i < _listeners.size(); ++i) {
			_listeners.get(i).onConnected();
		}
	}
	
	public final void onError() {
		for (int i = 0; i < _listeners.size(); ++i) {
			_listeners.get(i).onError();
		}
	}
	
	public final void onDisconnected() {
		for (int i = 0; i < _listeners.size(); ++i) {
			_listeners.get(i).onDisconnected();
		}
	}
	
	
	// Messsage handling
	public final void onMessageReceived(String message) {
		_receivedMessages.add(message);
		
		for (int i = 0; i < _listeners.size(); ++i) {
			_listeners.get(i).onMessageReceived(message);
		}
	}
	
	public boolean hasMessages() {
		return !_receivedMessages.isEmpty();
	}
	
	public String pollMessage() {
		return _receivedMessages.poll();
	}
}