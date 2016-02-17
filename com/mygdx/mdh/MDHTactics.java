package com.mygdx.mdh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.mdh.View.CharacterActor;
import com.mygdx.mdh.Model.Combat;
import com.mygdx.mdh.Controller.CombatController;
import com.mygdx.mdh.View.CombatRenderer;
import com.mygdx.mdh.View.AbilityButton;

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


public class MDHTactics extends ApplicationAdapter {

	TiledMap tiledMap;


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
