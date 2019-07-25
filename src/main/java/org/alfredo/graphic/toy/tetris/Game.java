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

/**
 * Class that encapsulates top Game logic
 */
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
    private double timeTextStart = 0;

    private Map<State, Double> timeByState = new HashMap<>();


    /* Processing PApplet that renders all graphics */
    private static PApplet P = TetrisPApplet.getInstance();

    /* App config */
    private static AppConfig config = AppConfig.getInstance();

    /* instance of this class */
    private static Game instance = null;

    /**
     * Setup the Game
     * Ideally called from Processing PApplet setup method
     */
    public void gameSetup() {
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

        state = State.INITIAL_TEXT;
    }



    private BaseComposableFigure createRandomFigure() {

        int totalFigures = 5;

        // select figure randomly also select initial col randomly
        Random r = new Random();
        int figNum = r.nextInt(totalFigures);
        int col = r.nextInt(config.NUM_COLS);

        BaseComposableFigure fig = null;

        if (figNum == 0)
            fig = new ComposableBox(0, col);
        else if (figNum == 1)
            fig = new ComposableI(0, col);
        else if (figNum == 2)
            fig = new ComposableL(0, col);
        else if (figNum == 3)
            fig = new ComposableS(0, col);
        else if (figNum == 4)
            fig = new ComposableT(0, col);
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

    public State showInitialText(State state) {
        if (state != State.INITIAL_TEXT) return state;

        // TODO: implement a text box with some info about the game

        if (timeTextStart == 0) timeTextStart = currentTimeSeconds();

        double curTime = currentTimeSeconds();
        if (curTime - timeTextStart < config.TIME_SHOW_TEXT) {

            // TODO: move Text drawing to separate class

            P.rectMode(P.CORNER);
            P.textSize(30);  // TODO: Text size to config param
            P.fill(0, 102, 173);
            int mar = 50;  // margin between background border and text
            P.text(config.TEXT_WELCOME, grid.x0 + mar, grid.y0 + mar, grid.width - 2*mar, grid.height/2 - 2*mar);  // Text wraps within text box

            P.textSize(22);  // TODO: Text size to config param
            P.fill(0, 102, 153);
            P.text(config.TEXT_AUTHOR, grid.x0 + mar, grid.y0 + grid.height/2 + mar, grid.width - 2*mar, grid.height/2 - 2*mar);  // Text wraps within text box

            return State.INITIAL_TEXT;
        }

        return State.BEFORE_FIGURE;
    }

    public State showGameOverText(State state) {
        if (state != State.GAME_OVER) return state;

        // FIXME: Text is drawn behind the squares, so it cant be seen.

        P.rectMode(P.CORNER);
        P.textSize(32);  // TODO: Text size to config param
        P.fill(0, 102, 173);
        int mar = 50;  // margin between background border and text
        P.text(config.TEXT_GAMEOVER, grid.x0 + mar, grid.y0 + mar, grid.width - 2*mar, grid.height/2 - 2*mar);  // Text wraps within text box

        return State.GAME_OVER;
    }

    public State cleanAfterFigure(State state) {
        if (state != State.AFTER_FIGURE) return state;
        // clearInteractionTrackingVars();
        return State.BEFORE_FIGURE;
    }

    /**
     * Create a new figure positioned on top (row=0, random col).
     * Randomly picks the type of figure to create, color,
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

            // TODO: check for end of game condition
            // 1. check if it's possible to position this figure in the floor (collisions?)
            // 2. If this is collision, try to generate N more figures and try again
            // 3. If after N attempts we cannot place the figure then GAME OVER

            BaseComposableFigure fig = null;

            for (int i=0; i<10; i++) {
                fig = createRandomFigure();
                if (floor.canFit(fig)) break;
                else fig = null;
            }

            if (fig == null) return State.GAME_OVER;

            figures.add(fig);
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
                // alternative way to Move (work around slow events)
                updateCurrentMoveArrowPressed();

                // check floor, do not perform move if collision
                if (floor.canMove(f, curMove))
                    f.move(curMove);
            }

            // mirror invert figure: test only
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

        return State.AFTER_FIGURE;
    }

    public void gameLogic() {
        state = showInitialText(state);
        state = initBeforeFigure(state);
        state = spawnNewFigure(state);
        state = respondToUserInput(state);
        state = advanceFigure(state);
        state = animateFallToFloor(state);
        state = mergeFigureToFloorASAP(state);
        state = cleanAfterFigure(state);
        state = showGameOverText(state);
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

        // TODO: rename method to game_loop and move the call to setBackground inside here

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

    private void updateCurrentMoveArrowPressed() {
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

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

}
