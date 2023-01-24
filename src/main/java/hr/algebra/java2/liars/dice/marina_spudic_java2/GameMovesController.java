package hr.algebra.java2.liars.dice.marina_spudic_java2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;

public class GameMovesController {
    @FXML
    private Button btnClose;
    @FXML
    private Label lbGameMoves;

    private List<Integer> numberMoves;
    private List<Integer> diceMoves;

    public void initialize(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.getNumberMoves().size(); i++) {
            stringBuilder.append("MOVE " + " : " + this.getNumberMoves().get(i) + " dices of " + this.getDiceMoves().get(i) + "\n");
        }
        lbGameMoves.setText(String.valueOf(stringBuilder));
    }

    public void close(){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public List<Integer> getNumberMoves() {
        return numberMoves;
    }

    public void setNumberMoves(List<Integer> numberMoves) {
        this.numberMoves = numberMoves;
    }

    public List<Integer> getDiceMoves() {
        return diceMoves;
    }

    public void setDiceMoves(List<Integer> diceMoves) {
        this.diceMoves = diceMoves;
    }
}
