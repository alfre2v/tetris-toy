package org.alfredo.graphic.toy.tetris.figures;

import org.alfredo.graphic.toy.tetris.*;
import org.alfredo.graphic.toy.tetris.constants.Move;
import org.alfredo.graphic.toy.tetris.constants.Rotation;
import org.alfredo.graphic.toy.tetris.squares.BaseSquare;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract public class BaseComposableFigure implements DisplayableFigure {

    public int row;
    public int col;
    public boolean movable;


    protected BaseSquare[][] group;

    public int numSquares;

    public int color;

    /* Processing PApplet that renders all graphics */
    protected static PApplet P = TetrisPApplet.getInstance();

    /* Game object */
    protected static Game game = Game.getInstance();

    /* App config */
    protected static AppConfig config = AppConfig.getInstance();


    public BaseComposableFigure(int row, int col) {
        this.row = row;
        this.col = col;
        this.movable = true;
        this.numSquares = 0;
    }

    public BaseComposableFigure(int row, int col, int color) {
        this(row, col);
        this.color = color;
    }

    abstract public int[][] positions();

    abstract public int[][] colors();

    abstract public BaseSquare createSquare(int row, int col, int colour);


    public BaseSquare[][] initializeGroup() {

        if (group != null) return group;

        int[][] positions = positions();
        int[][] colors = colors();

        int dimRows = positions.length;
        int dimCols = positions[0].length;

        group = new BaseSquare[dimRows][dimCols];

        for (int i = 0; i < dimRows; i++) {
            for (int j = 0; j < dimCols; j++) {
                if (positions[i][j] != 0) {
                    group[i][j] = createSquare(row + i, col + j, colors[i][j]);
                    numSquares++;
                }
            }
        }
        setPosition(this.row, this.col);
        return group;
    }

    public int figRows() {
        return group.length;
    }

    public int figCols() {
        return group[0].length;
    }

    public int indexTopRow() {
        return row;
    }

    public int indexBottomRow() {
        return row + figRows() - 1;
    }

    public int indexLeftCol() {
        return col;
    }

    public int indexRightCol() {
        return col + figCols() - 1;
    }

    /**
     * Return a list of n rows of this figure from bottom to top
     *
     * @param n number of rows to return
     * @return last rows starting at the bottom of the figure
     */
    public List<BaseSquare[]> lastRows(int n) {

        List<BaseSquare[]> rows = new ArrayList<>();

        for (int i = figRows()-1; i >= Math.max(0, figRows()-n); i--) {
            rows.add(group[i]);
        }
        return rows;
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
        if ((row > 0) && movable) {
            for (int i = 0; i< figRows(); i++) {
                for (int j = 0; j< figCols(); j++) {
                    if (group[i][j] != null)
                        group[i][j].moveUp();
                }
            }
            row--;
        }
    }

    @Override
    public void moveDown() {
        if ((row < config.NUM_ROWS - figRows()) && movable) {
            for (int i = 0; i < figRows(); i++) {
                for (int j = 0; j< figCols(); j++) {
                    if (group[i][j] != null)
                        group[i][j].moveDown();
                }
            }
            row++;
        }

    }

    @Override
    public void moveLeft() {
        if ((col > 0) && movable) {
            for (int i = 0; i < figRows(); i++) {
                for (int j = 0; j< figCols(); j++) {
                    if (group[i][j] != null)
                        group[i][j].moveLeft();
                }
            }
            col--;
        }

    }

    @Override
    public void moveRight() {
        if ((col < config.NUM_COLS - figCols()) && movable) {
            for (int i = 0; i < figRows(); i++) {
                for (int j = 0; j< figCols(); j++) {
                    if (group[i][j] != null)
                        group[i][j].moveRight();
                }
            }
            col++;
        }
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
        if (r == Rotation.NONE)  return;

        int rotDimRows = figCols();
        int rotDimCols = figRows();

        // new position so rotated figure fit inside board
        int newRow = Math.max(0, Math.min(this.row, config.NUM_ROWS -  rotDimRows));
        int newCol = Math.max(0, Math.min(this.col, config.NUM_COLS -  rotDimCols));

        BaseSquare[][] rotGroup = new BaseSquare[rotDimRows][rotDimCols];

        int antiI, antiJ;  // inverse rotation coordinates for (i, j)

        for (int i=0; i<rotDimRows; i++) {
            for (int j=0; j<rotDimCols; j++) {
                if (r == Rotation.LEFT) { antiI = j; antiJ = figCols() - i - 1; }
                else                    { antiI = figRows() - j - 1; antiJ = i; }

                rotGroup[i][j] = group[antiI][antiJ];

                if (rotGroup[i][j] != null) {
                    rotGroup[i][j].setPosition(newRow + i, newCol + j);
                    if (r == Rotation.LEFT) rotGroup[i][j].rotateLeft();
                    else                    rotGroup[i][j].rotateRight();
                    group[antiI][antiJ] = null;  // prevent memory loitering
                }
            }
        }
        this.group = rotGroup;
        this.row = newRow;
        this.col = newCol;
    }

    @Override
    public void rotateLeft() {
        rotate(Rotation.LEFT);
    }

    @Override
    public void rotateRight() {
        rotate(Rotation.RIGHT);
    }

    @Override
    public void display() {
        for (int i = 0; i< figRows(); i++) {
            for (int j = 0; j< figCols(); j++) {
                if (group[i][j] != null)
                    group[i][j].display();
            }
        }
    }

    @Override
    public void setPosition(int row, int col) {
        // readjust position so figure always fit inside the board
        this.row = Math.max(row, 0);
        this.col = Math.max(col, 0);
        this.row = Math.min(row, config.NUM_ROWS -  figRows());
        this.col = Math.min(col, config.NUM_COLS -  figCols());

        // set the position of all components of the group
        for (int i = 0; i<figRows(); i++) {
            for (int j = 0; j< figCols(); j++) {
                if (group[i][j] != null)
                    group[i][j].setPosition(this.row + i, this.col + j);
            }
        }

    }

    @Override
    public void setColor(int color) {
        // set the color of all components of the group
        for (int i = 0; i<figRows(); i++) {
            for (int j = 0; j< figCols(); j++) {
                if (group[i][j] != null)
                    group[i][j].setColor(color);
            }
        }
        this.color = color;
    }

    @Override
    public void mirrorInvert() {
        int numRows = figRows();
        BaseSquare[] tmpRow;

        // apply mirror inversion transformation
        for (int i=0, j=numRows-1; i<j; i++, j--) {
            tmpRow = group[i];
            group[i] = group[j];
            group[j] = tmpRow;
        }
        // call invert on each individual Square
        for (int i = 0; i< figRows(); i++) {
            for (int j = 0; j< figCols(); j++) {
                if (group[i][j] != null)
                    group[i][j].mirrorInvert();
            }
        }
        setPosition(row, col);
    }

    @Override
    public void randomRotate() {
        // get random int [0, 4), apply that number of consecutive rotations
        Random r = new Random();
        int num_rot = r.nextInt(4);
        for (int i=0; i<num_rot; i++) {
            rotate(Rotation.RIGHT);
        }
    }

    @Override
    public void randomInvert() {
        // get random int [0, 2), apply an inversion if 1
        Random r = new Random();
        if (r.nextInt(2) == 1) {
            mirrorInvert();
        }
    }

    public void printDebugGroup(BaseSquare[][] grp, String headerLine) {

        System.out.println("------------ " + headerLine + " ------------------");

        System.out.println("group.length: "+ grp.length);
        System.out.println("group[0].length: "+ grp[0].length);

        for (int i=0; i<grp.length; i++) {
            for (int j=0; j<grp[0].length; j++) {
                if (grp[i][j] != null)
                    System.out.print(grp[i][j].name + " ");
                else
                    System.out.print("null ");
            }
            System.out.println();
        }
    }


}
