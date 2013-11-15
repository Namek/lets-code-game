package com.letscode.lcg.screens;

import net.engio.mbassy.listener.Handler;

import com.letscode.lcg.Context;
import com.letscode.lcg.network.Events;
import com.letscode.lcg.network.NetworkComponent;
import com.letscode.lcg.network.messages.GameStartMessage;
import com.letscode.lcg.network.messages.PlayerJoinedMessage;
import com.letscode.lcg.network.messages.PlayerLeftMessage;
import com.letscode.lcg.network.messages.PlayerListMessage;
import com.letscode.ui.BaseScreen;

public class ConnectingScreen extends BaseScreen {

	private NetworkComponent network;
	
	public ConnectingScreen(Context context) {
		super(context.app);
		network = context.network;
		Events.subscribe(this);	}

	@Handler
	public void playerJoinedHandler(PlayerJoinedMessage message) {
		System.out.print(message);
	}
	
	@Handler
	public void playerLeftHandler(PlayerLeftMessage message) {
		System.out.print(message);
	}
	
	@Handler
	public void playerListHandler(PlayerListMessage message) {
		System.out.print(message);
		network.sendGameStartMessage();
	}
	
	@Handler
	public void gameStartedHandler(GameStartMessage message) {
		System.out.print(message);
	}
	
	@Override
	public void onBackPress() {	
		
	}
}
