package hr.algebra.java2.liars.dice.marina_spudic_java2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloController helloController = (HelloController) fxmlLoader.getController();
        stage.setTitle("Liar's dice");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getMainStage(){
        return mainStage;
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}