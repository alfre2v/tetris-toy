package org.alfredo.graphic.toy.tetris;


/**
 * Global config paramenters for the application
 */
public class AppConfig {

    /* Application screen settings */

    public int SCREEN_WIDTH = 640;
    public int SCREEN_HEIGHT = 650;
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

    private static AppConfig config = null;

    public static AppConfig getInstance() {
        if (config == null)
            config = new AppConfig();
        return config;
    }

}
