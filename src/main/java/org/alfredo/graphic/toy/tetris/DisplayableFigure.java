package org.alfredo.graphic.toy.tetris;


import org.alfredo.graphic.toy.tetris.constants.Move;
import org.alfredo.graphic.toy.tetris.constants.Rotation;

/**
 * Basic interface that all objects to be drawn must implement
 */
public interface DisplayableFigure extends Displayable {

    void move(Move m);

    boolean isMovable();

    void setMovable(boolean t);

    void setColor(int c);

    void moveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    void rotate(Rotation r);

    void rotateRight();

    void rotateLeft();

    void setPosition(int row, int col);

    void mirrorInvert();

    void randomRotate();

    void randomInvert();

}
