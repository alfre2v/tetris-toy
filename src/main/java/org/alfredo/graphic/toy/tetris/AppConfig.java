package org.alfredo.graphic.toy.tetris;


/**
 * Global config paramenters for the application
 */
public class AppConfig {

    /* Application screen settings */

    // TODO: load values from file https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html

    public int SCREEN_WIDTH = 640;
    public int SCREEN_HEIGHT = 680;
    public int FRAME_RATE = 60;

    public int NUM_ROWS = 30;
    public int NUM_COLS = 20;

    /* Application proportions */

    public int DEFAULT_BORDER_WIDTH = 50;  // TODO: Not used. Delete?

    public int BORDER_LEFT_WIDTH = 50;
    public int BORDER_RIGHT_WIDTH = 50;
    public int BORDER_TOP_HEIGHT = 10;
    public int BORDER_BOTTOM_HEIGHT = 10;

    /* Application Colors */

    public int DEFAULT_COLOUR = 0xFFC2C2C2;  // Grey: 194 (C2 = 194, alpha: FF)

    public int BORDER_LEFT_COLOUR = 0xFF7692B7;  // blueish
    public int BORDER_RIGHT_COLOUR = 0xFFB7B576;  // redish       //or #B77686 pinkish ?
    public int BORDER_TOP_COLOUR = DEFAULT_COLOUR;
    public int BORDER_BOTTOM_COLOUR = DEFAULT_COLOUR;

    public  int BACKGROUND_COLOR = 0;

    //                            vibrant green,   dark red,       blue
    public int[] FIGURE_COLORS = { 0xFF00BE00,     0xFF993333,     0xFF0093FF };

    /* Game dynamics */
    public double TIME_FALL_ANIMATION = 0.1;
    public double TIME_ADVANCE_TO_BOTTOM = 0.9;
    public double TIME_ARROW_REPEAT = 0.18;
    public double TIME_SHOW_TEXT = 7.0;

    /* Text Messages */
    public String TEXT_WELCOME = "Welcome to Tetris-Toy\n" +
            "A Toy implementation of the Tetris game in Java " +
            "using the Processing graphic library";

    public String TEXT_AUTHOR = "Written by Alfredo Valles\ngithub: alfre2v\n2018";

    public String TEXT_GAMEOVER = "Game over";

    public String TEXT_TOTALS = "Total score: %d\nLevels: %d\nTime played: %d";

    private static AppConfig config = null;

    public static AppConfig getInstance() {
        if (config == null)
            config = new AppConfig();
        return config;
    }

}
