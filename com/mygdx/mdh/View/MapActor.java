package com.mygdx.mdh.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.mdh.Controller.AbilityButtonClickListener;

/**
 * Created by zubisoft on 20/02/2016.
 */
public class MapActor extends Group {
    Texture texture;
    OrthographicCamera cam;
    SpriteBatch batch;
    //final Sprite[][] sprites = new Sprite[10][10];
    IsometricMapCellActor[][] mapCells = new IsometricMapCellActor[10][10];
    Matrix4 matrix = new Matrix4();

    public MapActor() {
        texture = new Texture(Gdx.files.internal("core/assets/bordered-tile.png"));
        cam = new OrthographicCamera(10, 10 * (Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth()));
        cam.position.set(5, 5, 10);
        cam.direction.set(-1, -1, -1);
        cam.near = 1;
        cam.far = 100;
        matrix.setToRotation(new Vector3(1, 0, 0), 90);

        for (int z = 0; z < 10; z++) {
            for (int x = 0; x < 10; x++) {
                Sprite s = new Sprite(texture);
                //s.setPosition(x, z);
                s.setSize(1, 1);

                mapCells[x][z] = new IsometricMapCellActor(s, cam);
                mapCells[x][z].setCell(x,z);
                mapCells[x][z].setPosition(x, z);
/*
                ClickListener eventListener = new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        event.getRelatedActor().setColor(Color.BLUE);
                        System.out.println("[CharacterActor] hit");
                    }
                };

                mapCells[x][z].addListener(eventListener);*/
                this.addActor(mapCells[x][z]);

            }
        }

        batch = new SpriteBatch();

        //Gdx.input.setInputProcessor(this);
    }

    public void draw() {

        ///Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        cam.update();

        batch.setProjectionMatrix(cam.combined);
        batch.setTransformMatrix(matrix);
        batch.begin();
        for(int z = 0; z < 10; z++) {
            for(int x = 0; x < 10; x++) {
                mapCells[x][z].draw(batch);
            }
        }
        batch.end();

        //checkTileTouched();
    }


    IsometricMapCellActor lastSelectedTile = null;

    private void checkTileTouched() {
        final Vector3 intersection = new Vector3();
        Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);

        if(Gdx.input.justTouched()) {
            Ray pickRay = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
            Intersector.intersectRayPlane(pickRay, xzPlane, intersection);
            int x = (int)intersection.x;
            int z = (int)intersection.z;
            //System.out.println("[CharacterActor] Manual Event "+x+" "+z+" "+intersection.x+" "+intersection.z);
            if(x >= 0 && x < 10 && z >= 0 && z < 10) {
                if(lastSelectedTile != null) lastSelectedTile.setColor(1, 1, 1, 1);
                mapCells[x][z].sprite.setColor(1, 0, 0, 1);
                lastSelectedTile = mapCells[x][z];
            }
        }
    }


    public IsometricMapCellActor getCell (int x, int y) {
        return mapCells[x][y];
    }

    public int getCellWidth () {
        return 10;
    }

    public int getCellHeigth () {
        return 10;
    }



}
