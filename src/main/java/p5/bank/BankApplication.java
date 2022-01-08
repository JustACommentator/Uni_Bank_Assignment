package p5.bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

public class BankApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainview.fxml"));
        Parent application = loader.load();
        stage.setTitle("Bankverwaltunssystem FH-Aachen");
        stage.setScene(new Scene(application));
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}