package com.mygdx.mdh.game.IA;

import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.map.IsoMapActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.Combat;
import com.mygdx.mdh.game.model.Map;
import com.mygdx.mdh.game.model.MapCell;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 03/04/2016.
 */
public class StrategyManager {

    Combat combat;
    CombatController controller;

    int numCharacters;
    float[][] characterDistances;


    public StrategyManager (CombatController c) {
        controller=c;
        combat=controller.getCombat();
        numCharacters = combat.getCharacters().size();

        characterDistances= new float[numCharacters][numCharacters];
    }

    public boolean nextAction(int index) {
        calculateDistances ();

        Character target=null;
        float distance=9999999;
        int targetIndex = 0;

        LOG.print(3,"[StrategyManager] Calculating next action for "+combat.getCharacters().get(index).getName(),LOG.ANSI_PURPLE);

        //Find closest target
        for (int x=0; x<numCharacters;x++) {

                if (    x!=index
                        && combat.getCharacters().get(x).isFriendly()
                        && !combat.getCharacters().get(x).isDead()
                        && characterDistances[index][x]<distance
                        )
                {
                    distance = characterDistances[index][x];
                    target = combat.getCharacters().get(x);
                    targetIndex = x;
                }
        }



        if(target != null) {

            LOG.print(3,"[StrategyManager] Closes target: "+target.getName(),LOG.ANSI_PURPLE);

            //TODO define range of abilities como dios manda
            if (distance < 2 ) {

                controller.setCurrentSelectedAbility(combat.getCharacters().get(index).getAbilities().get(0));
                controller.executeCurrentAbility(controller.getCharacterActor(target));

            } else {
                IsoMapActor map = controller.getMap();
                MapCell sourceCell = combat.getCharacters().get(index).getCell();
                MapCell targetCell = combat.getCharacters().get(targetIndex).getCell();

                //TODO define range of movement como dios manda
                MapCell resultCell = closestCellToTarget(sourceCell,targetCell,2);

                LOG.print(3,"[StrategyManger] Chosen cell "+resultCell+" "+resultCell.isOccupied()+" "+resultCell.hashCode());

                IsoMapCellActor resultCellActor = map.getCell(resultCell.getMapCoordinates());

                LOG.print(3,"[StrategyManger] Chosen cellactor "+resultCellActor+" "+resultCellActor.getCell().isOccupied()+" "+resultCell.hashCode());


                controller.getCharacterActor(combat.getCharacters().get(index)).moveToCell(resultCellActor);
            }
        }

        return false;

    }

    public void calculateDistances () {
        for (int x=0; x<numCharacters;x++) {
            for (int y = 0; y < numCharacters; y++) {
                characterDistances[x][y] = Map.distance(combat.getCharacters().get(x).getCell(), combat.getCharacters().get(y).getCell());

            }
        }
    }

    public MapCell closestCellToTarget (MapCell sourceCell, MapCell targetCell, int maxMovement) {

        float distance=9999999;
        MapCell resultCell = null;


        for (MapCell cell: combat.getMap().getCells(sourceCell, maxMovement)) {
            if (Map.distance(cell,targetCell) < distance
                    && Map.distance(cell,targetCell)>0
                    && !cell.isOccupied()) {
                distance = Map.distance(cell,targetCell);
                resultCell = cell;
            }

        }

        return resultCell;


    }

}
