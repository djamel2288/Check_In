package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.db.db_check_in;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.   load(getClass().getResource("form.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../itc_style.css").toExternalForm());
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) throws ClassNotFoundException {
        db_check_in.connect();
        //db_check_in.selectAll();
        launch(args);
    }
}