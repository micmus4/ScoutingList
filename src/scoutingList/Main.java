package scoutingList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scoutingList.classes.DataAccount;

public class Main extends Application {

    private final DataAccount dataAccount = DataAccount.getDataInstance();



    /**
     * This method opens a new window (Login Window - "fxmls/loginWindow.fxml") which is responsible
     * for logging into account or creating a new account.
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/loginWindow.fxml"));
        primaryStage.setTitle("Log in / Register");
        primaryStage.setScene(new Scene(root, 680, 420));
        primaryStage.show();
    }



    /**
     * This method calls <code>DataAccount.saveAccountsToFile()</code> which is responsible saving all accounts to file.
     * Therefore, every single new account made will be saved and can be accessed next time the program is run.
     *
     * This method is called everytime the program is closed.
     */

    @Override
    public void stop() throws Exception {
        dataAccount.saveAccountsToFile();
    }



    /**
     * This method calls <code>DataAccount.createAccountsFromFile()</code> which is responsible for creating accounts.txt file
     * which stores all data about users' accounts. If this file exists, the method will not do anything.
     * Then this method calls <code>DataAccount.loadAccountsFromFile()</code> which loads all the accounts to the program.
     *
     * This method is called everytime the program is run.
     */

    @Override
    public void init() throws Exception {
        dataAccount.createAccountsFile();
        dataAccount.loadAccountsFromFile();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
