package com.mygdx.mdh.game.util;

import com.badlogic.gdx.graphics.*;

import java.awt.*;

/**
 * Created by zubisoft on 02/04/2016.
 */
 public  class LOG {

    static int level = 3;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public enum DEBUG_COLOR {
        RED(27), B(203);

        private int numVal;

        DEBUG_COLOR(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    public static void setLevel (int l) {
        level = l;
    }

    public static void print(String s) {
        System.out.println (s);
    }


    public static void print(int l, String s) {
        if (l<=level)
            System.out.println (s);
    }

    public static void print(int l, String s, String color) {
        if (l<=level)
            System.out.println (color+ s+ ANSI_RESET);
    }

}
