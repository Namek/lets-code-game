package com.letscode.lcg.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Json;
import com.letscode.lcg.network.messages.MessageEnvelope;

public class NetworkComponent {
	private static class MessageListener extends Thread {
		private Json serializer = new Json();
		private BufferedReader stream;
		private BlockingQueue<MessageEnvelope> messages;
		
		public MessageListener(InputStream inStream) {
			stream = new BufferedReader(new InputStreamReader(inStream));
		}
		
		public void run() {
			while (true) {
				String message;
				try {
					message = stream.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
				System.out.println("Received: " + message);
				MessageEnvelope envelope = serializer.fromJson(MessageEnvelope.class, message);
				messages.add(envelope);
			}
		}
		
		public MessageEnvelope dequeueMessage() {
			return messages.poll();
		}
		
		public boolean hasMessages() {
			return !messages.isEmpty();
		}
	}
	
	private static class MessageSender extends Thread {
		private Json serializer = new Json();
		private OutputStream stream;
		private BlockingQueue<MessageEnvelope> messages;
		
		public MessageSender(OutputStream outStream) {
			stream = outStream;
		}
		
		public void run() {
			while (true) {
				MessageEnvelope envelope = messages.poll();
				try {
					String message = serializer.toJson(envelope);					
					stream.write(message.getBytes());
					stream.flush();
					System.out.println("Sent: " + message);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}				
			}
		}
		
		public void enqueueMessage(MessageEnvelope envelope) {
			messages.add(envelope);
		}
	}
	
	public void start(String host, int port) {
		Socket socket = Gdx.net.newClientSocket(Protocol.TCP, host, port, null);
		listener = new MessageListener(socket.getInputStream());
		listener.start();
		
		sender = new MessageSender(socket.getOutputStream());
		sender.start();
	}
	
	public void update() {
		while (listener.hasMessages()) {
			MessageEnvelope envelope = listener.dequeueMessage();
			Events.publishEvent(envelope.message);
		}
	}
	
	public void sendHandshakeMessage() {
		sender.enqueueMessage(null);
	}
	
	private MessageListener listener;
	private MessageSender sender;
}
