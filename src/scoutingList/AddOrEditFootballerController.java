package scoutingList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import scoutingList.classes.Countries;
import scoutingList.classes.MarketValues;
import scoutingList.classes.DataFootballer;
import scoutingList.classes.Footballer;


import java.time.LocalDate;

public class AddOrEditFootballerController {

    private final DataFootballer dataFootballer = DataFootballer.getInstance();

    @FXML
    private CheckBox noContract, noManager;

    @FXML
    private ComboBox<String> countries, marketValueComboBox, preferredFootComboBox;

    @FXML
    private TextField nameTextField, clubTextField, managerTextField;

    @FXML
    private Spinner<Integer> heightSpinner, weightSpinner;

    @FXML
    private Button addButton, editButton, addCancelButton, editCancelButton;

    @FXML
    private DatePicker dateOfBirthPicker, dateOfContractExpiration;


    public void initialize(){
        forbidChoosingImproperDates();
        setCountries();
        setPreferredFoot();
        setMarketValues();
    }



    /**
     * This method loads ObservableArrayList<String> of countries from <code>Countries.getCountries()</code> method
     * and then sets countries ComboBox's content from values of this ObservableArrayList.
     *
     * This method is called every time Add Window or Edit Window is opened.
     */

    private void setCountries(){
        ObservableList<String> listOfCountries = Countries.getCountries();
        countries.getItems().setAll(listOfCountries);
    }



    /**
     * This method loads ObservableArrayList<String> of market values from <code>MarketValues.getMarketValues()/code> method
     * and then sets marketValueComboBox ComboBox's content from values of this ObservableArrayList.
     *
     * This method is called every time Add Window or Edit Window is opened.
     */

    private void setMarketValues(){
        ObservableList<String> values = MarketValues.getMarketValues();
        marketValueComboBox.getItems().addAll(values);
    }



    /**
     * This method sets content of preferredFootComboBox ComboBox with String values: "Left", "Right" and "Both" and sets initial
     * value to "Right".
     *
     * This method is called every time Add Window or Edit Window is opened.
     */

    private void setPreferredFoot(){
        preferredFootComboBox.getItems().addAll("Left", "Right", "Both");
        preferredFootComboBox.getSelectionModel().select("Right");
    }



    /**
     * This method sets ranges of dateOfBirthPicker and dateOfContractExpiration DatePickers. When it comes to dateOfBirthPicker,
     * user is unable to choose a date which is further than current date. When it comes to dateOfContractExpiration, user is unable
     * to choose a date which is sooner than current date.
     *
     * This method is called every time Add Window or Edit Window is opened.
     */

    private void forbidChoosingImproperDates(){
        dateOfBirthPicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 );
            }
        });
        dateOfContractExpiration.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 );
            }
        });
    }



    /**
     * This method hides Add Window or Edit Window.
     *
     * This method is called when user chooses "Cancel" in Add Window or in Edit Window.
     */

    @FXML
    private void hideWindow(){
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.hide();
    }



    /**
     * This method is responsible for adding a new Footballer into TableView of Footballers. First, it collects all values
     * related to Footballer from TextFields, ComboBoxes, Spinners and DatePickers (additionally: it adds "cm" suffix to height value
     * and it adds "kg" value to weight value).
     *
     *      - If noContract CheckBox is selected, Footballer's contractExpirationDate is set to value
     *        "*None*" and also his club value is set to "*Free agent*".
     *      - If noManager CheckBox is selected, Footballer's manager value is set to value "*Unknown*"
     *
     * Then the method calls <code>dataVerifier(boolean freeAgent, boolean unknownManager)</code> method in order to verify the data.
     * If dataVerifier method returns true, the Footballer is added into TableView and the Add Window is hidden.
     *
     * This method is called when user chooses "Add" in Add Window.
     */

    @FXML
    private void addFootballer(){
        String name, nationality, club, manager, preferredFoot, marketValue, height, weight, dateOfBirth, contractExpirationDate;

        if(dataVerifier(noContract.isSelected(), noManager.isSelected())) {
            name = nameTextField.getText();
            nationality = countries.getValue();
            dateOfBirth = dateOfBirthPicker.getValue().toString();
            preferredFoot = preferredFootComboBox.getValue();
            marketValue = marketValueComboBox.getValue();
            height = heightSpinner.getValue().toString() + "cm";
            weight = weightSpinner.getValue().toString() + "kg";

            if(!noContract.isSelected()) {
                contractExpirationDate = dateOfContractExpiration.getValue().toString();
                club = clubTextField.getText();
            } else {
                contractExpirationDate = "*Free agent*";
                club = "*None*";
            }

            if(!noManager.isSelected()){
                manager = managerTextField.getText();
            } else {
                manager = "*Unknown*";
            }

            Footballer footballer = new Footballer(name, nationality, manager, dateOfBirth, club,
                                    preferredFoot, marketValue, weight, height, contractExpirationDate);
            dataFootballer.getFootballers().add(footballer);
            dataFootballer.saveFootballersToFile();
            hideWindow();
        }
    }



    /**
     * This method is responsible for disabling clubTextField and dateOfContractExpiration Controls as they shouldn't
     * be considered when Footballer has no contract.
     *
     * This method is called when user selects or deselects noContract CheckBox in Add Window or Edit Window.
     */

    @FXML
    private void turnOffUselessFieldsWhenFootballerIsAFreeAgent(){
        if(noContract.isSelected()){
            dateOfContractExpiration.setDisable(true);
            clubTextField.setText("");
            clubTextField.setDisable(true);
        } else {
            dateOfContractExpiration.setDisable(false);
            clubTextField.setDisable(false);
        }
    }



    /**
     *
     * This method checks whether there is an empty or a null value in one of the Controls. If so, the method will return false
     * and display an Alert which will tell the user to input value into every single Control which is available (clubTextField and
     * dateOfContractExpiration Controls can be turned off when noContract CheckBox is selected and managerTextField
     * TextField can be turned off when noManager CheckBox is selected). Otherwise the method will return true.
     *
     * @param freeAgent - value of noContract.isSelected().
     * @param unknownManager - value of noManager.isSelected().
     *
     * @return
     *
     *          true - if all Controls' values are not empty and they are not null.
     *          false - if at least one Control contains empty or null value.
     *
     * NOTE: If noContract CheckBox is selected, clubTextField TextField and dateOfContractExpiration DatePicker are not checked.
     *       If noManager CheckBox is selected, managerTextField TextField is not checked.
     *
     * This method is called inside AddFootballer() and editFootballer(Footballer footballer) methods.
     */

    private boolean dataVerifier(boolean freeAgent, boolean unknownManager){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Couldn't add new footballer");
        errorAlert.setHeaderText("Error - couldn't add a new footballer.");
        errorAlert.setContentText("You have to provide all the data in order to add new player into your list.");

        if(nameTextField.getText().isEmpty() || nameTextField.getText() == null){
            errorAlert.showAndWait();
            return false;
        }
        if(countries.getValue() == null || countries.getValue().isEmpty()){
            errorAlert.showAndWait();
            return false;
        }
        if(!freeAgent) {
            if (clubTextField.getText().isEmpty() || clubTextField.getText() == null) {
                errorAlert.showAndWait();
                return false;
            }
        }

        if(!unknownManager) {
            if (managerTextField.getText().isEmpty() || clubTextField.getText() == null) {
                errorAlert.showAndWait();
                return false;
            }
        }

        if(dateOfBirthPicker.getValue().toString().isEmpty()){
            errorAlert.showAndWait();
            return false;
        } else {
            dateOfBirthPicker.getValue();
        }
        if(preferredFootComboBox.getValue() == null || preferredFootComboBox.getValue().isEmpty()){
            errorAlert.showAndWait();
            return false;
        }
        if(marketValueComboBox.getValue() == null || marketValueComboBox.getValue().isEmpty()){
            errorAlert.showAndWait();
            return false;
        }
        if(heightSpinner.getValue().toString().isEmpty()){
            errorAlert.showAndWait();
            return false;
        } else {
            heightSpinner.getValue();
        }
        if(weightSpinner.getValue().toString().isEmpty()){
            errorAlert.showAndWait();
            return false;
        } else {
            weightSpinner.getValue();
        }
        if(!freeAgent) {
            if (dateOfContractExpiration.getValue().toString().isEmpty()) {
                errorAlert.showAndWait();
                return false;
            } else {
                dateOfContractExpiration.getValue();
            }
        }

        return true;
    }



    /**
     *
     * This method sets Edit Window' Controls values with selected Footballer's values. It de-converts height and weight value
     * to its form without 'cm' and 'kg' suffixes and sets their values back to Integer so they can be set in heightSpinner and
     * weightSpinner.
     *
     * @param footballer - selected Footballer from TableView.
     *
     * This method is called everytime user opens Edit Window.
     */

    public void setFieldsForEdit(Footballer footballer){
        nameTextField.setText(footballer.getName());
        countries.getSelectionModel().select(footballer.getNationality());
        managerTextField.setText(footballer.getManager());
        dateOfBirthPicker.setValue(LocalDate.parse(footballer.getDateOfBirth()));
        clubTextField.setText(footballer.getClub());
        preferredFootComboBox.setValue(footballer.getPreferredFoot());
        marketValueComboBox.setValue(footballer.getMarketValue());

        String height = footballer.getHeight();
        String[] heightElements = height.split("");
        String correctHeightFormat = "";

        for(int i = 0; i < height.length(); i++){
            if(!heightElements[i].equals("c")){
                correctHeightFormat += heightElements[i];
            } else {
                break;
            }
        }

        String weight = footballer.getWeight();
        String[] weightElements = weight.split("");
        String correctWeightFormat = "";

        for(int i = 0; i < weight.length(); i++){
            if(!weightElements[i].equals("k")){
                correctWeightFormat += weightElements[i];
            } else {
                break;
            }
        }



        heightSpinner.getValueFactory().setValue(Integer.parseInt(correctHeightFormat));
        weightSpinner.getValueFactory().setValue(Integer.parseInt(correctWeightFormat));
        if(footballer.getContractExpirationDate().equals("*Free agent*")){
            dateOfContractExpiration.setValue(null);
            noContract.setSelected(true);
            dateOfContractExpiration.setDisable(true);
        } else {
            dateOfContractExpiration.setValue(LocalDate.parse(footballer.getContractExpirationDate()));
        }

        addButton.setVisible(false);
        addCancelButton.setVisible(false);
        editButton.setVisible(true);
        editCancelButton.setVisible(true);
    }



    /**
     * This method is responsible for editing selected Footballer from TableView. First, it collects all values
     * related to Footballer from TextFields, ComboBoxes, Spinners and DatePickers (additionally: it adds "cm" suffix to height value
     * and it adds "kg" value to weight value).
     *
     * - If noContract CheckBox is selected, Footballer's contractExpirationDate is set to value
     *   "*None*" and also his club value is set to "*Free agent*".
     * - If noManager CheckBox is selected, Footballer's manager value is set to value "*Unknown*"
     *
     * Then the method calls <code>dataVerifier(boolean freeAgent, boolean unknownManager)</code> method in order to verify the data.
     * If dataVerifier method returns true, the Footballer's data is edited.
     *
     * This method is called when user chooses "Edit" in Edit Window.
     */

    @FXML
    private void editFootballer(){
        Footballer footballer = dataFootballer.getFootballerToEdit();

        String name, nationality, club, manager, preferredFoot, marketValue, height, weight, dateOfBirth, contractExpirationDate;

        if(dataVerifier(noContract.isSelected(), noManager.isSelected())) {
            name = nameTextField.getText();
            nationality = countries.getValue();
            dateOfBirth = dateOfBirthPicker.getValue().toString();
            preferredFoot = preferredFootComboBox.getValue();
            marketValue = marketValueComboBox.getValue();
            height = heightSpinner.getValue().toString() + "cm";
            weight = weightSpinner.getValue().toString() + "kg";

            if(!noContract.isSelected()) {
                contractExpirationDate = dateOfContractExpiration.getValue().toString();
                club = clubTextField.getText();
            } else {
                contractExpirationDate = "*Free agent*";
                club = "*None*";
            }

            if(!noManager.isSelected()){
                manager = managerTextField.getText();
            } else {
                manager = "*Unknown*";
            }

            Footballer newFootballer = new Footballer(name, nationality, manager, dateOfBirth, club,
                    preferredFoot, marketValue, weight, height, contractExpirationDate);


            int i = 0;
            for(Footballer ftbler : dataFootballer.getFootballers()){
                if(ftbler.equals(footballer)){
                    break;
                }
                i++;
            }

            dataFootballer.getFootballers().set(i, newFootballer);
            dataFootballer.saveFootballersToFile();
            hideWindow();
        }
    }



    /**
     * This method disables managerTextField TextField if noManager CheckBox is selected. If noManagerCheckBox is unselected,
     * the managerTextField TextField can gather data again.
     *
     * This method is called everytime noContract CheckBox is selected/deselected.
     */

    @FXML
    private void handleNoManager(){
        managerTextField.setDisable(noManager.isSelected());
    }
}
