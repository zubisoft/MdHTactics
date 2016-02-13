package com.mygdx.mdh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.mdh.View.CharacterActor;
import com.mygdx.mdh.Model.Combat;
import com.mygdx.mdh.Controller.CombatStage;
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
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	public static SpriteBatch batch;
	BitmapFont font;


	CombatStage stage;

	@Override
	public void create () {

		//Initialize graphics
		Gdx.graphics.setWindowedMode(1000,700);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();


		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);

		camera.update();


		//Initialize map
		tiledMap = new TmxMapLoader().load("core/assets/sample.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		//Gdx.input.setInputProcessor(new CombatController(this));


		Combat combat = new Combat();
		combat.populateSampleMap();

		stage = new CombatStage(tiledMap, combat);
		Gdx.input.setInputProcessor(stage);
		stage.getViewport().setCamera(camera);

		batch = new SpriteBatch();

		loadCharacters ();



	}

	private void loadCharacters () {
		/*images = new ArrayList<Texture>();
		for (CharacterActor c: stage.getCharacterActors()) {
			images.add(new Texture(Gdx.files.internal(c.getCharacter().getPic())));
		}*/
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();

		//batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);

		for (CharacterActor c: stage.getCharacterActors()) {
			c.draw();
		}

		for (AbilityButton a: stage.getAbilityButtons()) {
			a.draw();
		}

		Texture sprite = new Texture(Gdx.files.internal("core/assets/HUD-background.png"));
		batch.setColor(1f, 1f, 1f,  0.8f);
		batch.draw(sprite,450,0);
		batch.setColor(1f, 1f, 1f, 1f);

		font = new BitmapFont();
		font.draw(batch, "Hello World", 450, 100);



		batch.end();

	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

	public void setTiledMap(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
	}
}
