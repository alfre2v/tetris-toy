package org.alfredo.graphic.toy.tetris.figures;

import org.alfredo.graphic.toy.tetris.Game;
import org.alfredo.graphic.toy.tetris.squares.BaseSquare;
import org.alfredo.graphic.toy.tetris.squares.SimpleSquare;

public class ComposableI extends BaseComposableFigure {

    private int[][] positions = {
            {1, 1, 1, 1},
    };

    private int[][] colors = {
            {0xFF00BE00, 0xFF00BE00, 0xFF00BE00, 0xFF00BE00},
    };


    public ComposableI(int row, int col) {
        super(row, col);
        initializeGroup();
    }

    @Override
    public int[][] positions() {
        return positions;
    }

    @Override
    public int[][] colors() {
        return colors;
    }

    @Override
    public BaseSquare createSquare(int row, int col, int colour) {
        return new SimpleSquare(row, col, colour);
    }

}