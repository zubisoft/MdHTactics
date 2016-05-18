package com.mygdx.mdh;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.CombatRenderer;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.Constants;


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



import java.io.File;


public class MDHTactics extends ApplicationAdapter {




	CombatController combatController;
	CombatRenderer combatRenderer;



	@Override
	public void create () {

		// Set LigGdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());


		/*
		// Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);
		// Start gameScreen at menu screen
		ScreenTransition transition = ScreenTransitionSlice.init(2,
				ScreenTransitionSlice.UP_DOWN, 10, Interpolation. pow5Out);
		setScreen(new MenuScreen(this), transition);
		 */

		//Initialize graphics
		//Gdx.graphics.setWindowedMode(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);

		//Initialize main gameScreen logic
		//combatController = new CombatController();
		//combatRenderer = new CombatRenderer(combatController);




	}



	@Override
	public void render () {

		combatController.update(0);
		combatRenderer.render();

	}





/*
	public static void main(String[] args) {
		System.setProperty("user.name", "EnglishWords");


		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CanyonBunny";
		cfg.width = 200;
		cfg.height = 120;

		new LwjglApplication(new CanyonBunnyMain(), cfg);
	}
*/


}
