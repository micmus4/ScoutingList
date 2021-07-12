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



    /**
     * The method is responsible for setting TableView content based on chosen filters. It calls proper filterBy* methods
     * and then sets the contents of TableView with filtered ObservableArrayList of Footballers
     * and then the Filter Window is automatically closed.
     *
     * This method is called when user clicks 'Filter' in Filter Window.
     */

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



    /**
     * This method is responsible for hiding Filter Window.
     *
     * This method is called in <code>filterTableView()</code> method and also when user selects 'Cancel' in Filter Window.
     */

    @FXML
    private void hideWindow(){
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.hide();
    }



    /**
     * This method sets all fields in Filter Window to null or gives them empty value. Its purpose is to 'clean' all the fields
     * in Filter Window so the user doesn't have to do it all by himself.
     *
     * This method is called when user clicks on "Delete all filters" button in Filter Window.
     */

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



    /**
     * This method calls <code>Countries.getCountries()</code> and sets listOfCountries ObservableArrayList with an
     * ObservableArrayList of all (or at least most of) world's countries. Then this ObservableArrayList is set as content
     * of countries ComboBox.
     *
     * This method is called everytime Filter Window is opened.
     */

    private void setCountries(){
        ObservableList<String> listOfCountries = Countries.getCountries();
        countries.getItems().setAll(listOfCountries);
    }



    /**
     * This method calls <code>MarketValues.getMarketValues()</code> and sets values ObservableArrayList with an
     * ObservableArrayList of all possible market values. Then this ObservableArrayList is set as content
     * of leftValueRange and rightValueRanges ComboBoxes.
     *
     * This method is called everytime Filter Window is opened.
     */

    private void setValues(){
        ObservableList<String> values = MarketValues.getMarketValues();
        leftValueRange.setItems(values);
        rightValueRange.setItems(values);
    }



    /**
     * All handle*() methods are called when user clicks on the most right CheckBox (filter* CheckBoxses) in Filter Window.
     * If user selects the filter* CheckBox, then all the fields are open to input any data to them.
     * If user DE-selects the CheckBox, then all fields are disabled and the filter option is not considered.
     *
     * Those methods are called when user selects/deselects any filter* CheckBoxes (proper method is called for proper CheckBox).
     */


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



    /**
     * This method disables (if noContract / Free agent: CheckBox is selected) dateOfContractExpiration DatePicker
     * and clubTextField TextField as they shouldn't be considered in this case. It also enables (if noContract/Free agent: CheckBox is selected)
     * dateOfContractExpiration DatePicker and clubTextField TextField.
     *
     * This method is called when user selects/deselects noContract (Free agent:) CheckBox.
     */

    @FXML
    private void freeAgentHandler(){
        if(noContract.isSelected()){
            dateOfContractExpiration.setValue(null);
            dateOfContractExpiration.setDisable(true);
            clubTextField.setText("");
            clubTextField.setDisable(true);
        }
    }



    /**
     * This method (if noManager / Unknown/None CheckBox is selected) disables and empties managerTextField TextField. It also
     * (if noManager / Unknown/None CheckBox is deselected) enables managerTextField and user can use manager filter.
     *
     * This method is called when user selects/deselects noManager / Unknown/None CheckBox.
     */

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



    /**
     * This method sets range of rightValueRange ComboBox so it doesn't contain any market values that are
     * smaller than the value in leftValueRange.
     *
     * This method is called every time user changes the value of leftValueRange ComboBox.
     */

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



    /**
     * This method sets range of leftValueRange ComboBox so it doesn't contain any market values that are
     * bigger than the value in rightValueRange.
     *
     * This method is called every time user changes the value of rightValueRange ComboBox.
     */

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



    /**
     * This method sets range of rightHeightRange Spinner so it doesn't allow values that are smaller than the current value
     * in leftHeightRange.
     *
     * This method is called everytime user changes value of leftHeightRange Spinner.
     */

    @FXML
    private void setRightHeightRange(){
        if(filterHeight.isSelected() && leftHeightRange.getValue() != null){
            int leftRange = leftHeightRange.getValue();
            SpinnerValueFactory<Integer> range = new SpinnerValueFactory.IntegerSpinnerValueFactory(leftRange, 225 , rightHeightRange.getValue());
            rightHeightRange.setValueFactory(range);
        }
    }



    /**
     * This method sets range of leftHeightRange Spinner so it doesn't allow values that are bigger than the current value
     * in rightHeightRange.
     *
     * This method is called everytime user changes value of rightHeightRange Spinner.
     */

    @FXML
    private void setLeftHeightRange(){
        if(filterHeight.isSelected() && rightHeightRange.getValue() != null){
            int rightRange = rightHeightRange.getValue();
            SpinnerValueFactory<Integer> range = new SpinnerValueFactory.IntegerSpinnerValueFactory(150, rightRange, leftHeightRange.getValue());
            leftHeightRange.setValueFactory(range);
        }
    }



    /**
     * This method sets range of rightWeightRange Spinner so it doesn't allow values that are smaller than the current value
     * in leftWeightRange.
     *
     * This method is called everytime user changes value of leftWeightRange Spinner.
     */

    @FXML
    private void setRightWeightRange(){
        if(filterWeight.isSelected() && leftWeightRange.getValue() != null){
            int leftRange = leftWeightRange.getValue();
            SpinnerValueFactory<Integer> range = new SpinnerValueFactory.IntegerSpinnerValueFactory(leftRange, 125 , rightWeightRange.getValue());
            rightWeightRange.setValueFactory(range);
        }
    }



    /**
     * This method sets range of leftWeightRange Spinner so it doesn't allow values that are bigger than the current value
     * in rightWeightRange.
     *
     * This method is called everytime user changes value of rightWeightRange Spinner.
     */

    @FXML
    private void setLeftWeightRange(){
        if(filterWeight.isSelected() && rightWeightRange.getValue() != null){
            int rightRange = rightWeightRange.getValue();
            SpinnerValueFactory<Integer> range = new SpinnerValueFactory.IntegerSpinnerValueFactory(40, rightRange, leftWeightRange.getValue());
            leftWeightRange.setValueFactory(range);
        }
    }



    /**
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose
     * names contain String sequence passed in nameTextField TextField. The method ignores letter case and extra spaces.
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return ObservableArrayList<Footballer> containing all Footballers whose names contain value passed in nameTextField TextField.
     */

    private ObservableList<Footballer> filterByName(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        for(Footballer footballer : allFootballers){
            if(footballer.getName().toLowerCase().trim().contains(nameTextField.getText().toLowerCase().trim())){
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose
     * nationality is exactly the same as the nationality user chooses in countries ComboBox.
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return ObservableArrayList containing all Footballers whose nationalities match the nationality chosen in countries ComboBox.
     */

    private ObservableList<Footballer> filterByNationality(ObservableList<Footballer> allFootballers){
        ArrayList<Footballer> filteredFootballers = new ArrayList<>();
        for (Footballer footballer : allFootballers) {
            if (footballer.getNationality().equals(countries.getValue())) {
                filteredFootballers.add(footballer);
            }
        }
        return FXCollections.observableArrayList(filteredFootballers);
    }



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose (if noContract
     * CheckBox is unselected) club contains String sequence passed in clubTextField TextField or (if noContract CheckBox is selected)
     * whose club value is equal to String value "*None*". The method ignores letter case and extra spaces.
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return
     *         if noContract CheckBox is selected: returns ObservableArrayList of Footballers whose club contain value passed in
     *                                             clubTextField TextField.
     *         if noContract CheckBox is selected: returns ObservableArrayList of Footballers whose club value is equal to "*None*".
     */

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



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose
     * (if noManager CheckBox is unselected) manager value contain value passed in managerTextField TextField or (if noManager CheckBox is
     * selected) it returns an ObservableArrayList of Footballers whose manager value is equal to "*Unknown*". This method ignores extra
     * spaces and letter case.
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return
     *          if noManager CheckBox is selected: ObservableArrayList of Footballers whose manager value is equal to "*Unknown*"
     *          if noManager CheckBox is unselected: ObservableArrayList of Footballers whose manager value contains value passed in
     *                                               managerTextField TextField.
     */

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



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose
     * preferred foot is left (if leftFootButton RadioButton is selected) or right (if rightFootButton RadioButton is selected)
     * or both (if bothFeetButton RadioButton is selected).
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return ObservableArrayList containing all Footballers whose preferredFoot is:
     *
     *                  - left (if leftFootButton RadioButton is selected).
     *                  - right (if rightFootButton RadioButton is selected).
     *                  - both (if bothFeetButton RadioButton is selected).
     */

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



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose market
     * value is bigger than/equal to leftValueRange ComboBox or smaller than/equal to rightValueRange ComboBox. It converts value from String
     * to Integer version (by using <code>valueToInt(String value)</code> method so the mathematical comparison can be done.
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return ObservableArrayList containing all Footballers whose market value is in between
     *          the values of leftValueRange ComboBox and rightValueRange ComboBox.
     */

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



    /**
     * This method converts value from its natural String version to Integer version. It deletes all spaces and "€" character,
     * and then parses the value to Integer.
     *
     * @param stringValue - Footballer's value in its String version (e.g "€80 000 000")
     * @return Footballer's value parsed to Integer (e.g String: "€80 000 000" -> Integer: 80000000)
     */

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



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose
     * (if youngerButton RadioButton is selected) age is smaller than or (if equalButton RadioButton is selected) equal to
     * or (if olderButton RadioButton is selected) bigger than age passed in ageRange Spinner
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return ObservableArrayList containing all Footballers whose age is:
     *
     *                   - smaller than age value passed in ageRange Spinner (if youngerButton RadioButton is selected).
     *                   - equal to age value passed in ageRange Spinner (if equalButton RadioButton is selected).
     *                   - bigger than age value passed in ageRange Spinner (if olderButton RadioButton is selected).
     */

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



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose height
     * is higher than leftHeightRange Spinner's value and smaller than rightHeightRange Spinner's value. It converts values from those two
     * Spinners (by using <code>heightParser(String stringHeight)</code> method so the mathematical comparison can be done.
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return ObservableArrayList containing all Footballers whose height is in between values from leftHeightRange and
     *         rightHeightRange Spinners.
     */

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



    /**
     * This method converts height value from String version to Integer version by splitting the String version into an array
     * whose every element is exactly one character from that String and later on a new String is built without 'cm' suffix at the end
     * which enables the new String value to be converted into Integer value.
     *
     * @param stringHeight - Footballer's height in String version.
     * @return Footballer's height in its Integer version (without 'cm' at the end)
     */

    private int heightParser(String stringHeight){
        String[] height = stringHeight.split("");
        StringBuilder parserHeight = new StringBuilder();
        for(int i = 0; i <= 2; i++){
            parserHeight.append(height[i]);
        }
        return Integer.parseInt(parserHeight.toString());

    }



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose weight
     * is higher than leftWeightRange Spinner's value and smaller than rightWeightRange Spinner's value. It converts values from those two
     * Spinners (by using <code>weightParser(String stringWeight)</code> method so the mathematical comparison can be done.
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return ObservableArrayList containing all Footballers whose weight is in between values from leftWeightRange and
     *         rightWeightRange Spinners.
     */

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



    /**
     * This method converts weight value from String version to Integer version by splitting the String version into an array
     * whose every element is exactly one character from that String and later on a new String is built without 'kg' suffix at the end
     * which enables the new String value to be converted into Integer value.
     *
     * @param stringWeight - Footballer's weight in String version.
     * @return Footballer's weight in its Integer version (without 'kg' at the end)
     */

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



    /**
     *
     * This method filters through ObservableArrayList of Footballers and returns an ObservableArrayList of Footballers whose date of
     * contract expiration is sooner than date from dateOfContractExpiration DatePicker. It converts String version of date to LocalDate's
     * version and then compares two dates by using inbuilt compareTo method from LocalDate's class.
     *
     * @param allFootballers - ObservableArrayList of Footballers to filter.
     * @return ObservableArrayList containing all Footballers whose dateOfContractExpiration is sooner
     *         than dateOfContractExpiration DatePicker's date.
     */

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
