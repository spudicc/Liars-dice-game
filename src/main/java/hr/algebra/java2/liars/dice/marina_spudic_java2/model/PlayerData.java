package hr.algebra.java2.liars.dice.marina_spudic_java2.model;

import java.io.Serializable;
import java.util.List;

public class PlayerData implements Serializable {
    private String playerName;
    private long playerId;
    private List<Integer> playerDices;

    private int playerVictories;

    public PlayerData(String playerName) {
        this.playerId = ProcessHandle.current().pid();
        this.playerVictories = 0;
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getPlayerId() {
        return playerId;
    }
    public List<Integer> getPlayerDices() {
        return playerDices;
    }

    public void setPlayerDices(List<Integer> playerDices) {
        this.playerDices = playerDices;
    }

    public int getPlayerVictories() {
        return playerVictories;
    }

    public void recordNewVictory(){
            playerVictories++;
    }

    @Override
    public String toString() {
        return playerId + " - " + playerName;
    }
}
