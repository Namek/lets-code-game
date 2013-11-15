package com.letscode.lcg.network;

import com.letscode.lcg.network.messages.GameStartMessage;
import com.letscode.lcg.network.messages.HandshakeMessage;
import com.letscode.lcg.network.messages.MessageBase;
import com.letscode.lcg.network.messages.MessageEnvelope;

public class MessageFactory {
	private static MessageEnvelope packageMessage(MessageBase message, String type) {
		MessageEnvelope env = new MessageEnvelope();
		env.message = message;
		env.type = type;
		return env;
	}
	
	public static MessageEnvelope createHandshakeMessage(String nickname) {
		HandshakeMessage msg = new HandshakeMessage();
		msg.nickname = nickname;
		return packageMessage(msg, "handshake");
	}
	
	public static MessageEnvelope createGameStartMessage() {
		return packageMessage(new GameStartMessage(), "gameStart");
	}
}
