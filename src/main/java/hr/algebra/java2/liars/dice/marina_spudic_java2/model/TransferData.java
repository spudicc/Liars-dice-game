package hr.algebra.java2.liars.dice.marina_spudic_java2.model;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.io.Serializable;

public class TransferData implements Serializable {
    private Button btnCallBluff;
    private Button btnBidHigher;
    private Label lbOpponentsMove;
    private ChoiceBox cbLastNumberBid;

    public TransferData(Button btnCallBluff, Button btnBidHigher, Label lbOpponentsMove, ChoiceBox cbLastNumberBid) {
        this.btnCallBluff = btnCallBluff;
        this.btnBidHigher = btnBidHigher;
        this.lbOpponentsMove = lbOpponentsMove;
        this.cbLastNumberBid = cbLastNumberBid;
    }

    public Button getBtnCallBluff() {
        return btnCallBluff;
    }

    public Button getBtnBidHigher() {
        return btnBidHigher;
    }

    public Label getLbOpponentsMove() {
        return lbOpponentsMove;
    }

    public ChoiceBox getCbLastNumberBid() {
        return cbLastNumberBid;
    }
}
