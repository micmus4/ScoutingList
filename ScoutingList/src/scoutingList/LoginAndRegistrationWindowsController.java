package scoutingList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scoutingList.classes.Account;
import scoutingList.classes.DataAccount;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class LoginAndRegistrationWindowsController {

    private final DataAccount dataAccount = DataAccount.getDataInstance();

    @FXML
    private PasswordField registrationPasswordField, registrationPasswordField_CONFIRMATION, passwordField;

    @FXML
    private TextField firstNameField, lastNameField, emailAddressField, loginField;

    @FXML
    private Button confirmRegistrationButton;

    public void initialize(){
        createFileToStoreAccounts();

    }

    private void createFileToStoreAccounts(){
        if(!new File("src/scoutingList/data/accounts.txt").exists()){
            File file = new File("src/scoutingList/data/accounts.txt");
            try {
                file.createNewFile();
            } catch (IOException e){
                System.out.println("Couldn't create accounts.txt file");
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void createAccount(){
        String firstName, lastName, emailAddress, login, password, confirmedPassword;
        Stage registrationWindow = (Stage) confirmRegistrationButton.getScene().getWindow();

        firstName = firstNameField.getText();
        lastName = lastNameField.getText();
        emailAddress = emailAddressField.getText();
        login = loginField.getText();
        password = registrationPasswordField.getText();
        confirmedPassword = registrationPasswordField_CONFIRMATION.getText();

        int registrationValidationResult = registrationValidation(firstName, lastName, emailAddress, login, password, confirmedPassword);


        if(registrationValidationResult == 0){
            Account newAccount = new Account(firstName, lastName, emailAddress, login, password);
            dataAccount.getAccounts().add(newAccount);
            dataAccount.getLoginsAndPasswords().put(login, password);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Registration successful");
            successAlert.setHeaderText(null);
            successAlert.setContentText("You've successfully created your account. Now you can log in.");
            successAlert.show();
            registrationWindow.hide();
        } else {
            registrationFailed(registrationValidationResult);
        }
    }


    @FXML
    private void openRegistrationWindow(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxmls/registrationWindow.fxml"));
            Stage registrationWindow = new Stage();
            registrationWindow.setTitle("Create account");
            registrationWindow.setScene(new Scene(root, 450, 450));
            registrationWindow.initModality(Modality.APPLICATION_MODAL); // "locks" log in window.
            registrationWindow.show();
        }
        catch (IOException e) {
            System.out.println("ERROR while opening registration window.");
            e.printStackTrace();
        }
    }

    @FXML
    private void openMainWindow(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxmls/mainWindow.fxml"));
            Stage mainWindow = new Stage();
            mainWindow.setTitle("Scouting List");
            mainWindow.setScene(new Scene(root, 1030, 600));
            mainWindow.initModality(Modality.APPLICATION_MODAL); // "locks" log in window.
            mainWindow.show();
        }
        catch (IOException e) {
            System.out.println("ERROR while opening main window.");
            e.printStackTrace();
        }
    }

    private int registrationValidation(String firstName, String lastName,
                                       String emailAddress, String login,
                                       String password,  String confirmedPassword){

        ObservableList<Account> accounts = dataAccount.getAccounts();

        if(firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || login.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()){
            return -1; // returns -1 if there is a field not filled.
        } else if (login.length() < 6 || login.length() > 25){
            return -2; // returns -2 if login has less characters than 6 or more than 25.
        } else if (!password.equals(confirmedPassword)){
            return -3; // returns -3 if password don't match confirmedPassword.
        } else if(login.contains(";") || firstName.contains(";") || lastName.contains(";") || emailAddress.contains(";") || password.contains(";")) {
            return -4; // returns -4 if there is ';' character in login (";" is separator used in .txt files).
        } else if(!(emailAddress.contains("@") && emailAddress.contains("."))){
            return -7; // returns -7 if e-mail address is invalid (in this case, there isn't at least one '@' and one '.').
        }

        for(Account account : accounts){
            if(emailAddress.equals(account.getEmailAddress())){
                return -5; // returns -5 if there is already an account with the same e-mail.
            }
            if(login.equals(account.getLogin())){
                return -6; // returns -6 if there is already an account with the same login.
            }
        }

        return 0; // returns 0 if everything's fine.
    }

    private void registrationFailed(int result){
        Alert registrationFailedAlert = new Alert(Alert.AlertType.ERROR);
        registrationFailedAlert.setTitle("Registration failed");
        registrationFailedAlert.setHeaderText("Couldn't create account.");

        switch (result){
            case -1:
                registrationFailedAlert.setContentText("You need to fill in all the fields.");
                break;
            case -2:
                registrationFailedAlert.setContentText("Login must be 6-25 characters long.");
                break;
            case -3:
                registrationFailedAlert.setContentText("Passwords don't match.");
                break;
            case -4:
                registrationFailedAlert.setContentText("Field contains illegal character ';'.");
                break;
            case -5:
                registrationFailedAlert.setContentText("An account already exists with provided e-mail address.");
                break;
            case -6:
                registrationFailedAlert.setContentText("An account already exists with provided login.");
                break;
            case -7:
                registrationFailedAlert.setContentText("Invalid e-mail address.");
                break;
        }

        registrationFailedAlert.show();
    }


    @FXML
    private void logIn(){
        String login = loginField.getText();
        String password = passwordField.getText();
        HashMap<String, String> loginsAndPasswords = dataAccount.getLoginsAndPasswords();

        Alert unsuccessfulLoginAttempt = new Alert(Alert.AlertType.ERROR);
        unsuccessfulLoginAttempt.setTitle("Log in failure");
        unsuccessfulLoginAttempt.setHeaderText("Logging in was unsuccessful.");

        if(!login.isEmpty() && !password.isEmpty()){
            if(loginsAndPasswords.containsKey(login)){
                if(loginsAndPasswords.get(login).equals(password)){
                    dataAccount.setLoginStage((Stage) loginField.getScene().getWindow());
                    dataAccount.setLoggedUserLogin(login);
                    openMainWindow();
                } else {
                    unsuccessfulLoginAttempt.setContentText("Wrong password.");
                    unsuccessfulLoginAttempt.show();
                }
            } else {
                unsuccessfulLoginAttempt.setContentText("There is no user with such login.");
                unsuccessfulLoginAttempt.show();
            }
        } else {
            unsuccessfulLoginAttempt.setContentText("You've didn't provide login, password or both.");
            unsuccessfulLoginAttempt.show();
        }
    }


}

