package com.letscode.lcg.network;

import com.letscode.lcg.network.messages.MessageBase;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;

public class Events {
	private static MBassador<MessageBase> bus = new MBassador<MessageBase>(BusConfiguration.Default());
	
	public static void publishEvent(MessageBase message) {
		bus.publish(message);
	}
	
	public static void subscribe(Object listener) {
		bus.subscribe(listener);
	}
	
	public static void unsubscribe(Object listener) {
		bus.unsubscribe(listener);
	}
}
