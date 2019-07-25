package org.alfredo.graphic.toy.tetris.squares;

import org.alfredo.graphic.toy.tetris.Game;
import org.alfredo.graphic.toy.tetris.Grid;

/**
 * Simple square for a cell in game grid. It is colored with a uniform color,
 * so no need to provide any real rotation or inversion logic.
 */
public class SimpleSquare extends BaseSquare {


    public SimpleSquare(int row, int col, int colour) {
        super(row, col, colour);
    }

    @Override
    public void rotateLeft() {
    }

    @Override
    public void rotateRight() {
    }

    @Override
    public void mirrorInvert() {
    }

    @Override
    public void randomRotate() {
    }

    @Override
    public void randomInvert() {
    }

    @Override
    public void display() {

        Grid grid = Game.getInstance().getGrid();

        int[] top = grid.topCoords(row, col);

        //noStroke();
        P.stroke(212);
        P.fill(colour);
        P.rectMode(P.CORNER);
        P.rect(top[0], top[1], grid.squareWidth, grid.squareHeight);

        //pushMatrix();
        //translate(top[0], top[1]);
        //noStroke();
        //fill(colour);
        //rect(0, 0, butt[0], butt[1]);
        //popMatrix();
    }

}
