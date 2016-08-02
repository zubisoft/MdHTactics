package com.mygdx.mdh.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdx.mdh.game.characters.CharacterActor;
import com.mygdx.mdh.game.controller.AbilityButtonClickListener;
import com.mygdx.mdh.game.CombatController;
import com.mygdx.mdh.game.controller.CharacterChangeListener;
import com.mygdx.mdh.game.controller.EffectButtonClickListener;
import com.mygdx.mdh.game.model.Ability;
import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.Assets;
import com.mygdx.mdh.game.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zubisoft on 11/02/2016.
 */
public class CombatHUD extends Stage implements CharacterChangeListener {

    List<AbilityButton> abilityButtons;
    List<EffectButton> effectIcons;

    MessageBar messageBar;

    public CombatController controller;

    Table hudTableLayout;
    final Table effectsLayout = new Table();

    public static String notificationText;

    Stack abilitiesBar;

    Texture sprite = new Texture(Gdx.files.internal("core/assets/graphics/HUD-background.png"));
    Texture abilityBarSprite = new Texture(Gdx.files.internal("core/assets/graphics/HUD-bar.png"));
    Texture characterHUDSprite = new Texture(Gdx.files.internal("core/assets/graphics/combatui/character_hud.png"));


    Label characterInfoAttack;
    Label characterInfoDefense;
    Label characterInfoMovement;
    Label characterInfoHealth;
    Label characterInfoAP;

    static InfoBox infoBox;
    static boolean showInfo;
    static boolean showAbilities;

    public CombatHUD (CombatController controller) {

        this.controller = controller;

        abilityButtons = new ArrayList<>();

        notificationText="Hello World";

        messageBar = new MessageBar(this);
        this.addActor(messageBar);


        characterInfoAttack = new  Label("",Assets.uiSkin, "default-font", Color.YELLOW);
        characterInfoDefense = new  Label("",Assets.uiSkin, "default-font", Color.YELLOW);
        characterInfoMovement = new  Label("",Assets.uiSkin, "default-font", Color.YELLOW);
        characterInfoHealth = new Label("",Assets.uiSkin, "default-font", Color.YELLOW);
        characterInfoAP = new Label("",Assets.uiSkin, "default-font", Color.YELLOW);

        characterInfoHealth.setPosition(180,75);
        characterInfoAP.setPosition(255,75);

        characterInfoAttack.setPosition(180,45);
        characterInfoDefense.setPosition(235,45);
        characterInfoMovement.setPosition(290,45);

        infoBox = new InfoBox();

        showInfo = false;
        showAbilities = false;

        this.addListener(new InputListener());

        for (Character c: controller.getCombat().getCharacters()) {
            c.addListener(this);
        }


        effectsLayout.left().bottom();
        effectsLayout.setPosition(150,8);
        effectsLayout.setSize(200,10);

        hudTableLayout = new Table();
        hudTableLayout.padTop(25);
        hudTableLayout.align(Align.center);
        hudTableLayout.top();

        abilitiesBar = new Stack();
        abilitiesBar.setPosition(480,0);
        abilitiesBar.setSize(318,113);
        abilitiesBar.add(new Image(abilityBarSprite));
        abilitiesBar.add(hudTableLayout);


    }


    public void showAbilityButtons(Character selectedCharacter) {

        hudTableLayout.clear();
        for (Ability ability : selectedCharacter.getAbilities()) {
            System.out.println("abi"+ability.getPic());
            AbilityButton actor = new AbilityButton(ability);

            hudTableLayout.add(actor).size(35,35).pad(2);

            EventListener eventListener = new AbilityButtonClickListener(actor);
            actor.addListener(eventListener);
            //this.addActor(actor);
            abilityButtons.add(actor);
        }

        this.addActor(abilitiesBar);

        showAbilities = true;



    }

    public void hideAbilityButtons() {

        LOG.print("[CombatHUD] Hiding buttons ");
        for (Actor actor :this.abilityButtons) {
            actor.removeListener(actor.getListeners().first());
            actor.remove();
        }
        this.abilityButtons.clear();

        showAbilities = false;

    }

    public void showMessageBar (String message) {

        LOG.print("[CombatHUD] Showing message bar");

        messageBar.setMessage(message);
        messageBar.show();
        controller.setCombatPaused(true);

    }

    public void showMessageBar (String message, int duration) {
        LOG.print("[CombatHUD] Showing message bar");

        messageBar.setMessage(message);
        messageBar.show(duration);
        controller.setCombatPaused(true);

    }


    public java.util.List<AbilityButton> getAbilityButtons() {
        return abilityButtons;
    }




    public void renderCharacterInfoBox (SpriteBatch batch) {
        batch.draw(characterHUDSprite,0,0);
        batch.draw(controller.getSelectedCharacter().portrait,15,15);

        characterInfoAttack.setText(""+controller.getSelectedCharacter().getCharacter().getAttack());
        characterInfoDefense.setText(""+controller.getSelectedCharacter().getCharacter().getDefence());
        characterInfoMovement.setText(""+controller.getSelectedCharacter().getCharacter().getMovement());
        characterInfoHealth.setText(""+controller.getSelectedCharacter().getCharacter().getHealth()+"/"+controller.getSelectedCharacter().getCharacter().getMaxHealth());
        characterInfoAP.setText(""+controller.getSelectedCharacter().getCharacter().getAvailableActions()+"/"+controller.getSelectedCharacter().getCharacter().getMaxActions());

        characterInfoHealth.draw(batch,1.0f);
        characterInfoAP.draw(batch,1.0f);
        characterInfoAttack.draw(batch,1.0f);
        characterInfoDefense.draw(batch,1.0f);
        characterInfoMovement.draw(batch,1.0f);



        //TODO: hacer esto en cada iteracion es una gitanada
        effectsLayout.clear();
        for (Effect e : controller.getSelectedCharacter().getCharacter().getEffects()) {
            //System.out.print("Active effects: "+e.getName());

            EffectButton eb = new EffectButton(e);
            effectsLayout.add(eb).size(20,20).pad(2);

            EventListener eventListener = new EffectButtonClickListener(eb);
            eb.addListener(eventListener);
        }

        this.addActor(effectsLayout);

        effectsLayout.draw(batch,1.0f);

    }

    public static void showInfo (String text, float x, float y) {
        if(text != null) showInfo = true;
        else showInfo = false;

        infoBox.setTextArea(text);
        infoBox.setPosition(x,y);
    }



    public void update  (float deltaTime) {

        if (!CharacterActor.actionInProgress())
            messageBar.act(deltaTime);


    }

    public void render (SpriteBatch batch) {

        this.act();


        batch.setColor(1f, 1f, 1f,  1f);
        batch.draw(sprite,950,0);
        batch.setColor(1f, 1f, 1f, 1f);


        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.draw(batch, notificationText, 1000, 100);


        //Draw current character HUD
        if (controller.getSelectedCharacter() != null ) {

            renderCharacterInfoBox (batch);

            if (    showAbilities &&
                    controller.getSelectedCharacter().isActive() &&
                    controller.getSelectedCharacter().isFriendly() &&
                    !controller.getSelectedCharacter().actionInProgress()
                    ) {

                abilitiesBar.draw(batch, 1.0f);


            }

        }

        messageBar.draw(batch,1.0f);


        if (showInfo)
            infoBox.draw(batch,1.0f);


    }




    public void onCharacterHit (int damage)  {
    }

    public void onCharacterActive (Character c)  {
        if (controller.getSelectedCharacter()==null) return;
        if (c == controller.getSelectedCharacter().getCharacter()) {
            showAbilityButtons(c);
        }
    }

    public void onCharacterInactive (Character c)  {
        if (controller.getSelectedCharacter()==null) return;
        if (c == controller.getSelectedCharacter().getCharacter()) {
            hideAbilityButtons();
        }
    }

}
