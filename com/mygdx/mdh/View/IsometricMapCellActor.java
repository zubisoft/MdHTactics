package com.mygdx.mdh.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.mdh.Model.MapCell;

/**
 * Created by zubisoft on 20/02/2016.
 */
public class IsometricMapCellActor extends Actor{

    Sprite sprite;
    OrthographicCamera cam;

    Vector2 cell;

    public IsometricMapCellActor (Sprite sprite,OrthographicCamera cam) {
        this.sprite = sprite;
        this.cam=cam;
        this.cell = new Vector2();
    }

    public void setCell (int x, int y) {
        cell.x=x;
        cell.y=y;
    }

    protected void positionChanged () {
        sprite.setPosition(getX(),getY());
    }


    @Override
    public Actor hit(float x, float y, boolean touchable) {

            final Vector3 intersection = new Vector3();
            Plane xzPlane = new Plane(new Vector3(0, 1, 0), 0);

                Ray pickRay = cam.getPickRay(Gdx.input.getX(), Gdx.input.getY());
                Intersector.intersectRayPlane(pickRay, xzPlane, intersection);

                //Vector2 clickPosition = localToStageCoordinates(new Vector2(x,y));


                System.out.println("[CharacterActor] Hit Event "+this.getX()+" "+this.getY()+" "+intersection.x+" "+intersection.z+" "+Gdx.input.getX()+" "+Gdx.input.getY());
                if(this.getX() >= (int)intersection.x && this.getX()< (int)intersection.x+1 && this.getY() >= (int)intersection.z && this.getY() < (int)intersection.z+1) {
                    //System.out.println("[CharacterActor] WOP");
                    sprite.setColor(1, 0, 0, 1);
                    return this;
                }

        return null;

    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }


    public Vector2 getPosition () {
        Vector3 v = cam.project(new Vector3(0,0,0));
        System.out.println("[Projected] "+v);

        v = cam.project(new Vector3(getX(),getY(),0.0f));
        return new Vector2(v.x,v.y);
    }

    public Vector2 getMapCell() {
        return cell;
    }


}
