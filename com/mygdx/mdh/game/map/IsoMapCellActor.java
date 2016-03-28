package com.mygdx.mdh.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.mdh.game.model.MapCell;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 22/02/2016.
 */
public class IsoMapCellActor extends Actor {
    Sprite sprite;
    MapCell cell;

    final TextureRegion texture = Assets.instance.maps.get("tiles/tile-transparent-256x128");
    final TextureRegion highlightTexture = Assets.instance.maps.get("tiles/tile-white-256x128");

    //final Sprite highlightSprite = new Sprite(new Texture(Gdx.files.internal("core/assets/graphics/tile-green-256x128.png")));
    boolean showHighlight;

    final Skin uiSkin = new Skin(Gdx.files.internal("core/assets/skin/uiskin.json"));
    Label la;


    public IsoMapCellActor (MapCell cell) {
        this.sprite = new Sprite(texture);
        this.cell = cell;
        showHighlight=false;

        la = (new Label(cell.getMapCoordinates()+"", uiSkin, "default-font", Color.ORANGE));
    }

    @Override
    protected void positionChanged () {
        sprite.setPosition(getX(),getY());
    }

    @Override
    protected void sizeChanged () {
        sprite.setSize(getWidth(),getHeight());
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

    @Override
    public void draw (Batch batch, float parentAlpha) {

        //Get the current color before drawing (Useful to allow animating the color from actions)
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);

        sprite.draw(batch);

        la.setPosition(getX()+60,getY()+30);
        la.draw(batch,1);


        if (showHighlight) {
            //highlightSprite.draw(batch,0.2f);
            batch.draw(highlightTexture,getX(),getY(),getWidth(),getHeight());
        }


        batch.setColor(1f, 1f, 1f, 1f);

    }


    public Vector2 getPosition () {
        return new Vector2(getX(),getY());
    }

    public String toString () {
        return "Cell "+cell+" @"+getPosition();
    }


    public void highlight (Color c) {

        showHighlight = true;
        setColor(c);

    }

    public void removeHighlight () {

        showHighlight = false;
        setColor(new Color(1f, 1f, 1f, 1f));

    }


    public Vector2 getMapCoordinates() {
        return cell.getMapCoordinates();
    }


    public MapCell getCell() {
        return cell;
    }

    public void setCell(MapCell cell) {
        this.cell = cell;
    }
}
