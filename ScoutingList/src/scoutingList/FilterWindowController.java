package scoutingList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import scoutingList.classes.Countries;
import scoutingList.classes.DataFootballer;
import scoutingList.classes.Footballer;
import scoutingList.classes.MarketValues;

import java.time.LocalDate;
import java.util.ArrayList;

public class FilterWindowController {

    private final DataFootballer dataFootballer = DataFootballer.getInstance();

    @FXML
    private TextField nameTextField, clubTextField, managerTextField;

    @FXML
    private ComboBox<String> countries, leftValueRange, rightValueRange;

    @FXML
    private CheckBox filterName, filterClub, filterWeight, filterHeight,
                     filterAge, filterFoot, filterExpiration, filterManager, filterNationality, filterValue,
                     leftFootButton, rightFootButton, bothFeetButton, noContract, noManager;

    @FXML
    private Spinner<Integer> leftHeightRange, rightHeightRange, leftWeightRange, rightWeightRange, ageRange;

    @FXML
    private RadioButton youngerButton, olderButton, equalButton;

    @FXML
    private DatePicker dateOfContractExpiration;

    public void initialize(){
        setCountries();
        setValues();
    }


    @FXML
    private void filterTableView(){
        ObservableList<Footballer> allFootballers = FXCollections.observableArrayList(dataFootballer.getFootballers());

        if(filterName.isSelected() && nameTextField.getText() != null && !nameTextField.getText().equals("")){
            allFootballers = FXCollections.observableArrayList(filterByName(allFootballers));
        }

        if(filterNationality.isSelected() && countries.getValue() != null && !countries.getValue().equals("")){
            allFootballers = FXCollections.observableArrayList(filterByNationality(allFootballers));
        }

        if(filterClub.isSelected()){
            if(noContract.isSelected() || (!noContract.isSelected() && clubTextField.getText().equals(""))){
                allFootballers = FXCollections.observableArrayList(filterByClub(allFootballers));
            }
        }

        if(!filterClub.isSelected() && noContract.isSelected()){
            allFootballers = FXCollections.observableArrayList(filterByClub(allFootballers));
        }

        if(filterManager.isSelected()){
            if(noManager.isSelected() || (!noManager.isSelected() && !managerTextField.getText().equals(""))){
                allFootballers = FXCollections.observableArrayList(filterByManager(allFootballers));
            }
        }

        if(filterFoot.isSelected() && (leftFootButton.isSelected() || rightFootButton.isSelected() || bothFeetButton.isSelected())){
            allFootballers = FXCollections.observableArrayList(filterByFoot(allFootballers));
        }

        if(filterValue.isSelected() && (leftValueRange.getValue() != null && rightValueRange.getValue() != null)){
            allFootballers = FXCollections.observableArrayList((filterByValue(allFootballers)));
        }

        if(filterAge.isSelected() && (youngerButton.isSelected() || olderButton.isSelected() || equalButton.isSelected())){
            allFootballers = FXCollections.observableArrayList(filterByAge(allFootballers));
        }

        if(filterHeight.isSelected()){
            allFootballers = FXCollections.observableArrayList(filterByHeight(allFootballers));
        }

        if(filterWeight.isSelected()){
            allFootballers = FXCollections.observableArrayList(filterByWeight(allFootballers));
        }

        if(filterExpiration.isSelected() && dateOfContractExpiration.getValue() != null && !noContract.isSelected()){
            allFootballers = FXCollections.observableArrayList(filterByExpiration(allFootballers));
        }


        dataFootballer.getFootballerTableView().setItems(allFootballers);
        hideWindow();
    }

    @FXML
    private void hideWindow(){
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void resetFilters(){
        if(filterName.isSelected()){
            nameTextField.setText("");
            nameTextField.setDisable(true);
            filterName.setSelected(false);
        }
        if(filterNationality.isSelected()){
            countries.setValue("");
            countries.setDisable(true);
            filterNationality.setSelected(false);
        }
        if(filterClub.isSelected()){
            clubTextField.setText("");
            clubTextField.setDisable(true);
            filterClub.setSelected(false);
        }
        if(filterManager.isSelected()){
            managerTextField.setText("");
            noManager.setSelected(false);
            noManager.setDisable(true);
            managerTextField.setDisable(true);
            filterManager.setSelected(false);
        }
        if(filterAge.isSelected()){
            ageRange.getValueFactory().setValue(null);
            youngerButton.setSelected(false);
            olderButton.setSelected(false);
            equalButton.setSelected(false);
            ageRange.setDisable(true);
            youngerButton.setDisable(true);
            olderButton.setDisable(true);
            equalButton.setDisable(true);
            filterAge.setSelected(false);
        }
        if(filterValue.isSelected()){
            leftValueRange.setValue("");
            rightValueRange.setValue("");
            leftValueRange.setDisable(true);
            rightValueRange.setDisable(true);
            filterValue.setSelected(false);
        }
        if(filterFoot.isSelected()){
            leftFootButton.setSelected(false);
            rightFootButton.setSelected(false);
            bothFeetButton.setSelected(false);
            leftFootButton.setDisable(true);
            rightFootButton.setDisable(true);
            bothFeetButton.setDisable(true);
            filterFoot.setSelected(false);
        }
        if(filterWeight.isSelected()){
            leftWeightRange.getValueFactory().setValue(null);
            rightWeightRange.getValueFactory().setValue(null);
            leftWeightRange.setDisable(true);
            rightWeightRange.setDisable(true);
            filterWeight.setSelected(false);
        }
        if(filterHeight.isSelected()){
            leftHeightRange.getValueFactory().setValue(null);
            rightHeightRange.getValueFactory().setValue(null);
            leftHeightRange.setDisable(true);
            rightHeightRange.setDisable(true);
            filterHeight.setSelected(false);
        }
        if(filterExpiration.isSelected()){
            noContract.setSelected(false);
            dateOfContractExpiration.setValue(null);
            noContract.setDisable(true);
            dateOfContractExpiration.setDisable(true);
            filterExpiration.setSelected(false);
        }
    }

    private void setCountries(){
        ObservableList<String> listOfCountries = Countries.getCountries();
        countries.getItems().setAll(listOfCountries);
    }

    private void setValues(){
        ObservableList<String> values = MarketValues.getMarketValues();
        leftValueRange.setItems(values);
        rightValueRange.setItems(values);
    }

    @FXML
    private void handleNameFilter(){
        nameTextField.setDisable(!filterName.isSelected());
    }

    @FXML
    private void handleNationalityFilter(){
        countries.setDisable(!filterNationality.isSelected());
    }

    @FXML
    private void handleClubFilter(){
        clubTextField.setDisable(!filterClub.isSelected());
    }

    @FXML
    private void handleManagerFilter(){
        managerTextField.setDisable(!filterManager.isSelected());
        noManager.setSelected(false);
        noManager.setDisable(!filterManager.isSelected());
    }

    @FXML
    private void handleAgeFilter(){
        if(filterAge.isSelected()){
            ageRange.setDisable(false);
            youngerButton.setDisable(false);
            olderButton.setDisable(false);
            equalButton.setDisable(false);
        } else {
            ageRange.setDisable(true);
            youngerButton.setDisable(true);
            olderButton.setDisable(true);
            equalButton.setDisable(true);
        }
    }

    @FXML
    private void handleValueFilter(){
        if(filterValue.isSelected()){
            leftValueRange.setDisable(false);
            rightValueRange.setDisable(false);
        } else {
            leftValueRange.setDisable(true);
            rightValueRange.setDisable(true);
        }
    }

    @FXML
    private void handleFootFilter(){
        if(filterFoot.isSelected()){
            leftFootButton.setDisable(false);
            rightFootButton.setDisable(false);
            bothFeetButton.setDisable(false);
        } else {
            leftFootButton.setDisable(true);
            rightFootButton.setDisable(true);
            bothFeetButton.setDisable(true);
        }
    }

    @FXML
    private void handleWeightFilter(){
        if(filterWeight.isSelected()){
            leftWeightRange.setDisable(false);
            rightWeightRange.setDisable(false);
        } else {
            leftWeightRange.setDisable(true);
            rightWeightRange.setDisable(true);
        }

    }

    @FXML
    private void handleHeightFilter(){
        if(filterHeight.isSelected()){
            leftHeightRange.setDisable(false);
            rightHeightRange.setDisable(false);
        } else {
            leftHeightRange.setDisable(true);
            rightHeightRange.setDisable(true);
        }
    }

    @FXML
    private void handleExpirationFilter(){
        if(filterExpiration.isSelected()){
            dateOfContractExpiration.setDisable(false);
            noContract.setDisable(false);
        } else {
            dateOfContractExpiration.setDisable(true);
            noContract.setSelected(false);
            noContract.setDisable(true);
        }
    }

    @FXML
    private void freeAgentHandler(){
        if(noContract.isSelected()){
            dateOfContractExpiration.setValue(null);
            dateOfContractExpiration.setDisable(true);
            clubTextField.setText("");
            clubTextField.setDisable(true);
        }
    }

    @FXML
    private void noManagerHandler(){
        if(noManager.isSelected()){
            managerTextField.setText("");
            managerTextField.setDisable(true);
        }
        if(!noManager.isSelected() && filterManager.isSelected()){
            managerTextField.setDisable(false);
        }
    }

    @FXML
    private void setRightValueRange(){
        if(leftValueRange.getValue() != null && !leftValueRange.getValue().equals("")){
            ObservableList<String> values = MarketValues.getMarketValues();
            int index = values.indexOf(leftValueRange.getValue());
            ObservableList<String> newValues = FXCollections.observableArrayList();
            for(int i = index; i < values.size(); i++){
                newValues.add(values.get(i));
            }
            rightValueRange.setItems(newValues);
        }
    }

    @FXML
    private void setLeftValueRange(){
        if(rightValueRange.getValue() != null && !rightValueRange.getValue().equals("")){
            ObservableList<String> values = MarketValues.getMarketValues();
            int index = values.indexOf(rightValueRange.getValue());
            ObservableList<String> newValues = FXCollections.observableArrayList();
            for(int i = 0; i <= index; i++){
                newValues.add(values.get(i));
            }
           leftValueRange.setItems(newValues);
        }
    }

    @FXML
    private void setRightHeightRange(){
        if(filterHeight.isSelected() && leftHeightRange.getValue() != null){
            int leftRange = leftHeightRange.getValue();
            SpinnerValueFactory<Integer> range = new SpinnerValueFactory.IntegerSpinnerValueFactory(leftRange, 225 , rightHeightRange.getValue());
            rightHeightRange.setValueFactory(range);
        }
    }

    @FXML
    private void setLeftHeightRange(){
        if(filterHeight.isSelected() && rightHeightRange.getValue() != null){
            int rightRange = rightHeightRange.getValue();
            SpinnerValueFactory<Integer> range = new SpinnerValueFactory.IntegerSpinnerValueFactory(150, rightRange, leftHeightRange.getValue());
            leftHeightRange.setValueFactory(range);
        }
    }

    @FXML
    private void setRightWeightRange(){
        if(filterWeight.isSelected() && leftWeightRange.getValue() != null){
            int leftRange = leftWeightRange.getValue();
            SpinnerValueFactory<Integer> range = new SpinnerValueFactory.IntegerSpinnerValueFactory(leftRange, 125 , rightWeightRange.getValue());
            rightWeightRange.setValueFactory(range);
        }
    }

    @FXML
    private void setLeftWeightRange(){
        if(filterWeight.isSelected() && rightWeightRange.getValue() != null){
            int rightRange = rightWeightRange.getValue();
            SpinnerValueFactory<Integer> range = new SpinnerValueFactory.IntegerSpinnerValueFactory(40, rightRange, leftWeightRange.getValue());
            leftWeightRange.setValueFactory(range);
        }
    }

    private ObservableList<Footballer> filterByName(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        for(Footballer footballer : allFootballers){
            if(footballer.getName().toLowerCase().trim().contains(nameTextField.getText().toLowerCase().trim())){
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }

    private ObservableList<Footballer> filterByNationality(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        for (Footballer footballer : allFootballers) {
            if (footballer.getNationality().equals(countries.getValue())) {
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }

    private ObservableList<Footballer> filterByClub(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        if(noContract.isSelected()){
            for(Footballer footballer : allFootballers){
                if(footballer.getClub().equals("*None*")){
                    filteredFootballers.add(footballer);
                }
            }
        } else {
            for (Footballer footballer : allFootballers) {
                if (footballer.getClub().toLowerCase().trim().contains(clubTextField.getText().toLowerCase().trim())) {
                    filteredFootballers.add(footballer);
                }
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }

    private ObservableList<Footballer> filterByManager(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();

        if(noManager.isSelected()){
            for(Footballer footballer : allFootballers){
                if(footballer.getManager().equals("*Unknown*")){
                    filteredFootballers.add(footballer);
                }
            }
        } else {
            for (Footballer footballer : allFootballers) {
                if (footballer.getManager().toLowerCase().trim().contains(managerTextField.getText().toLowerCase().trim())) {
                    filteredFootballers.add(footballer);
                }
            }
        }

        return FXCollections.observableArrayList(filteredFootballers);
    }

    private ObservableList<Footballer> filterByFoot(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        String preferredFoot;
        if(leftFootButton.isSelected()){
            preferredFoot = "Left";
        } else if (rightFootButton.isSelected()){
            preferredFoot = "Right";
        } else {
            preferredFoot = "Both";
        }
        for(Footballer footballer : allFootballers){
            if(footballer.getPreferredFoot().equals(preferredFoot)){
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }

    private ObservableList<Footballer> filterByValue(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();

        String leftValueRangeString = leftValueRange.getValue();
        String rightValueRangeString = rightValueRange.getValue();

        int leftValueInt = valueToInt(leftValueRangeString);
        int rightValueInt = valueToInt(rightValueRangeString);

        for(Footballer footballer : allFootballers){
            int footballerValue = valueToInt(footballer.getMarketValue());
            if(footballerValue >= leftValueInt && footballerValue <= rightValueInt){
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }

    public static int valueToInt(String stringValue){
        String[] elements = stringValue.split("");
        StringBuilder temp = new StringBuilder();
        for(int i = 1; i < elements.length; i++){
            if(!elements[i].equals(" ")){
                temp.append(elements[i]);
            }
        }
        return Integer.parseInt(temp.toString());
    }

    private ObservableList<Footballer> filterByAge(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();


        int ageToCompareTo = ageRange.getValue();

        int currentYear = LocalDate.now().getYear();

        if(youngerButton.isSelected()){
            for(Footballer footballer : allFootballers){
                LocalDate dateOfBirth = LocalDate.parse(footballer.getDateOfBirth());
                int yearOfBirth = dateOfBirth.getYear();
                int howOld = currentYear - yearOfBirth;
                if(howOld < ageToCompareTo){
                    filteredFootballers.add(footballer);
                }
            }

        } else if (olderButton.isSelected()) {
            for(Footballer footballer : allFootballers){
                LocalDate dateOfBirth = LocalDate.parse(footballer.getDateOfBirth());
                int yearOfBirth = dateOfBirth.getYear();
                int howOld = currentYear - yearOfBirth;
                if(howOld > ageToCompareTo){
                    filteredFootballers.add(footballer);
                }
            }

        } else {
            for(Footballer footballer : allFootballers){
                LocalDate dateOfBirth = LocalDate.parse(footballer.getDateOfBirth());
                int yearOfBirth = dateOfBirth.getYear();
                int howOld = currentYear - yearOfBirth;
                if(howOld == ageToCompareTo){
                    filteredFootballers.add(footballer);
                }
            }
        }

        return FXCollections.observableArrayList(filteredFootballers);
    }

    private ObservableList<Footballer> filterByHeight(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        int leftRange = leftHeightRange.getValue();
        int rightRange = rightHeightRange.getValue();
        for(Footballer footballer : allFootballers){
            int height = heightParser(footballer.getHeight());
            if(height >= leftRange && height <= rightRange){
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }

    private int heightParser(String stringHeight){
        String[] height = stringHeight.split("");
        StringBuilder parserHeight = new StringBuilder();
        for(int i = 0; i <= 2; i++){
            parserHeight.append(height[i]);
        }
        return Integer.parseInt(parserHeight.toString());

    }

    private ObservableList<Footballer> filterByWeight(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        int leftRange = leftWeightRange.getValue();
        int rightRange = rightWeightRange.getValue();
        for(Footballer footballer : allFootballers){
            int weight = weightParser(footballer.getWeight());
            if(weight >= leftRange && weight <= rightRange){
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }

    private int weightParser(String stringWeight){
        String[] weight = stringWeight.split("");
        StringBuilder parsedWeight = new StringBuilder();
        for(int i = 0; i <= 3; i++){
            if(!weight[i].equals("k")){
                parsedWeight.append(weight[i]);
            } else {
                break;
            }
        }
        return Integer.parseInt(parsedWeight.toString());
    }

    private ObservableList<Footballer> filterByExpiration(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        for(Footballer footballer : allFootballers){
            LocalDate expirationDate = LocalDate.parse(footballer.getContractExpirationDate());
            LocalDate comparedDate = dateOfContractExpiration.getValue();
            if(expirationDate.compareTo(comparedDate) < 0){
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }

}
