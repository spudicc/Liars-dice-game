package hr.algebra.java2.liars.dice.marina_spudic_java2.model;

import java.io.Serializable;
import java.util.Map;

public class GameMetaData implements Serializable {
    private PlayerData playerOneData;
    private PlayerData playerTwoData;

    //private int lastNumberBid;

    //private int lastDiceBid;

    private Map<Integer, Integer> numberOfDices;

    // iz ove mape mogu iscitavati lastnumberbid i lastdice bid, ne trebaju mi gore zakomentirani propsi
    private Map<Integer, Integer> gameMoves;

    private boolean playerOneTurn;
    private boolean gameReady;

    public GameMetaData() {
        playerOneTurn = true;
    }

    public void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

    public GameMetaData(boolean gameReady) {
        playerOneTurn = true;
        this.gameReady = gameReady;
    }

    public GameMetaData(PlayerData playerOneData, PlayerData playerTwoData) {
        this.playerOneData = playerOneData;
        this.playerTwoData = playerTwoData;
    }

    @Override
    public String toString() {
        return "Gamemetadata Player one " + playerOneTurn;
    }

    public boolean isGameReady() {
        return gameReady;
    }

    public void setGameReady(boolean gameReady) {
        this.gameReady = gameReady;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public PlayerData getPlayerOneData() {
        return playerOneData;
    }

    public void setPlayerOneData(PlayerData playerOneData) {
        this.playerOneData = playerOneData;
    }

    public PlayerData getPlayerTwoData() {
        return playerTwoData;
    }

    public void setPlayerTwoData(PlayerData playerTwoData) {
        this.playerTwoData = playerTwoData;
    }

    public Map<Integer, Integer> getNumberOfDices() {
        return numberOfDices;
    }

    public void setNumberOfDices(Map<Integer, Integer> numberOfDices) {
        this.numberOfDices = numberOfDices;
    }

    public Map<Integer, Integer> getGameMoves() {
        return gameMoves;
    }

    public void setGameMoves(Map<Integer, Integer> gameMoves) {
        this.gameMoves = gameMoves;
    }
}
