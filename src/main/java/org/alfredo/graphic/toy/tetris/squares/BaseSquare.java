package org.alfredo.graphic.toy.tetris.squares;

import org.alfredo.graphic.toy.tetris.*;
import org.alfredo.graphic.toy.tetris.constants.Move;
import org.alfredo.graphic.toy.tetris.constants.Rotation;
import processing.core.PApplet;

abstract public class BaseSquare implements DisplayableFigure {

    public int colour;
    public int row;
    public int col;
    public boolean movable;
    public Grid grid;

    public String name;

    protected Game game;

    /* Processing PApplet that renders all graphics */
    protected static PApplet P = TetrisPApplet.getInstance();

    /* App config */
    protected static AppConfig config = AppConfig.getInstance();


    public BaseSquare(Game game, int row, int col, int colour) {
        this.game = game;
        this.grid = game.getGrid();
        this.row = row;
        this.col = col;
        this.colour = colour;
        this.movable = true;
        this.name = "square(col=" + col + ", row=" + row + ")";
    }

    @Override
    public void move(Move m) {
        // apply movement
        if      (m == Move.UP)    moveUp();
        else if (m == Move.DOWN)  moveDown();
        else if (m == Move.LEFT)  moveLeft();
        else if (m == Move.RIGHT) moveRight();
    }

    @Override
    public void moveUp() {
        if ((row > 0) && movable) row--;
    }

    @Override
    public void moveDown() {
        if ((row < config.NUM_ROWS - 1) && movable) row++;
    }

    @Override
    public void moveLeft() {
        if ((col > 0) && movable) col--;
    }

    @Override
    public void moveRight() {
        if ((col < config.NUM_COLS - 1) && movable) col++;
    }

    @Override
    public boolean isMovable() {
        return movable;
    }

    @Override
    public void setMovable(boolean t) {
        movable = t;
    }

    @Override
    public void rotate(Rotation r) {
        if      (r == Rotation.LEFT)  rotateLeft();
        else if (r == Rotation.RIGHT) rotateRight();
    }

    @Override
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public void setColor(int colour) {
        this.colour = colour;
    }

}

