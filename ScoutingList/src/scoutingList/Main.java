package scoutingList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scoutingList.classes.DataAccount;

public class Main extends Application {

    private final DataAccount dataAccount = DataAccount.getDataInstance();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/loginWindow.fxml"));
        primaryStage.setTitle("Log in / Register");
        primaryStage.setScene(new Scene(root, 680, 420));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        dataAccount.saveAccountsToFile();
    }

    @Override
    public void init() throws Exception {
        dataAccount.createAccountsFile();
        dataAccount.loadAccountsFromFile();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
