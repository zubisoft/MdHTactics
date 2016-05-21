package com.mygdx.mdh.screens;

/**
 * Created by zubisoft on 20/03/2016.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.CombatRenderer;


public class CombatScreen extends AbstractGameScreen {
    private static final String TAG = CombatScreen.class.getName();

    CombatController combatController;
    CombatRenderer combatRenderer;

    private boolean paused;

    public CombatScreen(ScreenManager game) {

        super(game);


    }

    @Override
    public void render(float deltaTime) {
        // Do not update gameScreen world when paused.
        if(!paused) {
            // Update gameScreen world by the time that has passed since last rendered frame.
            combatController.update(deltaTime);
        }

        // Set clear screen color to: CornFlower blue
        Gdx.gl.glClearColor(0x64/255.f, 0x95/255.f, 0xed/255.f, 0xff/255.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render gameScreen world to screen
        combatRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        combatRenderer.resize(width, height);
    }

    @Override
    public void show() {
        // Initialize controller and renderer
        combatController = new CombatController(gameScreen);
        combatRenderer = new CombatRenderer(combatController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {
        combatRenderer.dispose();
        combatController.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        // Only called on Android!
        paused = false;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return combatController.multiplexer;
    }

}