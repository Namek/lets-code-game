package com.letscode.lcg.screens;

import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.letscode.lcg.Context;
import com.letscode.lcg.model.Map;
import com.letscode.lcg.network.EventsInterface;
import com.letscode.lcg.network.GameMessageListener;
import com.letscode.lcg.network.messages.MessageBase;
import com.letscode.lcg.network.messages.PlayerJoinedMessage;
import com.letscode.lcg.network.messages.PlayerLeftMessage;
import com.letscode.lcg.network.messages.PlayerListMessage;
import com.letscode.lcg.network.messages.StateMessage;
import com.letscode.lcg.ui.BaseScreen;

public class ConnectingScreen extends BaseScreen implements GameMessageListener {
	private Context context;
	private Table playerList = new Table();
	private Button startGameButton;
	
	public ConnectingScreen(){
		super(null);
	}
	
	public ConnectingScreen(final Context context, final String givenHostname, final int givenPort) {
		super(context.app);
		this.context = context;

		EventsInterface.subscribe(this);
		
		mainTable.setBackground(app.skin.getDrawable("window1"));
		mainTable.setColor(Color.valueOf("4792A5"));
		
		Skin skin = context.app.skin;
		mainTable.add(new Label("Lambda Cipher Genesis - connecting people...", skin))
			.top()
			.row();
		
		Table lobbyTable = new Table();
		lobbyTable.setBackground(app.skin.getDrawable("window1"));
		lobbyTable.setColor(Color.valueOf("6DE1FF"));
		mainTable.add(new Label("===== Lobby =====", skin))
			.top()
			.row();
		lobbyTable.add(playerList)
			.expand()
			.top()
			.row();
		
		startGameButton = new TextButton("Start game", skin);
		startGameButton.addCaptureListener(startGameListener);
		startGameButton.setColor(Color.valueOf("808080"));
		lobbyTable.add(startGameButton)
			.pad(10)
			.right()
			.bottom();
		
		mainTable.add(lobbyTable)
			.expand()
			.fill()
			.top();
		
		final String randomNickname = "Player " + (new Random().nextInt(25124123));
		
		if (givenHostname != null && givenPort > 0) {
			// Ask for nickname
			Gdx.input.getTextInput(new TextInputListener() {
				@Override
				public void input(String nickname) {
					connectToServer(givenHostname, givenPort, nickname);
				}
			
				@Override
				public void canceled() {
					Gdx.app.exit();
				}
			}, "Nickname:", randomNickname);
		}
		else {
			// Ask for hostname
			Gdx.input.getTextInput(new TextInputListener() {
				@Override
				public void input(final String hostname) {
					// Ask for port
					Gdx.input.getTextInput(new TextInputListener() {
						@Override
						public void input(final String port) {
							// Ask for nickname
							Gdx.input.getTextInput(new TextInputListener() {
								@Override
								public void input(String nickname) {
									connectToServer(hostname, Integer.parseInt(port), nickname);
								}
	
								@Override
								public void canceled() {
									Gdx.app.exit();
								}
							}, "Nickname:", randomNickname);
						}
						
						@Override
						public void canceled() {
							Gdx.app.exit();
						}
					}, "Port:", new Integer(givenPort).toString());
				}
	
				@Override
				public void canceled() {
					Gdx.app.exit();
				}
				
			}, "Hostname:", givenHostname);
		}
	}

	private void connectToServer(String hostname, int port, String nickname) {
		context.network.start(hostname, port);
		context.network.sendHandshakeMessage(nickname);
		context.colorsForPlayers.put(context.network.getClientNickname(), Color.BLUE);
	}


	@Override
	public void onMessageReceived(MessageBase message) {
		if (message instanceof PlayerJoinedMessage) {
			playerJoinedHandler((PlayerJoinedMessage)message);
		}
		else if (message instanceof PlayerLeftMessage) {
			playerLeftHandler((PlayerLeftMessage)message);
		}
		else if (message instanceof PlayerListMessage) {
			playerListHandler((PlayerListMessage)message);
		}
		else if (message instanceof StateMessage) {
			stateHandler((StateMessage)message);
		}
	}

	
	public void playerJoinedHandler(PlayerJoinedMessage message) {
		playerList.add(new Label(message.nickname, context.app.skin))
			.row();
	}
	
	public void playerLeftHandler(PlayerLeftMessage message) {
		SnapshotArray<Actor> playerLabels = playerList.getChildren();
		for (int i = 0; i < playerLabels.size; ++i) {
			Label lbl = (Label)playerLabels.get(i);
			String labelText = lbl.getText().toString();
			if (labelText.equals(message.nickname)) {
				playerList.removeActor(lbl);
				break;
			}
		}
	}
	
	public void playerListHandler(PlayerListMessage message) {
		for (String nickname : message.players) {
			playerList.add(new Label(nickname, context.app.skin))
				.row();	
		}
	}
	
	public void stateHandler(StateMessage message) {
		mapPlayerNamesToColors();
		message.convertJsonToModel();
		context.map = new Map(message.fields);
		context.app.switchScreens(new PlayScreen(context));
		
		EventsInterface.unsubscribe(this);
	}
	
	
	@Override
	public void act(float delta) {
		super.act(delta);
		context.network.update();
	}
	
	@Override
	public void onBackPress() {	
		Gdx.app.exit();
	}
	
	private void mapPlayerNamesToColors() {
		context.colorsForPlayers.clear();
		context.colorsForPlayers.put(null, Color.WHITE);
		Array<String> players = context.network.getPlayers();
		players.sort();
		for (int i = 0; i < players.size; ++i) {
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
