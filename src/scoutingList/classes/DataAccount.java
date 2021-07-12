package scoutingList.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

public class DataAccount {

    private static final DataAccount dataInstance = new DataAccount();
    private final ObservableList<Account> accounts;
    private final HashMap<String, String> loginsAndPasswords;
    private Stage loginStage = null;
    private String loggedUserLogin = "";

    public String getLoggedUserLogin() {
        return loggedUserLogin;
    }

    public void setLoggedUserLogin(String loggedUserLogin) {
        this.loggedUserLogin = loggedUserLogin;
    }

    private DataAccount() {
        this.accounts = FXCollections.observableArrayList();
        this.loginsAndPasswords = new HashMap<>();
    }

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    public Stage getLoginStage() {
        return loginStage;
    }

    public static DataAccount getDataInstance() {
        return dataInstance;
    }

    public ObservableList<Account> getAccounts() {
        return accounts;
    }

    public HashMap<String, String> getLoginsAndPasswords() {
        return loginsAndPasswords;
    }



    /**
     *  This method is responsible for checking if accounts.txt file exists, and if not then it should create that file,
     *  which is responsible for containing all the information about users' accounts.
     */

    public void createAccountsFile(){
        File file = new File("src/scoutingList/data/accounts.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            }
            catch (IOException e){
                System.out.println("Couldn't create file to store accounts' data");
            }
        }
    }



    /**
     * This method is responsible for writing accounts' data into accounts.txt file. A line of accounts.txt file
     * is in format "%s;%s;%s;%s;%s" (where '%s' is String value related to Account). The method stops writing into accounts.txt file
     * when it stops loading data about the last account.
     */

    public void saveAccountsToFile(){
        Path path = Paths.get("src/scoutingList/data/accounts.txt");
        Iterator<Account> iterator = accounts.iterator();
        BufferedWriter writer = null;

        try {
            writer = Files.newBufferedWriter(path);
        } catch (IOException e){
            System.out.println("Error - connection to file wasn't established.");
            e.printStackTrace();
        }

        try {
            while (iterator.hasNext()) {
                Account account = iterator.next();
                try {
                    writer.write(String.format("%s;%s;%s;%s;%s",
                            account.getFirstName(),
                            account.getLastName(),
                            account.getEmailAddress(),
                            account.getLogin(),
                            account.getPassword()
                    ));
                    writer.newLine();
                } catch (IOException e) {
                    System.out.println("Error - couldn't save accounts' data to file..");
                    e.printStackTrace();
                }
            }
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e){
                    System.out.println("Error - connection to file couldn't be closed.");
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * This method is responsible for loading data about accounts from accounts.txt file and then adding them into accounts
     * ObservableArrayList (all data about accounts are stored there) and also it adds every single login and password of user
     * into loginsAndPassword HashMap (login is the key, password is the value). Inside the method, line from accounts.txt is
     * split into an Array (the separator is ';' character) and a Account instance is created out of those values.
     *
     * The method stops loading data when the last line from accounts.txt file is processed.
     */

    public void loadAccountsFromFile(){
        Path path = Paths.get("src/scoutingList/data/accounts.txt");
        String data;
        BufferedReader reader;

        try {
            reader = Files.newBufferedReader(path);
        } catch (IOException e){
            System.out.println("Error - connection to file wasn't established.");
            e.printStackTrace();
            return;
        }

        try {
            while ((data = reader.readLine()) != null){
                String[] dataArray = data.split(";");

                String firstName = dataArray[0];
                String lastName = dataArray[1];
                String emailAddress = dataArray[2];
                String login = dataArray[3];
                String password = dataArray[4];

                accounts.add(new Account(firstName, lastName, emailAddress, login, password));
                loginsAndPasswords.put(login, password);
            }
        } catch (IOException e){
            System.out.println("Error - loading accounts to file failed.");
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e){
                System.out.println("Error - closing connection to file failed.");
                e.printStackTrace();
            }
        }
    }
}
