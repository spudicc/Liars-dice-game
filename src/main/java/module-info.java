module hr.algebra.java2.liars.dice.marina_spudic_java2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.naming;
    requires java.xml;

    opens hr.algebra.java2.liars.dice.marina_spudic_java2 to javafx.fxml;
    exports hr.algebra.java2.liars.dice.marina_spudic_java2.rmi;
    exports hr.algebra.java2.liars.dice.marina_spudic_java2;
    exports hr.algebra.java2.liars.dice.marina_spudic_java2.utils;
    opens hr.algebra.java2.liars.dice.marina_spudic_java2.utils to javafx.fxml;
}