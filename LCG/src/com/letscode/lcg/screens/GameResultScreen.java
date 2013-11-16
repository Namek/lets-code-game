package com.letscode.lcg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.letscode.lcg.Context;
import com.letscode.lcg.actor.ParticleSystem;
import com.letscode.ui.BaseScreen;

public class GameResultScreen extends BaseScreen {

	private ParticleSystem leftSystem;
	private ParticleSystem rightSystem;
	
	public GameResultScreen(Context context, String winner) {
		super(context.app);
	    
	    String text = context.getPlayerNickname().equals(winner) ? "You have won!" : "Player " + winner + " has won."; 
	    Label label = new Label(text, app.skin); 
	    label.setAlignment(Align.center); 
	    mainTable.add(label).expand().fill(); 
	    
	    leftSystem = new ParticleSystem("winner.ps");
	    rightSystem = new ParticleSystem("winner.ps");
	    
	    leftSystem.setPosition(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2);
	    rightSystem.setPosition(Gdx.graphics.getWidth() / 3 * 2, Gdx.graphics.getHeight() / 2);
	    addActor(leftSystem);
	    addActor(rightSystem);
	    
	    mainTable.layout(); 
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		leftSystem.act(delta);
		rightSystem.act(delta);
	};
	
	@Override
	public void onBackPress() {	}

}

