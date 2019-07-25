package org.alfredo.graphic.toy.tetris;

import processing.core.PApplet;


/**
 * Singleton Class that inherits all the Processing machinery by extending the PApplet class.
 * Calling run() starts the Processing loop, which then drives the application flow:
 * settings() and setup() are automatically called to initialize stage params (one time only),
 * then the Processing loop periodically calls draw() to redraw the application.
 * Also notice Processing's user events methods.
 * All access to processing drawing primitives and functions must be through the this object only.
 * To get a reference to the single object of this class use getInstance().
 * Keep this class simple, all the logic of the game should be inside Game class.
 */
public class TetrisPApplet extends PApplet {

    private static TetrisPApplet instance;
    private static String[] processingArgs = {"org.alfredo.graphic.toy.tetris.TetrisPApplet"};


    /* Processing sketch initialization and setup */

    @Override
    public void settings() {
        // processing method to init most basic sketch params (runs only once)
        size(AppConfig.getInstance().SCREEN_WIDTH, AppConfig.getInstance().SCREEN_HEIGHT);
        // smooth(3);
    }

    @Override
    public void setup() {
        // processing method to setup the stage (runs only once, after PApplet.settings)
        frameRate(AppConfig.getInstance().FRAME_RATE);

        // initialize our Game class
        Game.getInstance().gameSetup();

    }


    /* Processing draw loop */

    @Override
    public void draw() {
        // processing method called periodically (draw loop)
        // draw the background
        Game.getInstance().setBackground();
        // display figures after applying events
        Game.getInstance().display();
    }


    /* Processing Events */

    @Override
    public void keyPressed() {
        Game.getInstance().keyPressed();
    }

    @Override
    public void mousePressed() {
        background(64);  // TODO: remove this
    }


    /* Custom methods */

    public static TetrisPApplet getInstance() {
        if (instance == null) {
            instance = new TetrisPApplet();
        }
        return instance;
    }

    /**
     * Entry point to start Processing Application
     */
    public static void run() {
        PApplet.runSketch(processingArgs, getInstance());
    }

}
