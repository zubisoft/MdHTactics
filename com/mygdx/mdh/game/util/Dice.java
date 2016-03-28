package com.mygdx.mdh.game.util;

/**
 * Created by zubisoft on 26/03/2016.
 */
public class Dice {

    private static int total=0;

    public static int roll(int numdice, int dicetype) {
        total=0;
        for (int i=0; i<numdice;i++)
            total +=  (int)(Math.random()*dicetype) + 1;

        return total;
    }

    public static int roll(int dicetype) {
        return (int)(Math.random()*dicetype) + 1;
    }

}
