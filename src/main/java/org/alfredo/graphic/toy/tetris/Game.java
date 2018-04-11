package org.alfredo.graphic.toy.tetris;

import org.alfredo.graphic.toy.tetris.constants.Move;
import org.alfredo.graphic.toy.tetris.constants.Rotation;
import org.alfredo.graphic.toy.tetris.constants.State;
import org.alfredo.graphic.toy.tetris.errors.IrrecoverableError;
import org.alfredo.graphic.toy.tetris.figures.*;
import org.alfredo.graphic.toy.tetris.figures.floor.Floor;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Game implements Displayable {

    private Move curMove;
    private Rotation curRotation;
    private int curInversion;
    private int curFall;

    private State state;

    /* Game objects */
    private TetrisBackground tBackground;
    private Grid grid;
    private Score score;
//    private DisplayableFigure cl;
    private BaseComposableFigure cl;


//    private ArrayList<DisplayableFigure> figures;
    private ArrayList<BaseComposableFigure> figures;

    private Floor floor;

    private double timeLastAdvance = currentTimeSeconds();
    private double timeLastArrowPressed = 0;

    private Map<State, Double> timeByState = new HashMap<>();


    /* Processing PApplet that renders all graphics */
    private static PApplet P = TetrisPApplet.getInstance();

    /* App config */
    private static AppConfig config = AppConfig.getInstance();

    public Game() {

        curMove = Move.NONE;
        curRotation = Rotation.NONE;
        curInversion = 0;
        curFall = 0;

        // create game objects
        tBackground = new TetrisBackground();

        grid = new Grid(
                config.BORDER_LEFT_WIDTH,
                config.BORDER_TOP_HEIGHT,
                config.SCREEN_WIDTH - config.BORDER_RIGHT_WIDTH,
                config.SCREEN_HEIGHT - config.BORDER_BOTTOM_HEIGHT,
                config.NUM_ROWS,
                config.NUM_COLS
        );

        score = new Score();

//        figures = new ArrayList<DisplayableFigure>(16);
        figures = new ArrayList<BaseComposableFigure>(16);

        floor = new Floor();

        state = State.BEFORE_FIGURE;

    }


    private BaseComposableFigure createRandomFigure() {

        int totalFigures = 5;

        // select figure randomly also select initial col randomly
        Random r = new Random();
        int figNum = r.nextInt(totalFigures);
        int col = r.nextInt(config.NUM_COLS);

        BaseComposableFigure fig = null;

        if (figNum == 0)
            fig = new ComposableBox(this, 0, col);
        else if (figNum == 1)
            fig = new ComposableI(this, 0, col);
        else if (figNum == 2)
            fig = new ComposableL(this, 0, col);
        else if (figNum == 3)
            fig = new ComposableS(this, 0, col);
        else if (figNum == 4)
            fig = new ComposableT(this, 0, col);
        else
            throw new IrrecoverableError("Some figure missing in random creation logic");

        // apply random rotation and inversion to generate more variety
        fig.randomInvert();
        fig.randomRotate();

        // apply random color
        int color = config.FIGURE_COLORS[r.nextInt(config.FIGURE_COLORS.length)];
        fig.setColor(color);

        return fig;
    }

    public State initBeforeFigure(State state) {
        if (state != State.BEFORE_FIGURE) return state;

        clearInteractionTrackingVars();

        // TODO: clear the timeByState key State.ADVANCING_TO_BOTTOM
        timeByState.remove(State.CREATE_FIGURE);

        return State.CREATE_FIGURE;
    }

    public State cleanAfterFigure(State state) {
        if (state != State.FINAL_STATE) return state;
        // clearInteractionTrackingVars();
        return State.BEFORE_FIGURE;
    }

    /**
     * Create a new figure positioned on top (row=0, random col).
     * Should randomly pick the type of figure to create, color,
     * number of rotations and inversions to apply to it.
     *
     * @return Next game state
     */
    public State spawnNewFigure(State state) {
        if (state != State.CREATE_FIGURE) return state;

        // apply a delay before creating a figure
        if (timeByState.get(state) == null) {
            timeByState.put(state, currentTimeSeconds());
            return State.CREATE_FIGURE;
        }
        if (currentTimeSeconds() - timeByState.get(state) < 1.0) {
            return State.CREATE_FIGURE;
        }

        if (figures.isEmpty()) {
            figures.add(createRandomFigure());
        }

        clearInteractionTrackingVars();
        timeByState.remove(state);

        return State.ADVANCING_TO_BOTTOM;
    }

    /**
     * Advance active figure one step down on periodical intervals
     *
     * @return Next game state
     */
    public State advanceFigure(State state) {

        if (state != State.ADVANCING_TO_BOTTOM) return state;

        double curTime = currentTimeSeconds();
        if (curTime - timeLastAdvance > getPeriodicalInterval()) {

            if (!floor.canMoveDown(figures.get(0))) {
                return State.MERGE_TO_FLOOR;
            }

            addMove(Move.DOWN);
            timeLastAdvance = curTime;
        }
        return State.ADVANCING_TO_BOTTOM;
    }


    public State respondToUserInput(State state) {

        if (state != State.ADVANCING_TO_BOTTOM) return state;

        for (BaseComposableFigure f: figures) {

            // rotate figure
            if (curRotation != Rotation.NONE) {
                f.rotate(curRotation);
                if (!floor.canFit(f)) {
                    Rotation back = (curRotation == Rotation.LEFT) ? Rotation.RIGHT : Rotation.LEFT;
                    f.rotate(back);  // reverse rotation
                }
            }

            // move figure
            if (f.isMovable()) {
                // work around the long delay for repeated keys events
                double curTime = currentTimeSeconds();
                if (curMove == Move.NONE) {
                    if (curTime - timeLastArrowPressed >= config.TIME_ARROW_REPEAT) {
                        if (isLeftKeyPressed()) curMove = Move.LEFT;
                        if (isRightKeyPressed()) curMove = Move.RIGHT;
                        if (curMove != Move.NONE) timeLastArrowPressed = curTime;
                    }
                }
                else timeLastArrowPressed = curTime;
                // check floor, do not perform move if collision
                if (floor.canMove(f, curMove))
                    f.move(curMove);
            }

            // mirror invert figure: test only
            // TODO: reverse inversion if inverted figure does not fit in floor
            if (curInversion == 1) {
                f.mirrorInvert();
            }

            if (curFall == 1) {
                // trigger animation of figure fall to floor
                state = State.FALLING_TO_FLOOR;
            }

        }

        clearInteractionTrackingVars();

        return state;
    }

    public State animateFallToFloor(State state) {
        if (state != State.FALLING_TO_FLOOR) return state;

        // calculate # rows to descend to keep up with speed of animation
        int numMoves = (int) (config.NUM_ROWS / (config.TIME_FALL_ANIMATION * config.FRAME_RATE) + 1);

        for (BaseComposableFigure f: figures) {
            for (int i=0; i<numMoves; i++) {
                if (floor.canMove(f, Move.DOWN)) {
                    f.move(Move.DOWN);
                }
                else return State.MERGE_TO_FLOOR;
            }
        }
        return State.FALLING_TO_FLOOR;
    }

    public State mergeFigureToFloorASAP(State state) {

        if (state != State.MERGE_TO_FLOOR) return state;

        // TODO: stay in state==2 until floor is done
        // useful if we decide to animate the fading full lines
        if (!floor.canMoveDown(figures.get(0))) {
            floor.assimilateFigure(figures.get(0));
            figures.remove(0);
            floor.removeFullLines();
        }

        return State.FINAL_STATE;
    }

//    public int periodicReport(int state) {
//        // TODO: report score every 30 seconds
//        if (state != 2) return state;
//    }


    public void gameLogic() {
        state = initBeforeFigure(state);
        state = spawnNewFigure(state);
        state = respondToUserInput(state);
        state = advanceFigure(state);
        state = animateFallToFloor(state);
        state = mergeFigureToFloorASAP(state);
        state = cleanAfterFigure(state);
    }


    public void clearInteractionTrackingVars() {
        curMove = Move.NONE;
        curRotation = Rotation.NONE;
        curInversion = 0;
        curFall = 0;
    }

    /**
     * Returns the time in seconds that the game waits before advancing the figure
     * towards the bottom. This will change getting shorter as the game advances,
     * as a way to increase the difficulty of the game.
     *
     * @return
     */
    public double getPeriodicalInterval() {
        // TODO: adjust dynamically as game advances
        int level = score.currentGameLevel();
        return config.TIME_ADVANCE_TO_BOTTOM / level;
    }

    public static double currentTimeSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    public void addMove(Move m) {
        curMove = m;
    }

    public void addRotation(Rotation r) {
        System.out.println("adding rotation");
        curRotation = r;
    }

    public void addMirrorInversion() {
        System.out.println("adding mirror inversion");
        curInversion = 1;
    }

    public void addFallToFloor() {
        System.out.println("adding fall to floor");
        curFall = 1;
    }

    public void setBackground() {
        P.background(config.BACKGROUND_COLOR);
        P.stroke(255);
        P.noFill();

        tBackground.display();
    }

    public void display() {

        gameLogic();

        // paint movable figures (there is only one in this game)
        for (DisplayableFigure f: figures) {
            f.display();
        }

        // paint floor lines
        floor.display();

    }


    private boolean isLeftKeyPressed() {
        if (P.keyPressed) {
            return ((P.key == P.CODED) && (P.keyCode == P.LEFT));
        }
        return false;
    }

    private boolean isRightKeyPressed() {
        if (P.keyPressed) {
            return ((P.key == P.CODED) && (P.keyCode == P.RIGHT));
        }
        return false;
    }

    /* Events */

    public void keyPressed() {
        if ((P.key == P.CODED) && (P.keyCode == P.LEFT)) {
            addMove(Move.LEFT);
        }
        else if ((P.key == P.CODED) && (P.keyCode == P.RIGHT)) {
            addMove(Move.RIGHT);
        }
        else if ((P.key == P.CODED) && (P.keyCode == P.UP)) {
            addRotation(Rotation.RIGHT);
        }
        else if ((P.key == P.CODED) && (P.keyCode == P.DOWN)) {
            // TODO: trigger animation figure move to floor
            //addMove(Move.DOWN);
            addFallToFloor();
        }
        else if ((P.key == 'O') || (P.key == 'o')) {
            addRotation(Rotation.LEFT);
        }
        else if ((P.key == 'P') || (P.key == 'p')) {
            addRotation(Rotation.RIGHT);
        }
        else if ((P.key == 'I') || (P.key == 'i')) {
            addMirrorInversion();
        }
    }

    public Grid getGrid() {
        return grid;
    }

    public Score getScore() {
        return score;
    }

}
