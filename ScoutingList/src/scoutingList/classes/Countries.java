package scoutingList.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Countries {

    public Countries() {
    }

    public static ObservableList<String> getCountries(){
        ObservableList<String> countriesList = FXCollections.observableArrayList();
        Path path = Paths.get("src/scoutingList/data/countries.txt");
        String data;
        BufferedReader reader;

        try {
            reader = Files.newBufferedReader(path);
        } catch (IOException e){
            System.out.println("Error - connection to file wasn't established.");
            e.printStackTrace();
            return null;
        }

        try {
            while ((data = reader.readLine()) != null){
                countriesList.add(data);
            }
        } catch (IOException e){
            System.out.println("Error - loading countries from file failed.");
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e){
                System.out.println("Error - closing connection to file failed.");
                e.printStackTrace();
            }
        }
        return countriesList;
    }
}
