package com.mygdx.mdh.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.model.Roll;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.model.effects.EffectListener;
import com.mygdx.mdh.game.model.effects.EffectManagerListener;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 28/03/2016.
 */
public class EffectManager {


    List<EffectManagerListener> effectListeners;

    public static final EffectManager instance = new EffectManager();


    private EffectManager () {
        effectListeners= new ArrayList<>();
    }

    public boolean apply( Effect e ) {

        System.out.println("[EffectManager] Applying: "+e.getClass()+" to "+e.getTarget());

        if (e.getTarget() == null) return false;
        //target = c.getCharacterActor(e.getTarget());

        notifyEffect(e);

        e.init();

        if (e.getGameSegment()== Effect.GameSegmentType.IMMEDIATE) {
            applySourceEffects(e);
            applyTargetEffects(e);
            e.execute();
        }

        System.out.println("[EffectManager] Ready to apply effect!");

        e.apply();





        return true;
    }


    public boolean execute( Effect e ) {

        //Gdx.app.debug("[EffectManager]", "Applying: "+e.getClass()+" to "+e.getTarget());


        if (e.getTarget() == null) return false;
        //target = c.getCharacterActor(e.getTarget());

        e.init();

        applySourceEffects(e);
        applyTargetEffects(e);

        e.execute();

        return true;
    }

    public boolean starTurn( Effect e ) {
        e.startTurn();
        return execute(e);
    }

    /**
     * Apply all the modifiers in the source character on the effect.
     * e.g. buff effects will trigger here to increase the strength of the effect.
     * @param e
     */
    public void applySourceEffects (Effect e) {
        //Gdx.app.debug("[EffectManager]"," Source: "+e.getSource().getEffects().size());
        for (Effect tmp: e.getSource().getEffects()) {
            tmp.process(e);
        }
    }

    /**
     * Apply all the modifiers in the target character on the effect.
     * e.g. defensive effects will trigger here protecting against harmful effects.
     * @param e
     */
    public void applyTargetEffects (Effect e) {
        //Gdx.app.debug("[EffectManager]"," Target: "+e.getTarget().getEffects().size());
        for (Effect tmp: e.getTarget().getEffects()) {
            tmp.process(e);
        }
    }



    public void notifyEffect (Effect e) {

        if (effectListeners.size()==0) return;

        for (EffectManagerListener l: effectListeners)
            l.onEffectProcessed(e);
    }

    public void addEffectListener (EffectManagerListener l) {
        if (effectListeners.contains(l)) return;

        effectListeners.add(l);
    }


}
