package com.letscode.lcg.screens;

import java.util.ArrayList;
import java.util.Collections;

import net.engio.mbassy.listener.Handler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
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
	private Table playerList = new Table();
	private Button startGameButton;
	
	public ConnectingScreen(Context context) {
		super(context.app);
		this.context = context;
		Events.subscribe(this);
		
		mainTable.setBackground(app.skin.getDrawable("window1"));
		mainTable.setColor(Color.valueOf("C5D8C5"));
		
		Skin skin = context.app.skin;
		mainTable.add(new Label("Lambda Cipher Genesis - connecting people...", skin))
			.top()
			.row();
		
		Table lobbyTable = new Table();
		lobbyTable.setBackground(app.skin.getDrawable("window1"));
		lobbyTable.setColor(Color.valueOf("C5D8C5"));
		lobbyTable.add(new Label("===== Lobby =====", skin))
			.top()
			.row();
		lobbyTable.add(playerList)
			.expand()
			.top()
			.row();
		
		startGameButton = new TextButton("Start game", skin);
		startGameButton.addCaptureListener(startGameListener);
		lobbyTable.add(startGameButton)
			.pad(10)
			.right()
			.bottom();
		
		mainTable.add(lobbyTable)
			.expand()
			.fill()
			.top();
	}

	@Handler
	public void playerJoinedHandler(PlayerJoinedMessage message) {
		playerList.add(new Label(message.nickname, context.app.skin))
			.row();
	}
	
	@Handler
	public void playerLeftHandler(PlayerLeftMessage message) {
		SnapshotArray<Actor> playerLabels = playerList.getChildren();
		for (int i = 0; i < playerLabels.size; ++i) {
			Label lbl = (Label)playerLabels.get(i); 
			if (lbl.getText().equals(message.nickname)) {
				playerList.removeActor(lbl);
				break;
			}
		}
	}
	
	@Handler
	public void playerListHandler(PlayerListMessage message) {
		for (String nickname : message.players) {
			playerList.add(new Label(nickname, context.app.skin))
				.row();	
		}
		
		if (message.players.size() != 1)
			startGameButton.setVisible(false);
	}
	
	@Handler
	public void stateHandler(StateMessage message) {
		mapPlayerNamesToColors();
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
	
	private void mapPlayerNamesToColors() {
		context.colorsForPlayers.clear();
		context.colorsForPlayers.put(null, Color.WHITE);
		ArrayList<String> players = context.network.getPlayers();
		Collections.sort(players);
		for (int i = 0; i < players.size(); ++i) {
			context.colorsForPlayers.put(players.get(i), context.colors[i + 1]);
		}
	}
	
	private ClickListener startGameListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			context.network.sendGameStartMessage();
			startGameButton.setVisible(false);
		};
	};
}
