package com.mygdx.mdh.View;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by zubisoft on 22/02/2016.
 */
public class IsoMapCellActor extends Actor {
    Sprite sprite;
    Vector2 cell;

    public IsoMapCellActor (Sprite sprite) {
        this.sprite = sprite;
        this.cell = new Vector2();
    }


    public void setCell (int x, int y) {
        cell.x=x;
        cell.y=y;
    }

    public Vector2 getMapCell() {
        return cell;
    }

    protected void positionChanged () {
        /*sprite.setPosition(getX(),getY());*/
    }


    @Override
    public Actor hit(float x, float y, boolean touchable) {


        if (x<=getWidth()/2 & y < 0.5f*x+getHeight()/2 & y>=-0.5f*x+getHeight()/2) {
            //sprite.setColor(1, 0, 0, 1);
            return this;
        }

        if (x>getWidth()/2 & y < -0.5f*x+1.5f*getHeight() & y>=0.5f*x-0.5f*getHeight()) {
            //sprite.setColor(1, 0, 0, 1);
            return this;
        }

        return null;
    }


    public void draw(SpriteBatch batch) {
        sprite.draw(batch);

    }


    public Vector2 getPosition () {
        return new Vector2(getX(),getY());
    }

    public String toString () {
        return "Cell "+getMapCell()+" @"+getPosition();
    }


}
