package org.alfredo.graphic.toy.tetris;

public class Grid {

    public int rows;
    public int cols;

    public int x0, y0;
    public int x1, y1;

    public int width, height;

    public int squareWidth;
    public int squareHeight;

    public Grid(int x0, int y0, int x1, int y1, int rows, int cols) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.width = Math.abs(x1 - x0);
        this.height = Math.abs(y1 - y0);

        this.rows = rows;
        this.cols = cols;

        squareWidth = (x1 - x0) / cols;
        squareHeight = (y1 - y0) / rows;
    }

    public int[] topCoords(int iRow, int iCol) {
        int[] coords = {0, 0};
        coords[0] = x0 + ((x1 - x0) * iCol) / cols ;
        coords[1] = y0 + ((y1 - y0) * iRow) / rows;
        return coords;
    }

    public int[] bottomCoords(int iRow, int iCol) {
        int[] coords = topCoords(iRow, iCol);
        coords[0] += squareWidth;
        coords[1] += squareHeight;
        return coords;
    }

    public int getSquareWidth() {
        return squareWidth;
    }

    public int getSquareHeight() {
        return squareHeight;
    }

}
