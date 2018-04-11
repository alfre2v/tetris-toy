package org.alfredo.graphic.toy.tetris.figures.floor;

import org.alfredo.graphic.toy.tetris.Game;
import org.alfredo.graphic.toy.tetris.errors.IrrecoverableError;
import org.alfredo.graphic.toy.tetris.figures.BaseComposableFigure;
import org.alfredo.graphic.toy.tetris.squares.BaseSquare;
import org.alfredo.graphic.toy.tetris.squares.SimpleSquare;

public class ComposableFloorLine extends BaseComposableFigure {

    private int[][] positions;

    private int[][] colors;


    public ComposableFloorLine(Game game, int row, int col) {
        super(game, row, 0);

        int dimRows = 1;
        int dimCols = config.NUM_COLS;

        positions = new int[dimRows][dimCols]; // array initialized to 0s
        colors = new int[dimRows][dimCols];  // array initialized to 0s

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

    /**
     * Adds a square object to this floor line.
     * If the square is not in the same row as the line, it will be moved there.
     * An unchecked exception will be raised if the column is already occupied
     * because this is a bug, prevent this by calling canAddSquare() first.
     *
     * @param square Square object to add to this floor line
     */
    public void addSquare(BaseSquare square) {

        if (square == null) return;

        int r = square.row;
        int c = square.col;

        // raise exception if group[0][c] is already occupied
        if (group[0][c] != null) throw new IrrecoverableError("Cant add square to floorline: col already occupied");

        group[0][c] = square;
        numSquares++;

        square.setPosition(this.row, c);  // relocate square to this row
    }

    /**
     * Checks whether a square object can be added to this floorline.
     *
     * @param square Square object to add to this floor line
     * @return true if the square can be added
     */
    public boolean canAddSquare(BaseSquare square) {
        //check that cell at square's column is not occupied
        return (square == null) || (group[0][square.col] == null);
    }

    public boolean canAddSquare(int insertCol) {
        //check that cell at square's column is not occupied
        return (insertCol >= 0) && (insertCol < figCols()) && (group[0][insertCol] == null);
    }

    public void removeSquare(int col) {
        if (group[0][col] != null) {
            group[0][col] = null;
            numSquares--;
        }
    }

    public boolean isEmpty() {
        return numSquares == 0;
    }

    public boolean isFull() {
        return numSquares == config.NUM_COLS;
    }


}

