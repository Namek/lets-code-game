package com.letscode.lcg.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.letscode.lcg.Assets;
import com.letscode.lcg.Context;
import com.letscode.lcg.actor.Board;
import com.letscode.lcg.actor.FieldActor;
import com.letscode.lcg.actor.ParticleSystem;
import com.letscode.lcg.enums.ActionCost;
import com.letscode.lcg.enums.BuildMode;
import com.letscode.lcg.enums.BuildingGoldCost;
import com.letscode.lcg.enums.CommandType;
import com.letscode.lcg.model.Field;
import com.letscode.lcg.network.EventsInterface;
import com.letscode.lcg.network.GameMessageListener;
import com.letscode.lcg.network.messages.GameEndMessage;
import com.letscode.lcg.network.messages.MessageBase;
import com.letscode.lcg.network.messages.MoveDoneMessage;
import com.letscode.lcg.network.messages.MoveMessage;
import com.letscode.lcg.network.messages.NextPlayerMessage;
import com.letscode.lcg.network.messages.YourTurnMessage;
import com.letscode.lcg.ui.BaseScreen;
import com.letscode.lcg.ui.UiApp;

public class PlayScreen extends BaseScreen implements GameMessageListener {
	Context context;	
	
	Button endTurnButton;
	Button buildTownhallButton, buildGoldmineButton, buildBarricadeButton;
	Label actionPointsValueLabel, goldValueLabel, turnPlayerLabel;
	
	Board board;
	
	public PlayScreen(Context context) {
		super(context.app);
		this.context = context;
		UiApp app = context.app;
		
		EventsInterface.subscribe(this);
		
		mainTable.setBackground(new TextureRegionDrawable(Assets.backgroundTexture));

		Label actionPointsLabel = new Label("Action Points:", app.skin);
		actionPointsValueLabel = new Label("0", app.skin);
		
		Label goldLabel = new Label("Gold: ", app.skin);
		goldValueLabel = new Label("0", app.skin);
		
		Table statsTable = new Table(app.skin);
		statsTable.setBackground(app.skin.getDrawable("window1"));
		statsTable.setColor(Color.valueOf("A0A0A0"));
		statsTable.add(actionPointsLabel);
		statsTable.add(actionPointsValueLabel);
		statsTable.row();
		statsTable.add(goldLabel);
		statsTable.add(goldValueLabel);		
		mainTable.add(statsTable);
		
		// Setup things about player's turn
        Table turnTable = new Table(app.skin);
        turnTable.setBackground(app.skin.getDrawable("window1"));
        turnTable.setColor(Color.valueOf("A0A0A0"));
        turnPlayerLabel = new Label("hgw", app.skin);
        turnPlayerLabel.setAlignment(Align.center);
        setTurnPlayerLabel(context.network.getClientNickname());
        endTurnButton = new TextButton("End turn", app.skin);
        endTurnButton.addListener(endTurnButtonListener);
        endTurnButton.setColor(Color.valueOf("808080"));
        endTurnButton.setVisible(false);
        turnTable.add(turnPlayerLabel).expandX().fill();
        turnTable.row();
        turnTable.add(endTurnButton).spaceTop(20);
        mainTable.add(turnTable).expandX().fill();
		
		// Setup build buttons
        Table buttonsTable = new Table(app.skin);
        buttonsTable.setBackground(app.skin.getDrawable("window1"));
        
        buildTownhallButton = new TextButton("Townhall", app.skin);
        buildTownhallButton .setColor(Color.valueOf("808080"));
        buildTownhallButton.addListener(buildTownhallButtonListener);
        Table buildTownhallTable = new Table(app.skin);
        Image buildTownhallImage = new Image(new TextureRegionDrawable(Assets.townhallTexture));
        buildTownhallTable.add(buildTownhallImage).maxWidth(64).maxHeight(64).center();
        buildTownhallTable.row();
        buildTownhallTable.add(buildTownhallButton);
        buildGoldmineButton = new TextButton("Goldmine", app.skin);
        buildGoldmineButton.setColor(Color.valueOf("808080"));
        buildGoldmineButton.addListener(buildGoldmineButtonListener);
        Table buildGoldmineTable = new Table(app.skin);
        Image buildGoldmineImage = new Image(new TextureRegionDrawable(Assets.goldmineTexture));
        buildGoldmineTable.add(buildGoldmineImage).maxWidth(64).maxHeight(64).center();
        buildGoldmineTable.row();
        buildGoldmineTable.add(buildGoldmineButton);
        buildBarricadeButton = new TextButton("Barricade", app.skin);
        buildBarricadeButton.setColor(Color.valueOf("808080"));
        buildBarricadeButton.addListener(buildBarricadeButtonListener);
        Table buildBarricadeTable = new Table(app.skin);
        Image buildBarricadeImage = new Image(new TextureRegionDrawable(Assets.barricadeTexture));
        buildBarricadeTable.add(buildBarricadeImage).maxWidth(64).maxHeight(64).center();
        buildBarricadeTable.row();
        buildBarricadeTable.add(buildBarricadeButton);
        buttonsTable.add(buildTownhallTable);
        buttonsTable.add(buildGoldmineTable);
        buttonsTable.add(buildBarricadeTable);
        buttonsTable.setColor(Color.valueOf("A0A0A0"));
        mainTable.add(buttonsTable);
        
        // Setup game board
		board = new Board(context);
		
        mainTable.row();
        Table boardTable = new Table(app.skin);
        boardTable.setBackground(app.skin.getDrawable("window1"));
        boardTable.setColor(Color.valueOf("335B3388"));
        boardTable.add(board).expand().fill();
        mainTable.add(boardTable).expand().fill().colspan(3);
        
        mainTable.layout();
        
        board.init();
        board.addListener(boardListener);
        
        context.app.setClearColor(Color.valueOf("9EAE9E"));
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		getStage().getCamera().update();
		context.shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
	}
	
	private void setBuildMode(BuildMode buildMode) {
		context.currentBuildMode = buildMode;
		
		buildTownhallButton.setDisabled(true);
		buildTownhallButton.setChecked(buildMode == BuildMode.Townhall);
		buildTownhallButton.setDisabled(false);
		buildGoldmineButton.setDisabled(true);
		buildGoldmineButton.setChecked(buildMode == BuildMode.Goldmine);
		buildGoldmineButton.setDisabled(false);
		buildBarricadeButton.setDisabled(true);
		buildBarricadeButton.setChecked(buildMode == BuildMode.Barricade);
		buildBarricadeButton.setDisabled(false);
	}
	
	private void setTurnPlayerLabel(String playerName) {
		boolean thisPlayerMoves = playerName.equals(context.getPlayerNickname());
		String labelText = thisPlayerMoves ? "Now you move!" : (playerName != null ? "Now: " + playerName : "");
		turnPlayerLabel.setText(labelText);
		turnPlayerLabel.setColor(context.colorsForPlayers.get(playerName));
	}
	
	/**
	 * Checks if move is possible and then it's being made.
	 * 
	 * @param fieldActor
	 */
	private void tryToMakeMove(FieldActor fieldActor) {
		CommandType commandType = null;
		boolean shouldSendCommand = false;
		int rowIndex = fieldActor.getRowIndex();
		int colIndex = fieldActor.getColIndex();
		Field field = context.map.getField(rowIndex, colIndex);
		String thisPlayerName = context.getPlayerNickname();
		boolean isOwnedByPlayer = context.map.isFieldOwnedBy(thisPlayerName, rowIndex, colIndex);
		boolean canPlayerBuildOnField = isOwnedByPlayer && field.building == null;
		int currentActionPoints = Integer.parseInt(actionPointsValueLabel.getText().toString());
		int currentGold = Integer.parseInt(goldValueLabel.getText().toString());
		
		if (context.currentBuildMode == BuildMode.None) {
			if (!isOwnedByPlayer) {
				commandType = CommandType.conquer;
				shouldSendCommand = context.map.canPlayerAttackField(thisPlayerName, rowIndex, colIndex)
					&& currentActionPoints >= ActionCost.CONQUER_EMPTY_FIELD;
				
				if (shouldSendCommand) {
					if (field.building != null) {
						field.building = null;
						
						ParticleSystem explodeBuilding = new ParticleSystem("explode.ps");
						explodeBuilding.setPosition(
								fieldActor.getX() + fieldActor.getWidth() / 2,
								fieldActor.getY() + fieldActor.getHeight() / 3);
						explodeBuilding.toFront();
						board.addActor(explodeBuilding);
					}
					else {
						field.owner = thisPlayerName;	
					}
										
					currentActionPoints -= ActionCost.CONQUER_EMPTY_FIELD;
				}
			}
			else if (field.type.equals(Field.TYPE_GOLD) 
					&& currentActionPoints >= ActionCost.MINE_GOLD
					&& field.building != null
					&& field.building.equals(Field.BUILDING_GOLDMINE)) {
				commandType = CommandType.mine_gold;
				shouldSendCommand = true;
				currentActionPoints -= ActionCost.MINE_GOLD;
			}
			
		}
		else if (context.currentBuildMode == BuildMode.Townhall) {
			commandType = CommandType.build_townhall;
			shouldSendCommand = canPlayerBuildOnField
					&& currentActionPoints >= ActionCost.BUILD_TOWNHALL
					&& currentGold >= BuildingGoldCost.TOWNHALL;
			
			if (shouldSendCommand) {
				field.building = Field.BUILDING_TOWNHALL;
				currentActionPoints -= ActionCost.BUILD_TOWNHALL;
				currentGold -= BuildingGoldCost.TOWNHALL;
				setBuildMode(BuildMode.None);
			}
		}
		else if (context.currentBuildMode == BuildMode.Goldmine) {
			commandType = CommandType.build_mine;
			shouldSendCommand =
					field.type.equals(Field.TYPE_GOLD)
					&& canPlayerBuildOnField
					&& currentActionPoints >= ActionCost.BUILD_MINE
					&& currentGold >= BuildingGoldCost.GOLDMINE;
			
			if (shouldSendCommand) {
				field.building = Field.BUILDING_GOLDMINE;
				currentActionPoints -= ActionCost.BUILD_MINE;
				currentGold -= BuildingGoldCost.GOLDMINE;
				setBuildMode(BuildMode.None);
			}
		}
		else if (context.currentBuildMode == BuildMode.Barricade) {
			commandType = CommandType.build_barricade;
			
			shouldSendCommand = canPlayerBuildOnField
					&& currentActionPoints >= ActionCost.BUILD_BARRICADE
					&& currentGold >= BuildingGoldCost.BARRICADE;

			if (shouldSendCommand) {
				field.building = Field.BUILDING_BARRICADE;
				currentActionPoints -= ActionCost.BUILD_BARRICADE;
				currentGold -= BuildingGoldCost.BARRICADE;
				setBuildMode(BuildMode.None);
			}
		}
		
		if (shouldSendCommand) {
			actionPointsValueLabel.setText(new Integer(currentActionPoints).toString());
			goldValueLabel.setText(new Integer(currentGold).toString());
			context.network.sendMakeMoveMessage(rowIndex, colIndex, commandType);
		}
	}
	
	private void updateGoldAndActionPoints(int actionPoints, int gold) {
		actionPointsValueLabel.setText(Integer.toString(actionPoints));
		goldValueLabel.setText(Integer.toString(gold));
	}
	
	///////////////////////////////////////////////
	// Network Events
	//

	@Override
	public void onMessageReceived(MessageBase message) {
		if (message instanceof MoveMessage) {
			moveHandler((MoveMessage)message);
		}
		else if (message instanceof NextPlayerMessage) {
			nextPlayerHandler((NextPlayerMessage)message);
		}
		else if (message instanceof YourTurnMessage) {
			yourTurnHandler((YourTurnMessage)message);
		}
		else if (message instanceof GameEndMessage) {
			gameEndHandler((GameEndMessage) message);
		}
		else if (message instanceof MoveDoneMessage) {
			moveDoneHandler((MoveDoneMessage)message);
		}
	}
	
	public void moveHandler(MoveMessage message) {
		Field fld = context.map.getField(message.row, message.col);
		if (message.what == CommandType.conquer) {	
			// TODO: enable particle effect
			if (fld.building != null) {
				fld.building = null;
			}
			else {
				fld.owner = message.who;
			}
		}
		else if (message.what == CommandType.build_mine) {
			fld.building = Field.BUILDING_GOLDMINE; 
		}
		else if (message.what == CommandType.build_townhall) {
			fld.building = Field.BUILDING_TOWNHALL;
		}
		else if (message.what == CommandType.build_barricade) {
			fld.building = Field.BUILDING_BARRICADE;
		}
	}
	
	public void nextPlayerHandler(NextPlayerMessage message) {
		setTurnPlayerLabel(message.nickname);
		endTurnButton.setVisible(false);
	}
	
	public void yourTurnHandler(YourTurnMessage message) {
        endTurnButton.setVisible(true);
        updateGoldAndActionPoints(message.actionPoints, message.gold);
        setTurnPlayerLabel(context.getPlayerNickname());
	}
	
	public void gameEndHandler(GameEndMessage message) {
		app.switchScreens(new GameResultScreen(context, message.winner));
	}

	public void moveDoneHandler(MoveDoneMessage message) {
		updateGoldAndActionPoints(message.actionPoints, message.gold);
		if (message.actionPoints == 0) {
			endTurnButton.setVisible(false);
		}
	}
	
	
	///////////////////////////////////////////////
	// GUI Events
	//
	ClickListener buildTownhallButtonListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			setBuildMode(buildTownhallButton.isChecked() ? BuildMode.Townhall : BuildMode.None);
			super.clicked(event, x, y);
		}
	};
	
	ClickListener buildGoldmineButtonListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			setBuildMode(buildGoldmineButton.isChecked() ? BuildMode.Goldmine : BuildMode.None);
			super.clicked(event, x, y);
		}
	};
	
	ClickListener buildBarricadeButtonListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			setBuildMode(buildBarricadeButton.isChecked() ? BuildMode.Barricade : BuildMode.None);
			super.clicked(event, x, y);
		}
	};
	
	EventListener boardListener = new ClickListener() {
		@Override 
		public void clicked(InputEvent event, float x, float y)  {
			Actor actor = event.getTarget() instanceof FieldActor ? (FieldActor) event.getTarget() : null;
			
			if (actor != null) {
				if (actor instanceof FieldActor) {
					FieldActor fieldActor = (FieldActor)actor;
					fieldActor.animateTouched();
					
					if (fieldActor.getField() != null) {
						tryToMakeMove(fieldActor);
					}
				}
			}
		};
	};
	
	ClickListener endTurnButtonListener = new ClickListener() {
		@Override 
		public void clicked(InputEvent event, float x, float y)  {
			context.network.sendEndTurnMessage();
			endTurnButton.setVisible(false);
			endTurnButton.setChecked(false);
		}
	};
	
	@Override
	public void act(float delta) {
		super.act(delta);
		context.network.update();
	}
	
	@Override
	public void onBackPress() {}

}
