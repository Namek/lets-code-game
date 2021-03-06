package com.letscode.lcg.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleSystem extends Actor {
	public ParticleSystem(String scriptFilename) {
		emitter = new ParticleEffect();
		emitter.load(Gdx.files.internal("data/" + scriptFilename), 
		            Gdx.files.internal("data"));
		emitter.start();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		emitter.update(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		emitter.setPosition(getX(), getY());
		emitter.draw(batch);
	};
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		emitter.setPosition(x, y);
	};
	
	private ParticleEffect emitter;
}
