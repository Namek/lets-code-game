package com.letscode.lcg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.letscode.lcg.Assets;
import com.letscode.lcg.Context;
import com.letscode.lcg.actor.ParticleSystem;
import com.letscode.ui.BaseScreen;

public class GameResultScreen extends BaseScreen {

	private ParticleSystem leftSystem;
	private ParticleSystem rightSystem;
	private boolean hasPlayerWon;
	private final static Color wonBackgroundTintColor = Color.valueOf("4CFF00");
	private final static Color lostBackgroundTintColor = Color.valueOf("FF1E38");
	
	public GameResultScreen(Context context, String winner) {
		super(context.app);
	    hasPlayerWon = context.getPlayerNickname().equals(winner);
		
	    String text = hasPlayerWon ? "You have won!" : "Player " + winner + " has won."; 
	    Label label = new Label(text, app.skin); 
	    label.setAlignment(Align.center); 
	    mainTable.add(label).expand().fill(); 
	    
	    if (hasPlayerWon) {
		    leftSystem = new ParticleSystem("winner.ps");
		    rightSystem = new ParticleSystem("winner.ps");
		    
		    leftSystem.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
		    rightSystem.setPosition(Gdx.graphics.getWidth() / 4 * 3, Gdx.graphics.getHeight() / 2);
		    addActor(leftSystem);
		    addActor(rightSystem);
	    }
	    
	    mainTable.layout(); 
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (leftSystem != null) {
			leftSystem.act(delta);
			rightSystem.act(delta);
		}
	};
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(hasPlayerWon ? wonBackgroundTintColor : lostBackgroundTintColor);
		batch.draw(Assets.backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		super.draw(batch, parentAlpha);
	}
	
	@Override
	public void onBackPress() {	}

}

