package com.mygdx.mdh.game.controller;

import com.mygdx.mdh.game.model.Campaign;
import com.mygdx.mdh.game.model.Mission;

/**
 * Created by zubisoft on 20/09/2016.
 */
public interface GameEventListener {

    default void onMissionUnlocked (Mission m) {};
    default void onCampaignUnlocked (Campaign c) {};

}
