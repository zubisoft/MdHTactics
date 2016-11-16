package com.mygdx.mdh.screens.widgets;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.util.Assets;

/**
 * Created by zubisoft on 05/09/2016.
 */
public class UnlocksWindow extends Window {



    public void close() {
        this.getParent().removeActor(this);
    }

    public UnlocksWindow(Table contents) {
        super("Contents Unlocked",Assets.uiSkin);

        this.setSize(400,600); //Default size
        this.align(Align.left);
        this.setMovable(true);

        //Close window button
        Label closeButton = new Label("[x]",Assets.uiSkin,"handwritten_black" );
        this.getTitleTable().add(closeButton).align(Align.right);
        closeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        //Generate contents
        Table mainLayout = new Table();
        mainLayout.add(contents).center();


        //Wrap up
        TextureRegion tex = Assets.instance.guiElements.get("menus/info_box");
        NinePatchDrawable tableBackground = new NinePatchDrawable(new NinePatch(tex,20,20,20,20));

        mainLayout.setBackground(tableBackground);

        this.add(mainLayout);

        this.pack();

        this.setVisible(false);

    }

    public void show () {

        float x = this.getWidth();
        float y = this.getHeight();

        this.setVisible(true);
        this.setSize(0,0);

        this.addAction(Actions.alpha(0));

        this.addAction(
                Actions.parallel(
                        Actions.sizeTo(x,y,1, Interpolation.linear),
                        Actions.alpha(1,1, Interpolation.linear)
                )
        );


    }


}
