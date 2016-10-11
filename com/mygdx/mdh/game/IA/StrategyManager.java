package com.mygdx.mdh.game.IA;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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


    float[][] characterDistances;

    final int MAX_CHARACTERS = 20;

    public StrategyManager (CombatController c) {
        controller=c;
        combat=controller.getCombat();

        characterDistances= new float[MAX_CHARACTERS][MAX_CHARACTERS];
    }

    public int getNumCharacters() {
        return combat.getCharacters().size();
    }

    public boolean nextAction(Character c) {
        calculateDistances ();

        Character target=null;
        float distance=9999999;
        int targetIndex = 0;

        Ability chosenAbility;

        LOG.print(3,"[StrategyManager] Calculating next action for "+c,LOG.ANSI_PURPLE);

        //Find closest target
        for (int x=0; x<getNumCharacters();x++) {

                if (    x!=combat.getCharacters().indexOf(c)
                        && combat.getCharacters().get(x).isFriendly()
                        && !combat.getCharacters().get(x).isDead()
                        && characterDistances[combat.getCharacters().indexOf(c)][x]<distance
                        )
                {
                    distance = characterDistances[combat.getCharacters().indexOf(c)][x];
                    target = combat.getCharacters().get(x);
                    targetIndex = x;
                }
        }


        if(target != null) {

            chosenAbility = chooseAbility(c, distance, target);


            LOG.print(3,"[StrategyManager] Closest target: "+target+" Max Range:"+chosenAbility.getRange(),LOG.ANSI_PURPLE);

            if (chosenAbility.getType() == Ability.AbilityType.BUFF || chosenAbility.getType() == Ability.AbilityType.HEAL ) {
                distance = 0;
                target = c;
            }

            //TODO define range of abilities como dios manda
            if (distance <= chosenAbility.getRange() ) {

                LOG.print(3,"[StrategyManager] Action: "+chosenAbility,LOG.ANSI_PURPLE);

                if (chosenAbility.getArea()>0) {
                    MapCell targetCell = findBestAreaTarget(c.getCell(),chosenAbility.getRange(),chosenAbility.getArea());
                    controller.getMap().highlightCells(controller.getMap().getCell(targetCell),chosenAbility.getArea(), IsoMapActor.redHighlight);

                    controller.setCurrentSelectedAbility(chosenAbility);
                    //controller.showAreaOfEffect(targetCell);
                    controller.executeCurrentAbility( controller.getMap().getCell(targetCell));

                } else {

                    controller.setCurrentSelectedAbility(chosenAbility);
                    controller.executeCurrentAbility(controller.getCharacterActor(target));
                }


            } else {

                LOG.print(3,"[StrategyManager] Action: Move towards Target",LOG.ANSI_PURPLE);

                IsoMapActor map = controller.getMap();
                MapCell sourceCell = c.getCell();
                MapCell targetCell = combat.getCharacters().get(targetIndex).getCell();

                MapCell resultCell = closestCellToTarget(sourceCell,targetCell,c.getMovement(), chosenAbility.getRange());

                IsoMapCellActor resultCellActor = map.getCell(resultCell.getMapCoordinates());

                controller.getCharacterActor(c).moveToCell(resultCellActor, map);
            }
        }

        return false;

    }

    public void calculateDistances () {
        for (int x=0; x<getNumCharacters();x++) {
            for (int y = 0; y < getNumCharacters(); y++) {
                characterDistances[x][y] = Map.distance(combat.getCharacters().get(x).getCell(), combat.getCharacters().get(y).getCell());
            }
        }
    }

    public MapCell closestCellToTarget (MapCell sourceCell, MapCell targetCell, int maxMovement, float range) {

        /*
        float distance=9999999;
        MapCell resultCell = null;


        for (MapCell cell: combat.getMap().getCells(sourceCell, maxMovement)) {
            if (Map.distance(cell,targetCell) < distance
                    && Map.distance(cell,targetCell)>0
                    && !cell.isImpassable()
                    && !cell.isOccupied()) {
                distance = Map.distance(cell,targetCell);
                resultCell = cell;
            }

        }

        return resultCell;
        */

        //System.out.println("Shortest Path "+combat.getMap().getShortestPath(sourceCell,targetCell));

        return combat.getMap().getClosestCellWithRange(sourceCell,targetCell,maxMovement,range);


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
                      case DAMAGE:
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
                LOG.print(3,"[StrategyManager] Action: Attack weak target",LOG.ANSI_PURPLE);
                return damageAvailable;
            }
        }

        //If health < 25%, heal if possible
        if ( healAvailable!= null && currentCharacter.getHealth() < currentCharacter.getMaxHealth()*0.25) {
            LOG.print(3,"[StrategyManager] Action: Heal",LOG.ANSI_PURPLE);
            return healAvailable;
        }

        //If too far to hit after movement, apply buff if not yet applied
        if(buffAvailable!= null) {

            if (tentativeTargetDistance > currentCharacter.getMovement() + damageAvailable.getRange()
                    && !currentCharacter.hasEffect(buffAvailable.getEffects().get(0))) {
                LOG.print(3,"[StrategyManager] Action: Buffing",LOG.ANSI_PURPLE);
                return buffAvailable;
            }
        }

        //If target character not debuffed and still healthy
        if(debuffAvailable!= null) {
            if (tentativeTargetDistance <= currentCharacter.getMovement() + debuffAvailable.getRange()
                    && currentCharacter.getHealth() > currentCharacter.getMaxHealth() * 0.50
                    && !tentativeTarget.hasEffect(debuffAvailable.getEffects().get(0))) {
                LOG.print(3,"[StrategyManager] Action: DeBuffing",LOG.ANSI_PURPLE);
                return debuffAvailable;
            }
        }

        LOG.print(3,"[StrategyManager] Action: Generic attack"+damageAvailable.getName(),LOG.ANSI_PURPLE);

        //Otherwise attack!
        return damageAvailable;
    }


    public MapCell findBestAreaTarget(MapCell sourceCell, int range, int area) {

        //System.out.println("Exploring Range"+range+" area "+area+" inrange"+controller.getMap().getCellsInRange(sourceCell,range));

        MapCell bestTarget=null;
        int maxScore = 0;
        for (MapCell tentativeTarget: controller.getMap().getCellsInRange(sourceCell,range)) {
            int score = 0;
            for (MapCell auxAreaCell: controller.getMap().getCellsInRange(tentativeTarget,area)) {
                if (auxAreaCell.getCharacter() != null) {

                    if (auxAreaCell.getCharacter().isFriendly()) {
                        score++;
                    }
                    else {
                        score--;
                    }
                }
            }

            if (score>=maxScore) {
                bestTarget = tentativeTarget;
                maxScore = score;
            }
        }


        return bestTarget;

    }


}
