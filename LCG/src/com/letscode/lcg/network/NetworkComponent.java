package com.letscode.lcg.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.letscode.lcg.network.messages.MessageEnvelope;

public class NetworkComponent {
	private static class MessageListener extends Thread {
		private Json serializer = new Json();
		private BufferedReader stream;
		private BlockingQueue<MessageEnvelope> messages = new ArrayBlockingQueue<MessageEnvelope>(40);
		
		public MessageListener(InputStream inStream) {
			stream = new BufferedReader(new InputStreamReader(inStream));
			serializer.setOutputType(OutputType.json);
		}
		
		public void run() {
			try {
				while (true) {
					if (stream.ready()) {
						String message = stream.readLine();
						if (!message.isEmpty()) {
							System.out.println("Received: " + message);
							MessageEnvelope envelope = serializer.fromJson(MessageEnvelope.class, message);
							messages.add(envelope);
						}
					}
					else {
						sleep(50);
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
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
		private BlockingQueue<MessageEnvelope> messages = new ArrayBlockingQueue<MessageEnvelope>(40);
		
		public MessageSender(OutputStream outStream) {
			stream = outStream;
			serializer.setOutputType(OutputType.json);
		}
		
		public void run() {
			while (true) {
				MessageEnvelope envelope;					
				try {
					envelope = messages.take();
					String message = serializer.toJson(envelope);
					message += "\n";
					stream.write(message.getBytes());
					stream.flush();
					System.out.print("Sent: " + message);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
				catch (InterruptedException e) {
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
	
	public void sendHandshakeMessage(String nickname) {
		sender.enqueueMessage(MessageFactory.createHandshakeMessage(nickname));
	}
	
	private MessageListener listener;
	private MessageSender sender;
}