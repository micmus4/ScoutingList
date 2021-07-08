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
