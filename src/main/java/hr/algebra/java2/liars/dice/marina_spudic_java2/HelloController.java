package hr.algebra.java2.liars.dice.marina_spudic_java2;

import hr.algebra.java2.liars.dice.marina_spudic_java2.model.Client;
import hr.algebra.java2.liars.dice.marina_spudic_java2.model.PlayerData;
import hr.algebra.java2.liars.dice.marina_spudic_java2.utils.AlertUtils;
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

    private static String playerName;

    public void startGame() throws IOException {
        if (tfPlayerName.getText() == null){
            AlertUtils.displayWarningDialog("You must provide a player name!");
        }

        Client client = new Client(new PlayerData(tfPlayerName.getText().trim()));

        playerName = tfPlayerName.getText().trim();

        GameScreenController.client = client;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        HelloApplication.getMainStage().setTitle("Liar's dice");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public static String getPlayerName() {
        return playerName;
    }
}