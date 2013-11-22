package com.letscode.lcg.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.letscode.lcg.enums.CommandType;
import com.letscode.lcg.network.messages.MessageBase;
import com.letscode.lcg.network.messages.MessageEnvelope;
import com.letscode.lcg.network.messages.PlayerJoinedMessage;
import com.letscode.lcg.network.messages.PlayerLeftMessage;
import com.letscode.lcg.network.messages.PlayerListMessage;

public class NetworkComponent implements GameMessageListener {
	private Json serializer = new Json();
//	private BufferedReader readerStream;
//	private OutputStream writerStream;
	
	private WebSocketClientInterface networkImpl;
	private Queue<MessageEnvelope> receivedMessages = new LinkedList<MessageEnvelope>();
	private Queue<MessageEnvelope> messagesToSend = new LinkedList<MessageEnvelope>();
	
	private String clientNickname;
	private Array<String> players;
	
	
	public NetworkComponent(WebSocketClientInterface networkImpl) {
		this.networkImpl = networkImpl;
		serializer.setOutputType(OutputType.json);
	}
	
	public void start(String host, int port) {
//		Socket socket = Gdx.net.newClientSocket(Protocol.TCP, host, port, null);

//		readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//		writerStream = socket.getOutputStream(); 
		
		EventsInterface.subscribe(this);
		networkImpl.connect(host, port);
		
//		Events.subscribe(this, new Handler[] {
//			Handler.tryCreate(this, "playerJoinedHandler", PlayerJoinedMessage.class),
//			Handler.tryCreate(this, "playerLeftHandler", PlayerLeftMessage.class),
//			Handler.tryCreate(this, "playerListHandler", PlayerListMessage.class)
//		});
		
	}
	
	public void update() {
		// Receive data from network.
		try {
//			if (readerStream != null && readerStream.ready()) {
//				String message = readerStream.readLine();
			
			if (networkImpl.hasMessages()) {
				String message = networkImpl.pollMessage().trim();
				if (!message.isEmpty()) {
					Gdx.app.log("network", "Received: " + message);
					MessageEnvelope envelope = serializer.fromJson(MessageEnvelope.class, message);
					receivedMessages.add(envelope);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			Gdx.app.error("network", e.toString(), e);
		}
		
		// Send queued messages.
		try {
			if (networkImpl.isConnected()) {
				MessageEnvelope envelope = messagesToSend.poll();
				
				if (envelope != null) {
					String message = serializer.toJson(envelope);
					message += "\n";
					networkImpl.send(message);
					Gdx.app.log("network", "Sent: " + message);
				}
			}
			
//			if (writerStream != null) {
//				MessageEnvelope envelope = messagesToSend.poll();
//				
//				if (envelope != null) {
//					String message = serializer.toJson(envelope);
//					message += "\n";
//					writerStream.write(message.getBytes());
//					writerStream.flush();
//					System.out.print("Sent: " + message);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.error("network", e.toString(), e);
		}
		
		// Send messages to other systems.
		while (!receivedMessages.isEmpty()) {
			MessageEnvelope envelope = receivedMessages.poll();

			Gdx.app.log("network", "publish event " + envelope.message.getClass());
			EventsInterface.publishEvent(envelope.message);
		}
	}
	
	private void queueMessageSend(MessageEnvelope envelope) {
		messagesToSend.add(envelope);
	}
	
	
	///////////////////////////////////
	// Player list handling
	///////////////////////////////////

	@Override
	public void onMessageReceived(MessageBase message) {
		if (message instanceof PlayerJoinedMessage) {
			players.add(((PlayerJoinedMessage)message).nickname);
		}
		else if (message instanceof PlayerLeftMessage) {
			players.removeValue(((PlayerLeftMessage)message).nickname, false);
		}
		else if (message instanceof PlayerListMessage) {
			players = ((PlayerListMessage)message).players;
		}
	}
	
	public String getClientNickname() {
		return clientNickname;
	}
	
	public Array<String> getPlayers() {
		return players;
	}
	
	
	///////////////////////////////////
	// Message sending
	///////////////////////////////////
	public void sendHandshakeMessage(String nickname) {
		clientNickname = nickname;
		queueMessageSend(MessageFactory.createHandshakeMessage(nickname));
	}
	
	public void sendGameStartMessage() {
		queueMessageSend(MessageFactory.createGameStartMessage());
	}
	
	public void sendEndTurnMessage() {
		queueMessageSend(MessageFactory.createEndTurnMessage());
	}
	
	public void sendMakeMoveMessage(int row, int col, CommandType commandType) {
		queueMessageSend(MessageFactory.createMakeMoveMessage(col, row, commandType));
	}
}
