package com.letscode.lcg.network;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.letscode.lcg.network.messages.MessageBase;

public class Events2 {
	private static Queue<MessageBase> messages = new LinkedList<MessageBase>();
	private static HashMap <Class<? extends MessageBase>, List<Handler>> listeners;
	
	static {
		messages = new LinkedList<MessageBase>();
		listeners = new HashMap<Class<? extends MessageBase>, List<Handler>>();
	}
	
	public static void publishEvent(MessageBase message) {
		messages.add(message);
	}
	
	public static void update() {
		MessageBase message = null;
		if ((message = messages.poll()) != null) {
			List<Handler> messageHandlers = listeners.get(message.getClass());
			if (messageHandlers != null) {
				for (Handler handler : messageHandlers) {
					handler.invoke(message);
				}
			}
		}
	}
	
	public static void subscribe(Object listener, Handler[] handlers) {
        for (Handler handler : handlers) {
        	
        	
        	if (handler == null) {
        		continue;
        	}
        	
        	Gdx.app.log("Events.subscribe", handler.toString());
        	
        	List<Handler> messageListeners = null; 
        	if (!listeners.containsKey(handler.eventType)) {
        		messageListeners = new LinkedList<Handler>();
        		listeners.put(handler.eventType, messageListeners);
        	}
        	else {
        		messageListeners = listeners.get(handler.eventType);
        	}
        	
        	messageListeners.add(handler);
        }
	}
	
	public static <T> void unsubscribe(T listener) {		
		for (List<Handler> handlers : listeners.values()) {
			List<Handler> handlersToRemove = new LinkedList<Handler>();
			
			for (Handler handler : handlers) {
				if (handler.methodOwner == listener) {
					handlersToRemove.add(handler);
				}
			}
			for (Handler handlerToRemove : handlersToRemove) {
				handlers.remove(handlerToRemove);
			}
		}
	}
	
	public static void unsubscribeStaticHandler(String methodName) {
		for (List<Handler> handlers : listeners.values()) {
			List<Handler> handlersToRemove = new LinkedList<Handler>();
			
			for (Handler handler : handlers) {
				if (handler.methodName.equals(methodName)) {
					handlersToRemove.add(handler);
				}
			}
			for (Handler handlerToRemove : handlersToRemove) {
				handlers.remove(handlerToRemove);
			}
		}
	}
    
	
    public static final class Handler {
    	final Method method;
    	final String methodName;
    	final Object methodOwner;
    	final Class<? extends MessageBase> eventType;
    	
    	
    	
    	
    	@Override
		public String toString() {
			return "{" + method.getName() + ", " + eventType + "}";
		}

		/**
    	 * Use for methods in context of some object as an owner.
    	 */
    	public Handler(Object methodOwner, String methodName, Class<? extends MessageBase> eventType) throws ReflectionException {
    		this.methodOwner = methodOwner;
    		this.methodName = methodName;
    		this.method = ClassReflection.getDeclaredMethod(methodOwner.getClass(), methodName, eventType);
    		this.eventType = eventType;
    		
    		method.setAccessible(true);
    	}
    	
    	/**
    	 * Use for static methods.
    	 */
    	public Handler(String methodName, Class<? extends MessageBase> eventType) throws ReflectionException {
    		this(null, methodName, eventType);
    	}
    	
    	public static Handler tryCreate(Object methodOwner, String methodName, Class<? extends MessageBase> eventType) {
    		try {
				return new Handler(methodOwner, methodName, eventType);
			} catch (ReflectionException e) {
				e.printStackTrace();
				return null;
			}
    	}
    	
    	public static Handler tryCreate(String methodName, Class<? extends MessageBase> eventType) {
    		return tryCreate(null, methodName, eventType);
    	}

		public void invoke(MessageBase message) {
			try {
				method.invoke(methodOwner, message);
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
		}
    }

}
