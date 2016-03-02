package com.mygdx.mdh.View;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.mdh.Controller.CombatController;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by zubisoft on 13/02/2016.
 */
public class CombatRenderer {

    OrthographicCamera camera;
    private OrthographicCamera cameraGUI;

    CombatController controller;



    TiledMapRenderer tiledMapRenderer;

    public CombatRenderer(CombatController controller) {
        this.controller=controller;

        camera = new OrthographicCamera();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera.setToOrtho(false,w,h);

        camera.update();

        cameraGUI = new OrthographicCamera();
        cameraGUI.setToOrtho(false,w,h);
        cameraGUI.update();

        controller.getViewport().setCamera(camera);

        tiledMapRenderer = new OrthogonalTiledMapRenderer(controller.getTiledMap());

    }



    private void renderMap() {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    private void renderObjects(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (CharacterActor c: controller.getCharacterActors()) {
            c.draw(batch);
        }
        batch.end();
    }

    private void renderHUD(SpriteBatch batch) {

        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();

        controller.combatHUD.render(batch);

        batch.end();
    }




    public void render (SpriteBatch batch) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();


        controller.cameraManager.applyTo(camera);
        controller.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        controller.map.draw(batch);
        batch.end();

        //renderMap();
        renderObjects(batch);
        renderHUD(batch);




    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
}
