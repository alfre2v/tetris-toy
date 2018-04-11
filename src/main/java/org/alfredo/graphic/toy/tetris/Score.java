package org.alfredo.graphic.toy.tetris;

public class Score {

    private int numLinesDestroyed = 0;
    private String playerName = null;

    public Score() {
        numLinesDestroyed = 0;
        playerName = "Unnamed player";
    }

    public Score(String playerName) {
        numLinesDestroyed = 0;
        this.playerName = playerName;
    }

    public void addToDestroyed(int count) {
        numLinesDestroyed += count;
        // TODO: better way to report score
        System.out.println("game score: " + currentScore());
        System.out.println("game level: " + currentGameLevel());
    }

    public int currentScore() {
        return numLinesDestroyed;
    }

    public int currentGameLevel() {
        return 1 + numLinesDestroyed / 2;
    }

}
