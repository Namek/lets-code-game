package com.letscode.lcg.network;

import com.letscode.lcg.network.messages.MessageBase;

public interface GameMessageListener {
	void onMessageReceived(MessageBase message);
}
