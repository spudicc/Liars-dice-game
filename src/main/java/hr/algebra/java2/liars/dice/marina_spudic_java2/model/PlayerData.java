package hr.algebra.java2.liars.dice.marina_spudic_java2.model;

import java.io.Serializable;
import java.util.List;

public class PlayerData implements Serializable {
    private String playerName;
    private long playerId;
    private List<Integer> playerDices;

    public PlayerData(String playerName) {
        this.playerId = ProcessHandle.current().pid();
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
}
