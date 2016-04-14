package com.mygdx.mdh.game.characters.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.mygdx.mdh.game.characters.CharacterActor;

/**
 * Created by zubisoft on 09/04/2016.
 */
public class GameWaitAction   extends GameAction {

    /**
     * Created by zubisoft on 07/03/2016.
     */


        boolean begin;
        float totalTime;
    float duration;



        public GameWaitAction(float duration) {

            this.begin=true;
            this.totalTime=0;
            this.duration = duration;
        }



        @Override
        public boolean act (float delta)  {

            totalTime += delta;
            if (totalTime > duration)
                   return true;

            return false;

        }



    }


