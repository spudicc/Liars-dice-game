package hr.algebra.java2.liars.dice.marina_spudic_java2.model;

public class PlayerInfo {
    private String playerName;
    private Integer playerVictories;

    public PlayerInfo(String playerName) {
        this.playerName = playerName;
        playerVictories = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void recordNewVictory()
    {
        playerVictories++;
    }

    public Integer getPlayerVictories() {
        return playerVictories;
    }
}
