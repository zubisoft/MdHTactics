package com.mygdx.mdh.View;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Group;



/**
 * Created by zubisoft on 22/02/2016.
 */
public class IsoMapActor extends Group{
    Sprite s;
    IsoMapCellActor[][] mapCells = new IsoMapCellActor[10][10];

    final int CELLWIDTH = 128;
    final int CELLHEIGTH = 64;

    public IsoMapActor() {
        Texture texture = new Texture(Gdx.files.internal("core/assets/graphics/tile-grass-64x32.png"));

        for (int z = 0; z < 10; z++) {
            for (int x = 0; x < 10; x++) {
                s = new Sprite(texture);
                //s.setPosition(x, z);
                s.setSize(CELLWIDTH, CELLHEIGTH);
                s.setPosition(x*CELLWIDTH+Math.floorMod(z,2)*CELLWIDTH/2, z*CELLHEIGTH/2);

                mapCells[x][z] = new IsoMapCellActor(s);
                mapCells[x][z].setCell(x,z);
                mapCells[x][z].setWidth(CELLWIDTH);
                mapCells[x][z].setHeight(CELLHEIGTH);
                mapCells[x][z].setPosition(x*CELLWIDTH+Math.floorMod(z,2)*CELLWIDTH/2, z*CELLHEIGTH/2);


                this.addActor(mapCells[x][z]);

            }
        }
    }


    public void draw(SpriteBatch batch) {

        for(int z = 0; z < 10; z++) {
            for(int x = 0; x < 10; x++) {
                mapCells[x][z].draw(batch);
            }
        }

    }

    public IsoMapCellActor getCell (int x, int y) {
        return mapCells[x][y];
    }

    public int getCellWidth () {
        return 10;
    }

    public int getCellHeigth () {
        return 10;
    }
}

