package com.letscode.lcg.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Cell;
import com.letscode.lcg.Context;
import com.letscode.lcg.actor.Board;
import com.letscode.lcg.enums.BuildMode;
import com.letscode.ui.BaseScreen;
import com.letscode.ui.UiApp;

public class PlayScreen extends BaseScreen {
	Context context;
	
	TextButton endTurnButton;
	TextButton buildTownhallButton, buildGoldmineButton, buildBarricadeButton;
	Label actionPointsValueLabel, goldValueLabel;
	
	Board board;
	

	public PlayScreen(Context context) {
		super(context.app);
		this.context = context;
		UiApp app = context.app;
	
		
		buildTownhallButton = new TextButton("Townhall", app.skin);
		buildGoldmineButton = new TextButton("Goldmine", app.skin);
		buildBarricadeButton = new TextButton("Barricade", app.skin);
		
		Label actionPointsLabel = new Label("Action Points:", app.skin);
		actionPointsValueLabel = new Label("0", app.skin);
		
		Label goldLabel = new Label("Gold: ", app.skin);
		goldValueLabel = new Label("0", app.skin);
		
		Table statsTable = new Table(app.skin);
		statsTable.add(actionPointsLabel);
		statsTable.add(actionPointsValueLabel);
		statsTable.row();
		statsTable.add(goldLabel);
		statsTable.add(goldValueLabel);
		
		board = new Board(context);
		Cell boardCell = mainTable.add(board).expand().fill();
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
	
	
	///////////////////////////////////////////////
	// Network Events
	//
	
	// TODO!!11111
	
	
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
	
	
	///////////////////////////////////////////////
	// Some other shitty events
	//
	
	@Override
	public void onBackPress() {}

}
