package hr.algebra.java2.liars.dice.marina_spudic_java2.utils;

import javafx.scene.control.Alert;

public class AlertUtils {

    public static void displayVictoryDialog(String playerName){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("They really bluffed!");
        alert.setContentText("Player " + playerName + " wins!");
        alert.showAndWait();
    }

    public static void displayDefeatDialog(String playerName){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("It seems like they didn't lie.");
        alert.setContentText("Player " + playerName + " wins!");
        alert.showAndWait();
    }

    public static void displayWarningDialog(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
