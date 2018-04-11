package org.alfredo.graphic.toy.tetris.figures.floor;

import org.alfredo.graphic.toy.tetris.*;
import org.alfredo.graphic.toy.tetris.constants.Move;
import org.alfredo.graphic.toy.tetris.figures.BaseComposableFigure;
import org.alfredo.graphic.toy.tetris.squares.BaseSquare;
import processing.core.PApplet;

import java.util.*;

/**
 * An ordered collection of ComposableFloorLines
 * It implements the List interface instead of directly extending ArrayList
 * This way is more verbose, but apparently Java guys dislike extending
 * collections directly and they favor using a decorator pattern.
 * Implementation based on previous work by Jenifer Lopez, see "On the Floor".
 */
public class Floor implements List<ComposableFloorLine>, Displayable {

    public List<ComposableFloorLine> floorLines;

    /* Processing PApplet that renders all graphics */
    protected static TetrisPApplet P = TetrisPApplet.getInstance();

    /* App config */
    protected static AppConfig config = AppConfig.getInstance();

    public Floor() {
        super();
        floorLines = new ArrayList<ComposableFloorLine>();
    }

    public Floor(List<ComposableFloorLine> floorLines) {
        super();
        this.floorLines = floorLines;
    }

    /**
     * Return the grid's row index of the top line of the floor
     *
     * @return index of the top floor line
     */
    public int indexTopRow() {
        return config.NUM_ROWS - floorLines.size();
    }


    /**
     * Return the n last added floor lines in a list.
     * The list is ordered from older to newer (which means
     * inverting the order of the last n lines in our list).
     *
     * @param n number of rows to return
     * @return last rows starting at the bottom of the figure
     */
    public List<ComposableFloorLine> lastLines(int n) {

        List<ComposableFloorLine> lines = new ArrayList<>();

        for (int i = floorLines.size()-1; i >= Math.max(0, floorLines.size()-n); i--) {
            lines.add(floorLines.get(i));
        }
        Collections.reverse(lines);
        return lines;
    }


    /* Specialized methods of the Floor */

    /**
     * Count how many rows inside the floor area a given figure is.
     * Meaning it calculates the depth given the position of a figure.
     *
     * Result is 0 if the figure's bottom is located on the row immediately
     * before floor's top row.
     * If the figure's bottom is inside the floor's top, the result is a
     * positive count of how many rows inside it is.
     * If the figure's bottom is several rows above the floor's top,
     * the result is a negative count of how many rows away it is.
     *
     * @param figure A composable figure object
     * @return current depth this figure is inside the floor area
     */
    public int currentDepth(BaseComposableFigure figure) {
        return figure.indexBottomRow() - this.indexTopRow() + 1;
    }

    /**
     * Calculate the figure's top row index on the game grid
     * if said figure were at a given a depth inside the floor.
     * Meaning it calculates the position of a figure given its depth,
     * which is the inverse of currentDepth.
     *
     * @param depth a given depth for a figure inside floor
     * @param figure A composable figure object
     * @return top row index (vertical position) of figure at given depth
     */
    public int depthToRowIndex(int depth, BaseComposableFigure figure) {
        return this.indexTopRow() + depth - figure.figRows();
    }

    /**
     * Calculate the maximum depth a figure can sink in the floor,
     * that is, how many steps down can it be pushed into the floor
     * area without colliding with one of the squares in the floor.
     * Depth 0 means the bottom row of the figure is in the row
     * immediately before the floor top row. Depth 1 means the bottom
     * of the figure is have entered 1 step into the floor area, and
     * so on.
     *
     * @param figure figure to be sunk in the floor
     * @return number of steps a figure can sink into the floor
     */
    public int maxDockDepth(BaseComposableFigure figure) {

        for (int depth=1; depth<=floorLines.size(); depth++) {
            if (!canDockAtDepth(figure, depth)) return depth - 1;
        }
        return floorLines.size();
    }

    private boolean canDockAtDepth(BaseComposableFigure figure, int depth) {

        if (depth <= 0) return true;
        if (depth > floorLines.size()) return false;

        List<ComposableFloorLine> lines = lastLines(depth);
        List<BaseSquare[]> figRows = figure.lastRows(depth);

        int numCompares = Math.min(lines.size(), figRows.size());

        for (int i = 0; i < numCompares; i++) {
            for (BaseSquare square : figRows.get(i)) {
                if (!lines.get(i).canAddSquare(square)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canDockAtDepth(BaseComposableFigure figure, int depth, int insertCol) {

        if (depth <= 0) return true;
        if (depth > floorLines.size()) return false;
        if ((insertCol < 0) || (insertCol > config.NUM_COLS - figure.figCols())) return false;

        List<ComposableFloorLine> lines = lastLines(depth);
        List<BaseSquare[]> figRows = figure.lastRows(depth);

        int numCompares = Math.min(lines.size(), figRows.size());

        for (int i = 0; i < numCompares; i++) {
            int arrIndex = 0;
            for (BaseSquare square : figRows.get(i)) {
                if ((square != null) && !lines.get(i).canAddSquare(insertCol + arrIndex)) {
                    return false;
                }
                ++arrIndex;
            }
        }
        return true;
    }

    /**
     * Checks whether the given figure fits in the floor
     * in its current position without any collision
     *
     * @param figure figure to check against floor
     * @return true if the figure fits in this floor
     */
    public boolean canFit(BaseComposableFigure figure) {
        int depth = currentDepth(figure);
        return canDockAtDepth(figure, depth);
    }

    /**
     * Checks if a composable figure can advance one step down
     * without colliding with the Floor
     *
     * @param figure composable figure to check
     * @return true if figure can move down without collision
     */
    public boolean canMoveDown(BaseComposableFigure figure) {
        int depth = currentDepth(figure);
        return canDockAtDepth(figure,depth + 1);
    }

    public boolean canMoveUp(BaseComposableFigure figure) {
        int depth = currentDepth(figure);
        return canDockAtDepth(figure,depth - 1);
    }

    public boolean canMoveLeft(BaseComposableFigure figure) {
        int depth = currentDepth(figure);
        return canDockAtDepth(figure, depth,figure.col-1);
    }

    public boolean canMoveRight(BaseComposableFigure figure) {
        int depth = currentDepth(figure);
        return canDockAtDepth(figure, depth,figure.col+1);
    }

    public boolean canMove(BaseComposableFigure figure, Move m) {
        // check figure can be moved inside floor without collision
        if      (m == Move.UP)    return canMoveUp(figure);
        else if (m == Move.DOWN)  return canMoveDown(figure);
        else if (m == Move.LEFT)  return canMoveLeft(figure);
        else return canMoveRight(figure);
    }


    /**
     * Incorporates the squares of a figure into the Floor, adding
     * new floor lines if necessary.
     *
     * @param figure A composable figure to assimilate into the Floor
     */
    public void assimilateFigure(BaseComposableFigure figure) {
        assimilateFigure(figure, false);
    }

    /**
     * Incorporates the squares of a figure into the Floor, adding
     * new floor lines if necessary. Optionally, before assimilation
     * it can move the figure to maximize its fit in the floor.
     *
     * @param figure A composable figure to assimilate into the Floor
     * @param maximizeDepth set true to force the move of the figure to its
     *                      maximal possible depth before assimilation, otherwise
     *                      the figure will be assimilated in its current position.
     */
    public void assimilateFigure(BaseComposableFigure figure, boolean maximizeDepth) {

        if (maximizeDepth) {
            int maxDepth = maxDockDepth(figure);
            int topRow = depthToRowIndex(maxDepth, figure);
            figure.setPosition(topRow, figure.col);
        }

        // If figure top is above floor create new floor lines
        int floorTopRow = indexTopRow();
        int numNewRows = floorTopRow - figure.row;
        if (numNewRows > 0) {
            for (int i=0; i < numNewRows; i++)
                this.add(
                        new ComposableFloorLine(
                                TetrisPApplet.getGame(),
                                floorTopRow - i - 1,
                                0
                        )
                );
        }

        // merge each figure's squares into the corresponding floor line
        int depth = currentDepth(figure);
        List<ComposableFloorLine> lines = lastLines(depth);
        List<BaseSquare[]> figRows = figure.lastRows(depth);
        int numCompares = Math.min(lines.size(), figRows.size());
        for (int i = 0; i < numCompares; i++) {
            for (BaseSquare square : figRows.get(i)) {
                lines.get(i).addSquare(square);
            }
        }

    }


    public int numEmptyLines() {
        int numEmpty = 0;
        for (ComposableFloorLine fl: floorLines) {
            if (fl.isEmpty()) numEmpty++;
        }
        return numEmpty;
    }

    public int numFullLines() {
        int numFull = 0;
        for (ComposableFloorLine fl: floorLines) {
            if (fl.isFull()) numFull++;
        }
        return numFull;
    }


    // remove empty lines

    public void removeEmptyLines() {
        int n = floorLines.size();
        //ComposableFloorLine[] floorLinesArr = floorLines.toArray(new ComposableFloorLine[n]);

        for (int i=n-1; i>=0; i--) {
            if (floorLines.get(i).isEmpty()) {
                floorLines.remove(i);
            }
        }
    }

    public boolean removeFirstEmptyLine() {
        int n = floorLines.size();
        int i = 0;

        while (i < n) {
            if (floorLines.get(i).isEmpty()) {
                floorLines.remove(i);
                return true;
            }
        }
        return false;
    }

    // remove all full lines

    public void removeFullLines() {
        Score score = TetrisPApplet.getGame().getScore();
        int n = floorLines.size();

        for (int i=n-1; i>=0; i--) {
            if (floorLines.get(i).isFull()) {
                floorLines.remove(i);
            }
        }

        if (n - floorLines.size() > 0) {
            // reposition the lines
            int index = 0;
            for (ComposableFloorLine line: floorLines) {
                line.setPosition(config.NUM_ROWS - ++index, 0);
            }
            // update score
            score.addToDestroyed(n - floorLines.size());
        }

    }

//    public void removeLineAt()


    @Override
    public void display() {
        for (DisplayableFigure line: this.floorLines) {
            line.display();  // paint floor line
        }
    }


    /* Boring methods to proxy calls to floorlines field */

    @Override
    public int size() {
        return floorLines.size();
    }

    @Override
    public boolean isEmpty() {
        return floorLines.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return floorLines.contains(o);
    }

    @Override
    public Iterator<ComposableFloorLine> iterator() {
        return floorLines.iterator();
    }

    @Override
    public Object[] toArray() {
        return floorLines.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return floorLines.toArray(a);
    }

    @Override
    public boolean add(ComposableFloorLine composableFloorLine) {
        return floorLines.add(composableFloorLine);
    }

    @Override
    public boolean remove(Object o) {
        return floorLines.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return floorLines.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends ComposableFloorLine> c) {
        return floorLines.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends ComposableFloorLine> c) {
        return floorLines.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return floorLines.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return floorLines.retainAll(c);
    }

    @Override
    public void clear() {
        floorLines.clear();
    }

    @Override
    public ComposableFloorLine get(int index) {
        return floorLines.get(index);
    }

    @Override
    public ComposableFloorLine set(int index, ComposableFloorLine element) {
        return floorLines.set(index, element);
    }

    @Override
    public void add(int index, ComposableFloorLine element) {
        floorLines.add(index, element);
    }

    @Override
    public ComposableFloorLine remove(int index) {
        return floorLines.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return floorLines.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return floorLines.lastIndexOf(o);
    }

    @Override
    public ListIterator<ComposableFloorLine> listIterator() {
        return floorLines.listIterator();
    }

    @Override
    public ListIterator<ComposableFloorLine> listIterator(int index) {
        return floorLines.listIterator(index);
    }

    @Override
    public List<ComposableFloorLine> subList(int fromIndex, int toIndex) {
        return floorLines.subList(fromIndex, toIndex);
    }

}
