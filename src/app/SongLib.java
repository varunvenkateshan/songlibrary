/**
 * Created by Varun Venkateshan and Yashwant Balaji
 * */

package app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import view.Controller;

public class SongLib extends Application {

    Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("Song Library");

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/SongLib.fxml"));
            AnchorPane pane = loader.load();

            Controller controller = loader.getController();
            controller.start(mainStage);

            Scene scene = new Scene(pane, 700, 500);
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Main
    public static void main(String[] args) {

        launch(args);
    }
}