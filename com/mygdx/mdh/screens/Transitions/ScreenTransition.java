package com.mygdx.mdh.screens.Transitions;

/**
 * Created by zubisoft on 20/03/2016.
 */

        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ScreenTransition {
    public float getDuration();
    public void render(SpriteBatch batch, Texture curScreen, Texture nextScreen, float alpha);
}