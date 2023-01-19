package hr.algebra.java2.liars.dice.marina_spudic_java2;

import hr.algebra.java2.liars.dice.marina_spudic_java2.model.Client;
import hr.algebra.java2.liars.dice.marina_spudic_java2.model.PlayerData;
import hr.algebra.java2.liars.dice.marina_spudic_java2.model.PlayerInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class HelloController {
    @FXML
    private TextField tfPlayerName;
    @FXML
    private Button btnStartNewGame;
    private static PlayerInfo playerInfo;

    private String playerId;

    public void initialize(){

    }

    public void startGame() throws IOException {
        Client client = new Client(new PlayerData(tfPlayerName.getText().trim()));

        playerInfo = new PlayerInfo(tfPlayerName.getText());
        GameScreenController.client = client;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        HelloApplication.getMainStage().setTitle("Liar's dice");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();


        System.out.println("Alan dautovic napravi mi male dautovice veceras pls");
    }

    public static PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerId(String playerId) {this.playerId = playerId;}

}