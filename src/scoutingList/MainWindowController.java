package scoutingList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scoutingList.classes.DataAccount;
import scoutingList.classes.DataFootballer;
import scoutingList.classes.Footballer;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

public class MainWindowController{

    private final DataAccount dataAccount = DataAccount.getDataInstance();
    private final DataFootballer dataFootballer = DataFootballer.getInstance();

    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem editMenuItem = new MenuItem("Edit");
    private final MenuItem deleteMenuItem = new MenuItem("Delete");

    private final String loggedUserLogin = dataAccount.getLoggedUserLogin();

    @FXML
    private TableView<Footballer> footballerTableView;

    @FXML
    private TableColumn<Footballer, String> valueColumn;

    public void initialize(){
        createFileToStoreUsersData();

        dataAccount.getLoginStage().hide();
        dataFootballer.setLoggedUserLogin(loggedUserLogin);
        dataFootballer.loadFootballersFromFile();
        
        footballerTableView.setItems(dataFootballer.getFootballers());

        contextMenu.getItems().add(editMenuItem);
        contextMenu.getItems().add(deleteMenuItem);

        dataFootballer.setFootballerTableView(footballerTableView);
        sortByValues();

        rightClickOnCell();
    }



    /**
     * This method performs logging out functionality. It leads you from Main Window (the one with TableView) to the Login
     * Window. It also saves all the changes you've made and also resets content of TableView and ObservableArrayList of Footballers.
     *
     * This method is called when you click 'Manage' in the top left corner of the Main Window, and then 'Logout' option.
     */

    @FXML
    private void logout(){
        Stage mainWindowStage = (Stage) footballerTableView.getScene().getWindow();
        mainWindowStage.hide();
        dataAccount.getLoginStage().show();
        dataFootballer.saveFootballersToFile();
        dataFootballer.removeAllFootballers();
        footballerTableView.setItems(null);
    }



    /**
     *  This method opens a new window (Add Or Edit Footballer Window / addOrEditFootballerWindow.fxml) which is responsible
     *  for adding a new Footballer into the TableView.
     *
     *  This method is call when you click 'Manage' in the top left corner of the Main Window, and then 'Add' option.
     */

    @FXML
    private void openAddFootballerWindow(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxmls/addOrEditFootballerWindow.fxml"));
            Stage addFootballerWindow = new Stage();
            addFootballerWindow.setTitle("Add Footballer");
            addFootballerWindow.setScene(new Scene(root, 662, 567));
            addFootballerWindow.initModality(Modality.APPLICATION_MODAL);
            addFootballerWindow.show();
        }
        catch (IOException e) {
            System.out.println("ERROR while opening window.");
            e.printStackTrace();
        }
    }



    /**
     * This method opens a new window (Add Or Edit Footballer Window / addOrEditFootballerWindow.fxml) which is responsible for editing data of already existing Footballer
     * from your TableView. This method calls <code>setFieldsForEdit(Footballer footballer)</code> method from AddOrEditFootballerController
     * class and sets up all the fields with the data of chosen Footballer.
     *
     * This method is called when you:
     *  a) - click 'Manage' in top left corner of the Main Window, and then 'Edit' option.
     *  b) - click right mouse button on selected Footballer from TableView and then 'Edit' option.
     *
     *  NOTE: In order for method to work properly in case a), you have to select Footballer from TableView. Otherwise the method will
     *  immediately return and do nothing at all.
     */

    @FXML
    private void openEditFootballerWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/addOrEditFootballerWindow.fxml"));
            Parent root = loader.load();

            AddOrEditFootballerController controller = loader.getController();
            Footballer footballer = footballerTableView.getSelectionModel().getSelectedItem();
            if(footballer != null) {
                controller.setFieldsForEdit(footballer);
                dataFootballer.setFootballerToEdit(footballer);
            } else {
                return;
            }

            Stage addFootballerWindow = new Stage();
            addFootballerWindow.setTitle("Edit Footballer");
            addFootballerWindow.setScene(new Scene(root, 662, 567));
            addFootballerWindow.initModality(Modality.APPLICATION_MODAL);
            addFootballerWindow.show();

        }
        catch (IOException e) {
            System.out.println("ERROR while opening window.");
            e.printStackTrace();
        }
    }



    /**
     * This method opens a new window (Filter Window - filterWindow.fxml) which is responsible for setting filters that will
     * change the TableView current content.
     *
     * This method is call when you click 'Manage' in the top left corner of the Main Window, and then 'Filter' option.
     */

    @FXML
    private void openFilterWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmls/filterWindow.fxml"));
            Parent root = loader.load();


            Stage filterWindow = new Stage();
            filterWindow.setTitle("Filter");
            filterWindow.setScene(new Scene(root, 1280, 720));
            filterWindow.initModality(Modality.APPLICATION_MODAL);
            filterWindow.show();
        }
        catch (IOException e) {
            System.out.println("ERROR while opening window.");
            e.printStackTrace();
        }
    }



    /**
     * This method is responsible for deleting Footballer from TableView. First it pops up an alert
     * which asks you whether you're sure if you want to delete the selected Footballer, and if so then it deletes him
     * from your TableView.
     *
     * It is called when you click 'Manage' in top left corner of the Main Window and then 'Delete'
     *
     * NOTE: In order for the method to work properly, you must choose a Footballer from TableView. Otherwise the method
     * will return immediately.
     */

    @FXML
    private void deleteFootballer(){
        Footballer footballer = footballerTableView.getSelectionModel().getSelectedItem();
        if(footballer == null){
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Deleting footballer");
        confirmationAlert.setHeaderText("Removing " + footballer.getName());
        confirmationAlert.setContentText("Are you sure you wish to remove this footballer from the list?");

        ((Button) confirmationAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes, I'm sure");
        ((Button) confirmationAlert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancel");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.CANCEL){
            return;
        }
        if(result.isPresent() && result.get() == ButtonType.OK){
            dataFootballer.getFootballers().remove(footballer);
            dataFootballer.saveFootballersToFile();
            footballerTableView.setItems(dataFootballer.getFootballers());
        }
    }



    /**
     * This method creates a file which stores all the Footballers that user has in his/her TableView. The name of the file
     * is the login of logged user and .txt suffix. The method doesn't do anything if the file already exists. In normal case, it will
     * only create the file once, when new user logs into his/her account for the first time. Anyway, if fore some reason the file will
     * be deleted, then the method will create new file again.
     *
     * This method is called everytime user logs into his/her account.
     */

    private void createFileToStoreUsersData(){
        File file = new File("src/scoutingList/data/accountsData/" + loggedUserLogin + ".txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
                System.out.println("Couldn't create file to store user's data.");
            }
        }
    }



    /**
     * This method calls deleteFootballer() method when clicking 'DELETE' button on keyboard.
     *
     * @see <code>MainWindowController.deleteFootballer()</code>
     */

    @FXML
    private void deleteFootballerByKey(KeyEvent event){
        if(event.getCode() == KeyCode.DELETE){
            deleteFootballer();
        }
    }




    /**
     *  This method gives user an ability to edit or delete Footballer by right clicking on a Footballer. After user
     *  right clicked on Footballer, he will see two options, as mentioned above - to either delete or edit selected Footballer.
     *  After decision is made, the method calls either <code>openEditFootballerWindow()</code> method or <code>deleteFootballer()</code> method.
     */

    private void rightClickOnCell(){
        footballerTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if(t.getButton() == MouseButton.SECONDARY && footballerTableView.getSelectionModel().getSelectedItem() != null) {
                contextMenu.show(footballerTableView, t.getScreenX(), t.getScreenY());
            }
        });

        editMenuItem.setOnAction((ActionEvent e) -> openEditFootballerWindow());
        deleteMenuItem.setOnAction((ActionEvent e) -> deleteFootballer());

    }



    /**
     * This resets all the filter options you've chosen and therefore sets the TableView to its initial version,
     * which is the one containing every single Footballer you have.
     *
     * This method is called when you click 'Manage' on left-upper corner of Main Window and then 'Reset Filters'.
     */

    @FXML
    private void resetFilters(){
        footballerTableView.setItems(dataFootballer.getFootballers());
    }



    /**
     * This method is responsible for changing Comparator of Market Value column. It's purpose is to fix sorting by Footballers'
     * market values which in normal case is impossible due to Market Values being String instances.
     */

    private void sortByValues(){
        valueColumn.setComparator(new CompareValues());
    }



    /**
     * An inner CompareValues class which implements Comparator interface. It only has compare method which compares
     * two values that are parsed to Integer version from their String version thanks to
     * <code>FilterWindowController.valueToInt(String value)</code> method.
     */

    static class CompareValues implements Comparator<String>
    {
        public int compare(String value1, String value2)
        {
            if(value1 == null || value2 == null){
                return 0;
            }
            int intValue1 = FilterWindowController.valueToInt(value1);
            int intValue2 = FilterWindowController.valueToInt(value2);
            return Integer.compare(intValue1, intValue2);
        }
    }
}
