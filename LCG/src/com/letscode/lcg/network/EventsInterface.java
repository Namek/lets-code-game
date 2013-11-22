package com.letscode.lcg.network;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.utils.Array;
import com.letscode.lcg.network.messages.MessageBase;

public class EventsInterface {
	private static Queue<MessageBase> messages = new LinkedList<MessageBase>();
	private static Queue<GameMessageListener> listeners;
	private static Array<GameMessageListener> currentReceivers;
	
	static {
		messages = new LinkedList<MessageBase>();
		listeners = new LinkedList<GameMessageListener>();
		currentReceivers = new Array<GameMessageListener>(true, 20, GameMessageListener.class);
	}
	
	public static void publishEvent(MessageBase message) {
		messages.add(message);
	}
	
	public static void update() {
		MessageBase message = null;
		
		if ((message = messages.poll()) != null) {
			for (GameMessageListener listener : listeners) {
				currentReceivers.add(listener);
			}
			
			for (GameMessageListener listener : currentReceivers) {
				listener.onMessageReceived(message);
			}
			
			currentReceivers.clear();
		}
	}
	
	public static void subscribe(GameMessageListener listener) {
        listeners.add(listener);
	}
	
	public static void unsubscribe(GameMessageListener listener) {		
		listeners.remove(listener);
	}

	public static void unsubscribeAll() {
		listeners.clear();
	}
}