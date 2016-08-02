package com.mygdx.mdh.game.Test;

import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 08/06/2016.
 */
public class EffectTest {

    public EffectTest () {

        Combat testCombat = Combat.loadFromJSON("combat01");
        Character lord = testCombat.getCharacters().get(0);
        Character hagen = testCombat.getCharacters().get(1);

        //Hit Hagen
        lord.getAbilities().get(0).apply(hagen);
        LOG.print("[EffectTest] "+hagen);
        lord.getAbilities().get(0).apply(hagen);
        LOG.print("[EffectTest] "+hagen);
        lord.getAbilities().get(0).apply(hagen);
        LOG.print("[EffectTest] "+hagen);


        /*
        //Activate shield
        zubi.getAbilities().get(1).apply(zubi);

        //Hit Zubi
        hagen.getAbilities().get(0).apply(zubi);

        //Hit Zubi
        hagen.getAbilities().get(0).apply(zubi);

        zubi.startTurn();
        */

    }

}
