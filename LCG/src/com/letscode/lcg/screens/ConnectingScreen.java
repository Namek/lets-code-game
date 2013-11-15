package com.letscode.lcg.screens;

import net.engio.mbassy.listener.Handler;

import com.letscode.lcg.Context;
import com.letscode.lcg.model.Map;
import com.letscode.lcg.network.Events;
import com.letscode.lcg.network.messages.PlayerJoinedMessage;
import com.letscode.lcg.network.messages.PlayerLeftMessage;
import com.letscode.lcg.network.messages.PlayerListMessage;
import com.letscode.lcg.network.messages.StateMessage;
import com.letscode.ui.BaseScreen;

public class ConnectingScreen extends BaseScreen {

	private Context context;
	
	public ConnectingScreen(Context context) {
		super(context.app);
		this.context = context;
		Events.subscribe(this);	
	}

	@Handler
	public void playerJoinedHandler(PlayerJoinedMessage message) {
		// TODO
	}
	
	@Handler
	public void playerLeftHandler(PlayerLeftMessage message) {
		// TODO
	}
	
	@Handler
	public void playerListHandler(PlayerListMessage message) {
		context.network.sendGameStartMessage();
	}
	
	@Handler
	public void stateHandler(StateMessage message) {
		message.convertJsonToModel();
		context.map = new Map(message.fields);
		context.app.switchScreens(new PlayScreen(context));
		Events.unsubscribe(this);
	}
	
	@Override
	public void act(float delta) {
		context.network.update();
	}
	
	@Override
	public void onBackPress() {	
		
	}
}
