package com.mygdx.mdh.game.IA;

import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.map.IsoMapActor;
import com.mygdx.mdh.game.map.IsoMapCellActor;
import com.mygdx.mdh.game.model.*;
import com.mygdx.mdh.game.model.Character;
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

        Ability chosenAbility;

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


            chosenAbility = chooseAbility(combat.getCharacters().get(index), distance, target);

            //TODO define range of abilities como dios manda
            if (distance < chosenAbility.getRange() ) {

                controller.setCurrentSelectedAbility(chosenAbility);
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

    public Ability chooseAbility(Character currentCharacter, float tentativeTargetDistance, Character tentativeTarget) {
        Ability healAvailable = null;
        Ability buffAvailable = null;
        Ability debuffAvailable = null;
        Ability damageAvailable = null;


        for (Ability a: currentCharacter.getAbilities()) {

            if (a.isEnabled()) {
                switch (a.getType()) {
                    case HEAL:
                        if (healAvailable == null) {
                            healAvailable = a;
                        } else if (Math.random()<0.5) {
                            //To randomize which attack the enemy chooses
                            healAvailable = a;
                        }
                        break;
                    case MELEE:
                        if (damageAvailable == null) {
                            damageAvailable = a;
                        } else if (Math.random()<0.5) {
                            //To randomize which attack the enemy chooses
                            damageAvailable = a;
                        }
                        break;
                    case RANGED:
                        if (damageAvailable == null) {
                            damageAvailable = a;
                        } else if (Math.random()<0.5) {
                            //To randomize which attack the enemy chooses
                            damageAvailable = a;
                        }
                        break;
                    case BUFF:
                        if (buffAvailable == null) {
                            buffAvailable = a;
                        } else if (Math.random()<0.5) {
                            //To randomize which attack the enemy chooses
                            buffAvailable = a;
                        }
                        break;
                    case DEBUFF:
                        if (debuffAvailable == null) {
                            debuffAvailable = a;
                        } else if (Math.random()<0.5) {
                            //To randomize which attack the enemy chooses
                            debuffAvailable = a;
                        }

                        break;
                }
            }

        }

        //If target in range and weak, go for the kill
        if (damageAvailable != null) {
            if (tentativeTargetDistance <= currentCharacter.getMovement() + damageAvailable.getRange()
                    && tentativeTarget.getHealth() < tentativeTarget.getMaxHealth() * 0.30
                    ) {
                return buffAvailable;
            }
        }

        //If health < 25%, heal if possible
        if ( healAvailable!= null && currentCharacter.getHealth() < currentCharacter.getMaxHealth()*0.25) {
            return healAvailable;
        }

        //If too far to hit after movement, apply buff if not yet applied
        if(buffAvailable!= null) {
            if (tentativeTargetDistance > currentCharacter.getMovement() + damageAvailable.getRange()
                    && currentCharacter.hasEffect(buffAvailable.getEffects().get(0))) {
                return buffAvailable;
            }
        }

        //If target character not debuffed and still healthy
        if(debuffAvailable!= null) {
            if (tentativeTargetDistance <= currentCharacter.getMovement() + debuffAvailable.getRange()
                    && currentCharacter.getHealth() > currentCharacter.getMaxHealth() * 0.50
                    && tentativeTarget.hasEffect(debuffAvailable.getEffects().get(0))) {
                return debuffAvailable;
            }
        }

        //Otherwise attack!
        return damageAvailable;
    }

}
