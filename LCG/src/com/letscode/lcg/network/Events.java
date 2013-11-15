package com.letscode.lcg.network;

import com.letscode.lcg.network.messages.MessageBase;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;

public class Events {
	// TODO: MBassador event input
	private static MBassador<MessageBase> bus = new MBassador<MessageBase>(BusConfiguration.Default());
}
