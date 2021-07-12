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



    /***
     * This method creates 'accounts.txt' - file, which contains data of all users - when program is opened
     * for the first time. If for any reason the file will be deleted, the program will once again create it.
     *
     * This method is called everytime Login Window is opened.
     */

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



    /***
     * This method collects all the data user inputed while creating an account.
     * It then calls <code>registrationValidation(String firstName, String lastName, String emailAddress, String login,
     * String password,  String confirmedPassword)</code> method which is responsible for checking whether user has provided
     * correct data. If registrationValidation method returned 0, it means that data is correct and user has succeeded in creating
     * an account.
     *
     * This method is called when you click 'Register' while you're in Registration Window.
     */

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



    /**
     * This method is responsible for opening new window (Registration Window - fxmls/registrationWindow.fxml).
     * It locks Login Window until user creates an account or closes Registration Window.
     *
     * This method is called when you click 'Register' in Login Window.
     */

    @FXML
    private void openRegistrationWindow(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxmls/registrationWindow.fxml"));
            Stage registrationWindow = new Stage();
            registrationWindow.setTitle("Create account");
            registrationWindow.setScene(new Scene(root, 450, 450));
            registrationWindow.initModality(Modality.APPLICATION_MODAL);
            registrationWindow.show();
        }
        catch (IOException e) {
            System.out.println("ERROR while opening registration window.");
            e.printStackTrace();
        }
    }



    /**
     * This method is responsible for opening new window (Main Window - mainWindow.fxml) which contains
     * the TableView with all the Footballers that user added. It locks Login Window which is later hidden.
     *
     * This method is called when you click 'Log in' in Login Window and the <code>logIn()</code> method is successful
     * (the user passed correct login and password).
     */

    @FXML
    private void openMainWindow(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxmls/mainWindow.fxml"));
            Stage mainWindow = new Stage();
            mainWindow.setTitle("Scouting List");
            mainWindow.setScene(new Scene(root, 1030, 600));
            mainWindow.initModality(Modality.APPLICATION_MODAL);
            mainWindow.show();
        }
        catch (IOException e) {
            System.out.println("ERROR while opening main window.");
            e.printStackTrace();
        }
    }



    /**
     *
     * @param firstName - user's firstName (String).
     * @param lastName - user's lastName (String).
     * @param emailAddress - user's e-mail address (String).
     * @param login - user's login (String).
     * @param password - user's password (String).
     * @param confirmedPassword user's password once again, for confirmation purposes (String).
     *
     * All the params are user's personal information provided while registration.
     *
     * The method is called when creating a new account (it is called indirectly when clicking 'Register' in Register Window).
     *
     * @return
     *          0 - if all the dats is correct and the account can be created.
     *
     *         -1 - if at least one parameter passes is empty.
     *         -2 - if login is shorter than 6 characters long or longer than 25 characters.
     *         -3 - if the confirmation of the password didn't work (user has provided different passwords while registering)
     *         -4 - if any parameter contains ";" character which is illegal in this program.
     *         -5 - if there already is an user with provided e-mail address.
     *         -6 - if there already is an user with provided login.
     *         -7 - if e-mail address and login don't contain at least one "@" and one "." characters.
     */

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



    /**
     * This method is called when registrationValidation(Strings...) method returns different value than 0. It creates an Alert
     * which message depends on returned value of registrationValidation method and it gives user information about
     * what he did wrong when trying to create an account.
     *
     * @param result - result of registrationValidation(String firstName, String lastName, String emailAddress, String login,
     *                 String password, String confirmedPassword) method
     */

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



    /**
     * This method creates an Alert if Logging In failed (in this case when login or/and password are empty,
     * or when there isn't an user with such login or password is not correct for the login). If none of this occurs,
     * it means Logging In is correct and user is led to Main Window (mainWindow.fxml).
     *
     * This method is called when clicking "Log In" option in Login Window.
     */

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

