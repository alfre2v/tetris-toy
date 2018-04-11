package org.alfredo.graphic.toy.tetris;

import processing.core.PApplet;


public class TetrisBackground implements Displayable {

    // screen size
    private static final int screenWidth = AppConfig.getInstance().SCREEN_WIDTH;
    private static final int screenHeight = AppConfig.getInstance().SCREEN_HEIGHT;

    // Wall widths
    private static final int leftWallWidth = AppConfig.getInstance().BORDER_LEFT_WIDTH;
    private static final int rightWallWidth = AppConfig.getInstance().BORDER_RIGHT_WIDTH;
    private static final int topWallWidth = AppConfig.getInstance().BORDER_TOP_HEIGHT;
    private static final int bottomWallWidth = AppConfig.getInstance().BORDER_BOTTOM_HEIGHT;

    // Wall colors
    private static final int leftWallColor = AppConfig.getInstance().BORDER_LEFT_COLOUR;
    private static final int rightWallColor = AppConfig.getInstance().BORDER_RIGHT_COLOUR;
    private static final int topWallColor = AppConfig.getInstance().BORDER_TOP_COLOUR;
    private static final int bottomWallColor = AppConfig.getInstance().BORDER_BOTTOM_COLOUR;

    //private int colour;
    //private int wallWidth;  // TODO: Not used. Delete?

    /* Processing PApplet that renders all graphics */
    private static PApplet P = TetrisPApplet.getInstance();

    /* App config */
    private static AppConfig config = AppConfig.getInstance();

    private void drawTopWall() {
        P.noStroke();
        P.fill(topWallColor);
        P.rect(0, 0, screenWidth, topWallWidth);
    }

    private void drawBottomWall() {
        P.pushMatrix();
        P.translate(0, screenHeight - bottomWallWidth);
        P.noStroke();
        P.fill(bottomWallColor);
        P.rect(0, 0, screenWidth, bottomWallWidth);
        P.popMatrix();
    }

    private void drawLeftWall() {
        P.noStroke();
        P.fill(leftWallColor);
        P.rect(0, topWallWidth, leftWallWidth, screenHeight - topWallWidth - bottomWallWidth);
    }

    private void drawRightWall() {
        P.pushMatrix();

        P.translate(screenWidth - leftWallWidth, 0);

        P.noStroke();
        P.fill(rightWallColor);
        P.rect(0, topWallWidth, rightWallWidth, screenHeight - topWallWidth - bottomWallWidth);

        P.popMatrix();
    }

    public void display() {
        drawTopWall();
        drawBottomWall();
        drawLeftWall();
        drawRightWall();
    }

}
