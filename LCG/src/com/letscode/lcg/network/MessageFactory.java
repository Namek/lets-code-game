package com.letscode.lcg.network;

import com.letscode.lcg.enums.CommandType;
import com.letscode.lcg.network.messages.EndTurnMessage;
import com.letscode.lcg.network.messages.GameStartMessage;
import com.letscode.lcg.network.messages.HandshakeMessage;
import com.letscode.lcg.network.messages.MakeMoveMessage;
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
	
	public static MessageEnvelope createMakeMoveMessage(int col, int row, CommandType commandType) {
		MakeMoveMessage msg = new MakeMoveMessage();
		msg.col = col;
		msg.row = row;
		msg.what = commandType;
		return packageMessage(msg, "makeMove");
	}
	
	public static MessageEnvelope createEndTurnMessage() {
		return packageMessage(new EndTurnMessage(), "endTurn");
	}
}
