package org.alfredo.graphic.toy.tetris.figures;

import org.alfredo.graphic.toy.tetris.squares.BaseSquare;
import org.alfredo.graphic.toy.tetris.Game;
import org.alfredo.graphic.toy.tetris.squares.SimpleSquare;

public class ComposableBox extends BaseComposableFigure {

    private int[][] positions = {
            {1, 1},
            {1, 1}
    };

    private int[][] colors = {
            {0xFF00BE00, 0xFF00BE00},
            {0xFF00BE00, 0xFF00BE00}
    };


    public ComposableBox(Game game, int row, int col) {
        super(game, row, col);
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
    public BaseSquare createSquare(Game game, int row, int col, int colour) {
        return new SimpleSquare(game, row, col, colour);
    }

}
