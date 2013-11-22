package com.letscode.lcg.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.letscode.lcg.Context;
import com.letscode.lcg.LcgApp;
import com.letscode.lcg.network.EventsInterface;
import com.letscode.lcg.ui.BaseScreen;

public class ErrorScreen extends BaseScreen {
	private Context context;

	public ErrorScreen(Context context) {
		super(context.app);
		this.context = context;
		EventsInterface.unsubscribeAll();
		
		String text = "Excuse us but some connection error occured. Please try again :)"; 
	    Label label = new Label(text, app.skin); 
	    label.setAlignment(Align.center); 
	    mainTable.add(label).expand().fill();
	}

	@Override
	public void onBackPress() {
		context.app.switchScreens(new ConnectingScreen(context, LcgApp.DEFAULT_HOSTNAME, LcgApp.DEFAULT_PORT));
	}
}
