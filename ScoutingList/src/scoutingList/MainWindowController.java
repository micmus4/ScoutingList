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


    @FXML
    private void logout(){
        Stage mainWindowStage = (Stage) footballerTableView.getScene().getWindow();
        mainWindowStage.hide();
        dataAccount.getLoginStage().show();
        dataFootballer.saveFootballersToFile();
        dataFootballer.removeAllFootballers();
        footballerTableView.setItems(null);
    }

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
        }
    }




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

    @FXML
    private void deleteFootballerByKey(KeyEvent event){
        if(event.getCode() == KeyCode.DELETE){
            deleteFootballer();
        }
    }

    private void rightClickOnCell(){
        footballerTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if(t.getButton() == MouseButton.SECONDARY && footballerTableView.getSelectionModel().getSelectedItem() != null) {
                contextMenu.show(footballerTableView, t.getScreenX(), t.getScreenY());
            }
        });

        editMenuItem.setOnAction((ActionEvent e) -> openEditFootballerWindow());
        deleteMenuItem.setOnAction((ActionEvent e) -> deleteFootballer());

    }

    @FXML
    private void resetFilters(){
        footballerTableView.setItems(dataFootballer.getFootballers());
    }

    private void sortByValues(){
        valueColumn.setComparator(new CompareValues());
    }

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
