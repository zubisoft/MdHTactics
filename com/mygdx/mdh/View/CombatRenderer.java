package com.mygdx.mdh.View;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.mdh.Controller.CombatController;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by zubisoft on 13/02/2016.
 */
public class CombatRenderer {

    CombatController controller;

    OrthographicCamera camera;

    TiledMapRenderer tiledMapRenderer;

    public CombatRenderer(CombatController controller) {
        this.controller=controller;

        camera = new OrthographicCamera();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera.setToOrtho(false,w,h);
        camera.update();

        controller.getViewport().setCamera(camera);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(controller.getTiledMap());


    }

    private void renderHUD(SpriteBatch batch) {
        Texture sprite = new Texture(Gdx.files.internal("core/assets/HUD-background.png"));
        batch.setColor(1f, 1f, 1f,  0.8f);
        batch.draw(sprite,450,0);
        batch.setColor(1f, 1f, 1f, 1f);

        BitmapFont font = new BitmapFont();
        font.draw(batch, "Hello World", 450, 100);

        for (AbilityButton a: controller.getAbilityButtons()) {
            a.draw(batch);
        }

        if (controller.getEOTButton() != null)
            controller.getEOTButton().draw(batch,1.0f);

    }

    private void renderMap() {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    private void renderObjects(SpriteBatch batch) {
        for (CharacterActor c: controller.getCharacterActors()) {
            c.draw();
        }
    }


    public void render (SpriteBatch batch) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        renderMap();

        batch.begin();
        renderObjects(batch);
        renderHUD(batch);

        batch.end();

    }
}
