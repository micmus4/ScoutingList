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
        forbidChoosingUnproperDates();
        setCountries();
        setPreferredFoot();
        setMarketValues();
    }



    private void setCountries(){
        ObservableList<String> listOfCountries = Countries.getCountries();
        countries.getItems().setAll(listOfCountries);
    }

    private void setMarketValues(){
        ObservableList<String> values = MarketValues.getMarketValues();
        marketValueComboBox.getItems().addAll(values);
    }

    private void setPreferredFoot(){
        preferredFootComboBox.getItems().addAll("Left", "Right", "Both");
        preferredFootComboBox.getSelectionModel().select("Right");
    }


    private void forbidChoosingUnproperDates(){
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




    @FXML
    private void hideWindow(){
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.hide();
    }

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

    @FXML
    private void handleNoManager(){
        managerTextField.setDisable(noManager.isSelected());
    }
}
