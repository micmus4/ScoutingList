package scoutingList.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class DataFootballer {

    private static final DataFootballer instance = new DataFootballer();
    private String loggedUserLogin;
    private ObservableList<Footballer> footballers;
    private Footballer footballerToEdit = null;
    private TableView<Footballer> footballerTableView = null;


    public TableView<Footballer> getFootballerTableView() {
        return footballerTableView;
    }

    public void setFootballerTableView(TableView<Footballer> footballerTableView) {
        this.footballerTableView = footballerTableView;
    }

    public Footballer getFootballerToEdit() {
        return footballerToEdit;
    }

    public void setFootballerToEdit(Footballer footballerToEdit) {
        this.footballerToEdit = footballerToEdit;
    }

    public DataFootballer() {
        this.footballers = FXCollections.observableArrayList();
    }

    public static DataFootballer getInstance() {
        return instance;
    }

    public void removeAllFootballers(){
        footballers = FXCollections.observableArrayList();
    }

    public ObservableList<Footballer> getFootballers() {
        return footballers;
    }

    public void setLoggedUserLogin(String loggedUserLogin) {
        this.loggedUserLogin = loggedUserLogin;
    }

    public void saveFootballersToFile(){
        Path path = Paths.get("src/scoutingList/data/accountsData/" + loggedUserLogin + ".txt");
        Iterator<Footballer> iterator = footballers.iterator();
        BufferedWriter writer = null;

        try {
            writer = Files.newBufferedWriter(path);
        } catch (IOException e){
            System.out.println("Error - connection to file wasn't established.");
            e.printStackTrace();
        }

        try {
            while (iterator.hasNext()) {
                Footballer footballer = iterator.next();
                try {
                    writer.write(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                            footballer.getName(),
                            footballer.getNationality(),
                            footballer.getManager(),
                            footballer.getDateOfBirth(),
                            footballer.getClub(),
                            footballer.getPreferredFoot(),
                            footballer.getMarketValue(),
                            footballer.getWeight(),
                            footballer.getHeight(),
                            footballer.getContractExpirationDate()
                    ));
                    writer.newLine();
                } catch (IOException e) {
                    System.out.println("Error - couldn't save footballers' data to file..");
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

    public void loadFootballersFromFile(){
        Path path = Paths.get("src/scoutingList/data/accountsData/" + loggedUserLogin + ".txt");
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

                String name = dataArray[0];
                String nationality = dataArray[1];
                String manager = dataArray[2];
                String dateOfBirth = dataArray[3];
                String club = dataArray[4];
                String preferredFoot = dataArray[5];
                String marketValue = dataArray[6];
                String weight = dataArray[7];
                String height = dataArray[8];
                String contractExpirationDate = dataArray[9];

                footballers.add(new Footballer( name,  nationality,  manager,  dateOfBirth,  club,  preferredFoot,
                                                marketValue,  weight,  height,  contractExpirationDate));
            }
        } catch (IOException e){
            System.out.println("Error - loading footballers to file failed.");
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
