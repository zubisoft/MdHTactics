package com.mygdx.mdh.game;

import com.badlogic.gdx.math.Vector2;


/**
 * Created by zubisoft on 20/02/2016.
 */

import com.badlogic.gdx.graphics.OrthographicCamera;
public class CameraManager {

        Vector2 position;
        float zoom;

        public CameraManager() {
            position = new Vector2(0,0);
            zoom = 1.0f;
        }

        public void update(float deltaTime) {

        }

        public void applyTo(OrthographicCamera camera) {
            camera.position.x = position.x;
            camera.position.y = position.y;
            camera.zoom = zoom;
            camera.update();
        }

    public void move (int deltax, int deltay) {
        if (position.x+deltax >= 512 & position.x+deltax <= 800 )
            this.position.x = position.x+deltax;

        if (position.y+deltay >= 352 & position.y+deltay <= 448)
            this.position.y = position.y+deltay;

        this.zoom = zoom;

    }


    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
