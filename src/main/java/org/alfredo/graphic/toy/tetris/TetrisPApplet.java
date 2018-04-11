package org.alfredo.graphic.toy.tetris;

import processing.core.PApplet;


public class TetrisPApplet extends PApplet {

    private static TetrisPApplet instance;
    private static String[] processingArgs = {"org.alfredo.graphic.toy.tetris.TetrisSketch"};
    private static Game game;


    public void settings() {
        size(AppConfig.getInstance().SCREEN_WIDTH, AppConfig.getInstance().SCREEN_HEIGHT);
        // smooth(3);
    }

    public void setup() {
        // setup processing stage (run only once)
        frameRate(AppConfig.getInstance().FRAME_RATE);
        // create game
        game = new Game();
    }

    public void draw() {
        // draw the background
        game.setBackground();
        // display figures after applying events
        game.display();
    }


    /* Events */

    public void keyPressed() {
        game.keyPressed();
    }

    public void mousePressed() {
        background(64);
    }


    public static void run() {
        PApplet.runSketch(processingArgs, getInstance());
    }

    public static TetrisPApplet getInstance() {
        if (instance == null) {
            instance = new TetrisPApplet();
        }
        return instance;
    }

    public static Game getGame() {
        return game;
    }

}
