package com.mygdx.mdh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.CombatRenderer;


/*
public class MDHTactics extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {

		img = new Texture(Gdx.files.internal("core/assets/badlogic.jpg"));
	}

	@Override
	public void render () {

	}
}
*/

import com.badlogic.gdx.tools.texturepacker.TexturePacker;


public class MDHTactics extends ApplicationAdapter {


	public static SpriteBatch batch;

	CombatController combatController;
	CombatRenderer combatRenderer;

	@Override
	public void create () {




		//Initialize graphics
		Gdx.graphics.setWindowedMode(1000,700);

		//Initialize main game logic
		combatController = new CombatController();
		combatRenderer = new CombatRenderer(combatController);

		batch = new SpriteBatch();

	}



	@Override
	public void render () {

		combatController.update();
		combatRenderer.render(batch);

	}

}
