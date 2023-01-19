package hr.algebra.java2.liars.dice.marina_spudic_java2.model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class SerializableDice extends ImageView implements Serializable {
    public String dice;

    public SerializableDice(String dice) {
        this.dice = dice;
    }

    @Override
    public String toString() {
        return dice;
    }
}
