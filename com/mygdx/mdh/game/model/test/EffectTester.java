package com.mygdx.mdh.game.model.test;

import com.mygdx.mdh.game.model.Character;
import com.mygdx.mdh.game.model.effects.Effect;
import com.mygdx.mdh.game.util.Dice;
import com.mygdx.mdh.game.util.LOG;

/**
 * Created by zubisoft on 23/10/2016.
 */
public class EffectTester {


    public static void runTests () {

        LOG.setLevel(2);

        testMinoanGuard ();
        testMinotaur();
        testDamageModifier();

        testMinoanPriestess();
        testShields();
        testDamageEffects();
        testStunEffects();
        testAttributeModifier();
        testRemover();
        testHeal();



    }


    public static void testMinoanGuard () {

        /* Testing Axe */
        Character c1 = Character.loadFromJSON("minoan_guard");
        Character c2 = Character.loadFromJSON("test_dummy");
        c1.setAttack(20);


        int[] damages = new int[1000];
        int test = 0;

        for (int i = 0; i < 1000; i++) {
            c2.setHealth(c2.getMaxHealth());

            c1.getAbilities().get(0).apply(c2);

            damages[i] = c2.getMaxHealth() - c2.getHealth();
        }


        //On average we should see 6.75

        for (int i = 0; i < 1000; i++) {

            test += damages[i];
        }


        if (test / 1000.0f > 6 && test / 1000.0f < 8)
            LOG.print(1, "[OK] Damage - " + c1.getAbilities().get(0).getName() + "@" + c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - " + c1.getAbilities().get(0).getName() + "@" + c1.getName(), LOG.ANSI_RED);


        /* Testing Bullrush */

        c1 = Character.loadFromJSON("minoan_guard");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setAttack(20);





        for (int i = 0; i < 1000; i++) {
            c2.setHealth(c2.getMaxHealth());

            c1.getEffects().clear();
            c1.getAbilities().get(1).apply(c1);
            c1.getAbilities().get(0).apply(c2);

            damages[i] = c2.getMaxHealth() - c2.getHealth();
        }


        //On average we should see 6.75
        test = 0;
        for (int i = 0; i < 1000; i++) {

            test += damages[i];
        }


        if (test / 1000.0f > 9 && test / 1000.0f < 12)
            LOG.print(1, "[OK] Buff - " + c1.getAbilities().get(1).getName() + "@" + c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Buff - " + c1.getAbilities().get(1).getName() + "@" + c1.getName(), LOG.ANSI_RED);




    }


    public static void testMinotaur () {

        /* Testing Axe */
        Character c1 = Character.loadFromJSON("minotaur");
        Character c2 = Character.loadFromJSON("test_dummy");


        int[] damages = new int[1000];


        for (int i = 0; i < 1000; i++) {
            c2.setHealth(c2.getMaxHealth());

            c1.getAbilities().get(0).apply(c2);

            damages[i] = c2.getMaxHealth() - c2.getHealth();
        }


        //On average we should see 6.75
        int test = 0;
        for (int i = 0; i < 1000; i++) {

            test += damages[i];
        }


        if (test / 1000.0f > 32 && test / 1000.0f < 38)
            LOG.print(1, "[OK] Damage - " + c1.getAbilities().get(0).getName() + "@" + c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - " + c1.getAbilities().get(0).getName() + "@" + c1.getName(), LOG.ANSI_RED);

        /* Testing Bullrush */

        c1 = Character.loadFromJSON("minotaur");
        c2 = Character.loadFromJSON("test_dummy");



        for (int i = 0; i < 1000; i++) {
            c2.setHealth(c2.getMaxHealth());

            c1.getEffects().clear();
            c1.getAbilities().get(1).apply(c1);
            c1.getAbilities().get(0).apply(c2);

            damages[i] = c2.getMaxHealth() - c2.getHealth();
        }


        //On average we should see 6.75
        test = 0;
        for (int i = 0; i < 1000; i++) {

            test += damages[i];
        }


        if (c1.getMovement() == 4 && test / 1000.0f > 39 && test / 1000.0f < 44)
            LOG.print(1, "[OK] Buff - " + c1.getAbilities().get(1).getName() + "@" + c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Buff - " + c1.getAbilities().get(1).getName() + "@" + c1.getName(), LOG.ANSI_RED);

    }

    public static void testMinoanPriestess() {

        /* Greater Dispell */
        Character c1 = Character.loadFromJSON("minoan_priestess");
        Character c2 = Character.loadFromJSON("test_dummy");
        Character c3 = Character.loadFromJSON("test_dummy");
        c2.setFriendly(true);
        c3.setFriendly(false);


        c2.getAbilities().get(2).apply(c3); //debuff
        c2.getAbilities().get(3).apply(c2); //buff
        c3.getAbilities().get(2).apply(c2); //debuff
        c3.getAbilities().get(3).apply(c3); //buff

        c1.getAbilities().get(2).apply(c2); //remover
        c1.getAbilities().get(2).apply(c3); //remover

        if (c2.getEffects().size() == 1 && c3.getEffects().size() == 1
                && c3.getEffects().get(0).getEffectType()== Effect.EffectType.BUFF
                && c2.getEffects().get(0).getEffectType()== Effect.EffectType.DEBUFF)
            LOG.print(2, "[OK] Remover - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[Error] Remover - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_RED);

        /* Embrace of the Snake */
        c1 = Character.loadFromJSON("minoan_priestess");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setLevel(10);
        c1.setAttack(20);
        c2.setMaxHealth(200);

        int[] damages = new int[1000];

        for (int i=0; i<1000; i++) {
            c2.setHealth(200);

            c1.getAbilities().get(0).apply(c2);
            c2.startTurn();
            c2.startTurn();

            damages[i] = 200-c2.getHealth();
        }

        int test = 0;
        for (int i=0; i<1000; i++) {

            test+=damages[i];
        }

        if (test/1000.0f > 9.5 && test/1000.0f < 12)
            LOG.print(1, "[OK] Damage - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_RED);


        /* Embrace of the Snake */
        c1 = Character.loadFromJSON("minoan_priestess");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setLevel(10);
        c1.setAttack(20);
        c2.setMaxHealth(200);

        for (int i=0; i<1000; i++) {
            c2.setHealth(200);
            c2.getEffects().clear();

            c1.getAbilities().get(0).apply(c2);
            c1.getAbilities().get(1).apply(c2);

            damages[i] = 200-c2.getHealth();
        }

        test = 0;
        for (int i=0; i<1000; i++) {

            test+=damages[i];
        }


        if (test/1000.0f > 102 && test/1000.0f < 105)
            LOG.print(1, "[OK] Damage - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_RED);

    }

    public static void testDamageEffects () {



        /* Testing Zubi Shuriken */
        Character c1 = Character.loadFromJSON("zubi_filmmaker");
        Character c2 = Character.loadFromJSON("cultist");


        int[] damages = new int[1000];

        c2.setMaxHealth(100);
        for (int i=0; i<1000; i++) {
            c2.setHealth(100);
            c2.setDefence(1);

            c1.getAbilities().get(0).apply(c2);

            damages[i] = 100-c2.getHealth();
        }


        //On average we should see 6.75
        int test = 0;
        for (int i=0; i<1000; i++) {

            test+=damages[i];
        }

        if (test/1000.0f > 5.75 && test/1000.0f < 7.75)
            LOG.print(1, "[OK] Damage - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_RED);


        /* Testing Zubi Proton Torpedoes */
        c1 = Character.loadFromJSON("zubi_starfleet");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setLevel(10);
        c1.setAttack(0);

        c2.setMaxHealth(100);
        for (int i=0; i<1000; i++) {
            c2.setHealth(100);
            c2.setDefence(Dice.roll(1,6));

            c1.getAbilities().get(3).apply(c2);

            damages[i] = 100-c2.getHealth();
        }

        test = 0;
        for (int i=0; i<1000; i++) {

            test+=damages[i];
        }

        if (test/1000.0f > 5.5 && test/1000.0f < 8)
            LOG.print(1, "[OK] Damage - "+c1.getAbilities().get(3).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - "+c1.getAbilities().get(3).getName()+"@"+c1.getName(), LOG.ANSI_RED);


        /* Testing Zubi Phaser */
        c1 = Character.loadFromJSON("zubi_starfleet");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setAttack(0);

        for (int i=0; i<1000; i++) {
            c2.setHealth(100);

            c1.getAbilities().get(0).apply(c2);

            damages[i] = 100-c2.getHealth();
        }

        test = 0;
        for (int i=0; i<1000; i++) {

            test+=damages[i];
        }

        if (test/1000.0f > 3 && test/1000.0f < 4)
            LOG.print(1, "[OK] Damage - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_RED);


        /* Testing Hagen Bacteria */
        c1 = Character.loadFromJSON("hagen_doctor");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setLevel(10);
        c1.setAttack(0);

        for (int i=0; i<1000; i++) {
            c2.setHealth(100);

            c1.getAbilities().get(2).apply(c2);
            c2.startTurn();
            c2.startTurn();
            c2.startTurn();

            damages[i] = 100-c2.getHealth();
        }

        test = 0;
        for (int i=0; i<1000; i++) {

            test+=damages[i];
        }

        if (test/1000.0f > 5 && test/1000.0f < 9)
            LOG.print(1, "[OK] Damage - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_RED);




        /* Testing Hagen Nitrogen */
        c1 = Character.loadFromJSON("hagen_doctor");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setAttack(0);
        c1.setLevel(10);

        for (int i=0; i<1000; i++) {
            c2.setHealth(100);

            c1.getAbilities().get(3).apply(c2);

            damages[i] = 100-c2.getHealth();
        }

        test = 0;
        for (int i=0; i<1000; i++) {

            test+=damages[i];
        }

        if (test/1000.0f > 9.5 && test/1000.0f < 11.5)
            LOG.print(1, "[OK] Damage - "+c1.getAbilities().get(3).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Damage - "+c1.getAbilities().get(3).getName()+"@"+c1.getName(), LOG.ANSI_RED);


    }


    public static void testStunEffects () {
        Character c1 = Character.loadFromJSON("zubi_filmmaker");
        Character c2 = Character.loadFromJSON("cultist");

        int[] stuns = new int[1000];

        c2.setMaxHealth(100);
        c1.setAttack(20);

        for (int i=0; i<1000; i++) {
            c2.setHealth(c2.getMaxHealth());
            c2.setAvailableActions(2);
            c2.getEffects().clear();

            c1.getAbilities().get(0).apply(c2);
            c2.startTurn();

            stuns[i] = 2-c2.getAvailableActions();
        }


        //On average we should see 6.75
        int test = 0;
        for (int i=0; i<1000; i++) {

            test+=stuns[i];
        }

        if (test/1000.0f > 0.23 && test/1000.0f < 0.27)
            LOG.print(1, "[OK] Stun - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Stun - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_RED);


        c1 = Character.loadFromJSON("hagen_doctor");
        c2 = Character.loadFromJSON("test_dummy");


        c2.setMaxHealth(100);
        c1.setAttack(20);

        for (int i=0; i<1000; i++) {
            c2.setHealth(c2.getMaxHealth());
            c2.setAvailableActions(2);
            c2.getEffects().clear();

            stuns[i] = 0;
            c1.getAbilities().get(0).apply(c2);

            c2.startTurn();
            stuns[i]  += 2-c2.getAvailableActions();
            c2.startTurn();
            stuns[i]  += 2-c2.getAvailableActions();
            c2.startTurn();
            stuns[i]  += 2-c2.getAvailableActions();
        }


        test = 0;
        for (int i=0; i<1000; i++) {

            test+=stuns[i];
        }

        if (test/1000.0f > 0.8 && test/1000.0f < 1.2)
            LOG.print(1, "[OK] Stun - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Stun - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_RED);

    }

    public static void testShields()  {
        Character c1 = Character.loadFromJSON("zubi_starfleet");
        Character c2 = Character.loadFromJSON("test_dummy");

        int[] results = new int[1000];

        for (int i=0; i<1000; i++) {
            c1.setHealth(c1.getMaxHealth());
            c1.getEffects().clear();
            results[i] = c1.getHealth();

            c1.getAbilities().get(1).apply(c1); //shield
            c2.getAbilities().get(0).apply(c1); //attack
            results[i] -= c1.getHealth();


        }

        int total = 0;
        for (int i=0; i<1000; i++) {
            total += results[i];

        }

        if (total/1000f >= 5 && total/1000f <= 6)
            LOG.print(1, "[OK] Shields - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Shields - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_RED);


        c1 = Character.loadFromJSON("zubi_filmmaker");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setLevel(10);
        c1.setDefence(0);
        c1.setMaxHealth(100);

        for (int i=0; i<1000; i++) {
            c1.setHealth(c1.getMaxHealth());
            c1.getEffects().clear();
            results[i] = c1.getHealth();

            c1.getAbilities().get(2).apply(c1); //shield
            c2.getAbilities().get(0).apply(c1); //attack
            c2.getAbilities().get(0).apply(c1); //attack
            c2.getAbilities().get(0).apply(c1); //attack
            results[i] -= c1.getHealth();

        }

        total = 0;
        for (int i=0; i<1000; i++) {
            total += results[i];

        }


        if (total/1000f >= 10 && total/1000f <= 10)
            LOG.print(1, "[OK] Shields - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Shields - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_RED);
    }


    public static void testAttributeModifier()  {
        Character c1 = Character.loadFromJSON("zubi_filmmaker");
        Character c2 = Character.loadFromJSON("test_dummy");

        c1.setDefence(0);

        int[] results = new int[1000];

        c1.getAbilities().get(1).apply(c2); //apply attribute reduction

        if (c2.getMovement() == 1)
            LOG.print(2, "[OK] Attribute Mod (Movement) - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[OK] Attribute Mod (Movement)  - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_RED);

        for (int i=0; i<1000; i++) {
            c1.setHealth(c1.getMaxHealth());
            results[i] = c1.getHealth();
            c2.getAbilities().get(1).apply(c1); //attack
            results[i] -= c1.getHealth();
        }

        int total = 0;
        for (int i=0; i<1000; i++) {
            total += results[i];
        }

        //Without the modifier, should be 50% chance of 10
        if (total/1000f >= 2.5 && total/1000f <= 3.5)
            LOG.print(2, "[OK] Attribute Mod (Attack) - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[OK] Attribute Mod (Attack) - "+c1.getAbilities().get(1).getName()+"@"+c1.getName(), LOG.ANSI_RED);



        /* Testing Zubi Tungsten */
        c1 = Character.loadFromJSON("zubi_filmmaker");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setLevel(10);

        c1.getAbilities().get(3).apply(c2); //apply attribute modifier

        if (c2.getAvailableActions() == 4)
            LOG.print(2, "[OK] Attribute Mod (Actions) - "+c1.getAbilities().get(3).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[OK] Attribute Mod (Actions)  - "+c1.getAbilities().get(3).getName()+"@"+c1.getName(), LOG.ANSI_RED);

        /* Testing Hagen Nitrogen */
        c1 = Character.loadFromJSON("hagen_doctor");
        c2 = Character.loadFromJSON("test_dummy");
        c1.setLevel(10);

        c1.getAbilities().get(3).apply(c2); //apply attribute modifier

        if (c2.getAttack() == -4)
            LOG.print(2, "[OK] Attribute Mod (Attack) - "+c1.getAbilities().get(3).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[OK] Attribute Mod (Attack)  - "+c1.getAbilities().get(3).getName()+"@"+c1.getName(), LOG.ANSI_RED);
    }


    public static void testRemover()  {
        Character c1 = Character.loadFromJSON("zubi_starfleet");
        Character c2 = Character.loadFromJSON("cultist");
        Character c3 = Character.loadFromJSON("doomsayer");
        c1.setLevel(10);


        c1.getEffects().clear();
        c1.getAbilities().get(1).apply(c1); //buff
        c2.getAbilities().get(1).apply(c1); //debuff
        c3.getAbilities().get(1).apply(c1); //debuff
        c1.getAbilities().get(2).apply(c1); //remover


        if (c1.getEffects().size() == 1)
            LOG.print(2, "[OK] Remover - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[OK] Remover - "+c1.getAbilities().get(2).getName()+"@"+c1.getName(), LOG.ANSI_RED);
    }

/*
    public void testStun()  {

        Character c1 = Character.loadFromJSON("zubi_filmmaker");
        Character c2 = Character.loadFromJSON("test_dummy");
        c1.setAttack(20);


        int[] results = new int[1000];

        for (int i=0; i<1000; i++) {
            c2.setHealth(c1.getMaxHealth());
            c2.getEffects().clear();

            c1.getAbilities().get(0).apply(c2); //attack
            c2.startTurn();
            c2.startTurn();
            c2.startTurn();


            results[i] = 2-c1.getAvailableActions();
        }

        int total = 0;
        for (int i=0; i<1000; i++) {
            total += results[i];
        }

        //Without the modifier, should be 50% chance of 10
        if (total/1000f >= 0.4 && total/1000f <= 0.6)
            LOG.print(2, "[OK] Stun - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(2, "[OK] Stun - "+c1.getAbilities().get(0).getName()+"@"+c1.getName(), LOG.ANSI_RED);
    }
*/


    public static void testHeal () {

        /* Testing Hagen Transfusion */
        Character c1 = Character.loadFromJSON("hagen_doctor");


        int[] results = new int[1000];

        c1.setMaxHealth(100);
        for (int i = 0; i < 1000; i++) {
            c1.setHealth(80);

            c1.getAbilities().get(1).apply(c1);
            c1.startTurn();
            c1.startTurn();

            results[i] = c1.getHealth() - 80;
        }


        //On average we should see 6.75
        int total = 0;
        for (int i = 0; i < 1000; i++) {

            total += results[i];
        }

        if (total / 1000.0f > 8.5 && total / 1000.0f < 9.5)
            LOG.print(1, "[OK] Heal - " + c1.getAbilities().get(1).getName() + "@" + c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Heal - " + c1.getAbilities().get(1).getName() + "@" + c1.getName(), LOG.ANSI_RED);
    }


    public static void testDamageModifier () {

        /* Testing Hagen Transfusion */
        Character c1 = Character.loadFromJSON("migo_harvester");
        Character c2 = Character.loadFromJSON("test_dummy");
        c1.setAttack(0);


        int[] results = new int[1000];
        int[] results2 = new int[1000];

        for (int i = 0; i < 1000; i++) {
            c2.setMaxHealth(100);
            c2.getEffects().clear();

            c1.getAbilities().get(0).apply(c2);
            c1.getAbilities().get(0).apply(c2);
            c1.getAbilities().get(0).apply(c2);
            c1.getAbilities().get(0).apply(c2);
            c1.getAbilities().get(0).apply(c2);
            c1.getAbilities().get(0).apply(c2);

            c2.setHealth(100);
            c1.setHealth(10);

            c1.getAbilities().get(2).apply(c2);

            results[i] = 100 - c2.getHealth();

            results2[i] = c1.getHealth()-10;
        }


        int total = 0;
        for (int i = 0; i < 1000; i++) {

            total += results[i];
        }


        if (total / 1000.0f > 15 && total / 1000.0f < 19)
            LOG.print(1, "[OK] Conditional Damage - " + c1.getAbilities().get(0).getName() + "@" + c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Conditional Damage - " + c1.getAbilities().get(0).getName() + "@" + c1.getName(), LOG.ANSI_RED);


        total = 0;
        for (int i = 0; i < 1000; i++) {

            total += results[i];
        }


        if (total / 1000.0f > 15 && total / 1000.0f < 19)
            LOG.print(1, "[OK] Drain Health - " + c1.getAbilities().get(2).getName() + "@" + c1.getName(), LOG.ANSI_GREEN);
        else
            LOG.print(1, "[Error] Drain Health  - " + c1.getAbilities().get(2).getName() + "@" + c1.getName(), LOG.ANSI_RED);
    }
}
