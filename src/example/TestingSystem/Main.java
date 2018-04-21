package example.TestingSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{ //Запуск окна
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/TestingSystem.fxml"));
        primaryStage.setTitle("Testing System");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
//        Controller c = new Controller();
//        c.initialize();
    }
}
