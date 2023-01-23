package hr.algebra.java2.liars.dice.marina_spudic_java2.model;

import java.io.Serializable;
import java.util.*;

public class GameMetaData implements Serializable {
    private PlayerData playerOneData;
    private PlayerData playerTwoData;
    private List<Integer> numberBids;
    private List<Integer> diceBids;

    private Map<Integer, Integer> numberOfDices;

    // iz ove mape mogu iscitavati lastnumberbid i lastdice bid, ne trebaju mi gore zakomentirani propsi
    //private Map<Integer, Integer> gameMoves;

    private boolean playerOneTurn;

    public void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

    public GameMetaData() {
        playerOneTurn = true;
        this.numberBids = new ArrayList<>();
        this.diceBids = new ArrayList<>();
        this.numberOfDices = new HashMap<>();
        this.numberOfDices.put(1, 0);
        this.numberOfDices.put(2, 0);
        this.numberOfDices.put(3, 0);
        this.numberOfDices.put(4, 0);
        this.numberOfDices.put(5, 0);
        this.numberOfDices.put(6, 0);
    }

    @Override
    public String toString() {
        return "Gamemetadata Player one " + playerOneTurn + playerOneData + " second: " + playerTwoData;
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
        int diceCounter = 0;
        for (Map.Entry<Integer, Integer> entry:
                this.numberOfDices.entrySet()) {
            diceCounter += entry.getValue();
        }
        if (diceCounter < 10)
        {
            for (Map.Entry<Integer, Integer> newEntry: numberOfDices.entrySet()) {
                this.numberOfDices.put(newEntry.getKey(), (this.numberOfDices.get(newEntry.getKey()) + newEntry.getValue()));
            }
        }
    }

    public List<Integer> getNumberBids() {
        return numberBids;
    }

    public void setNumberBids(int numberBid) {
        this.numberBids.add(numberBid);
    }

    public List<Integer> getDiceBids() {
        return diceBids;
    }

    public void setDiceBids(int diceBid) {
        this.diceBids.add(diceBid);
    }
}
