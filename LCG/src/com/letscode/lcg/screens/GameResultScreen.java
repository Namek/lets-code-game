package com.letscode.lcg.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.letscode.lcg.Context;
import com.letscode.ui.BaseScreen;

public class GameResultScreen extends BaseScreen {

	public GameResultScreen(Context context, String winner) {
		super(context.app);
	    
	    String text = context.getPlayerNickname().equals(winner) ? "You have won!" : "Player " + winner + " has won."; 
	    Label label = new Label(text, app.skin); 
	    label.setAlignment(Align.center); 
	    mainTable.add(label).expand().fill(); 
	    
	    mainTable.layout(); 
	}

	@Override
	public void onBackPress() {	}

}
