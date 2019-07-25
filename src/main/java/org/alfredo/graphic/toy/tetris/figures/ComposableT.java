package org.alfredo.graphic.toy.tetris.figures;

import org.alfredo.graphic.toy.tetris.Game;
import org.alfredo.graphic.toy.tetris.squares.BaseSquare;
import org.alfredo.graphic.toy.tetris.squares.SimpleSquare;

public class ComposableT extends BaseComposableFigure {

    private int[][] positions = {
            {0, 1, 0},
            {1, 1, 1},
    };

    private int[][] colors = {
            {0,          0xFF00BE00, 0         },
            {0xFF00BE00, 0xFF00BE00, 0xFF00BE00},
    };

    public ComposableT(int row, int col) {
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

