/**
 * Created by Varun Venkateshan and Yashwant Balaji
 * */

package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import songObj.song;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

public class Controller {

    Stage mainStage;

    @FXML private TextField songDisplay;
    @FXML private TextField inputSong;
    @FXML private TextField inputArtist;
    @FXML private TextField inputAlbum;
    @FXML private TextField inputYear;
    @FXML private Button add;
    @FXML private Button edit;
    @FXML private Button delete;
    @FXML ListView<song> listView;
    private ObservableList<song> obsList;


    /**
     * Start Method
     */
    public void start(Stage mainStage) {

        ObservableList<song> obsList = FXCollections.observableArrayList();
        addToFile(obsList);

        Collections.sort(obsList, (song1, song2) -> {
            int ret = song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());

            if (ret != 0) {
                return ret;
            } else {
                return song1.getArtist().toLowerCase().compareTo(song2.getArtist().toLowerCase());
            }
        });
        listView.setItems(obsList);
        listView.setCellFactory(param -> new ListCell<song>() {
            protected void updateItem(song item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getTitleAndArtist() == null) {
                    setText(null);
                } else {
                    setText(item.getTitleAndArtist());
                }
            }
        });
        if (!obsList.isEmpty()) {
            listView.getSelectionModel().selectFirst();
            selectedSong();
        }
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldvalue, newvalue) -> selectedSong());

    }

    /**
     * Display Song Details of Selected Song
     */
    public void selectedSong() {
        if (listView.getSelectionModel().getSelectedIndex() < 0) {
            return;
        }
        song temp = listView.getSelectionModel().getSelectedItem();
        if (temp != null) {
            String title = temp.getTitle();
            String artist = temp.getArtist();
            String album = temp.getAlbum();
            String year = temp.getYear();

            songDisplay.setText("Song: "+ title + " Artist: " + artist +
                    "   Album: " + album + "    Year: " + year);
        }

    }

    /**
     * Method to Add Songs to the File for Persistence
     */
    public void addToFile(ObservableList<song> obsList) {
        Path filePath = Paths.get("src/songLib.txt");
        boolean isPresent = (new File(String.valueOf(filePath))).exists();

        try {
            if (!isPresent) {
                File playlist = new File("src/songLib.txt");
                playlist.createNewFile();
                return;
            }
            Scanner s = new Scanner(filePath);
            while (s.hasNextLine()) {
                String title = s.nextLine();
                String artist = s.nextLine();
                String album = s.nextLine();
                String year = s.nextLine();

                obsList.add(new song(title, artist, album, year));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add
     */
    @FXML
    private void addToList(ActionEvent event) {
        int index = -1;

        String title = inputSong.getText().trim();
        String artist = inputArtist.getText().trim();
        String album = inputAlbum.getText().trim();
        String year = inputYear.getText().trim();

        //Year is not a positive number
        if(!isInteger(year)){
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setHeaderText("Year needs to be a positive integer");
            alert.setContentText("Please re-enter a valid year");
            alert.showAndWait();
            return;
        }
        //Invalid '|' Character
        if(containsVertBar(title,artist,album,year)){
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setHeaderText("Character not allowed");
            alert.setContentText("Please re-enter a valid input");
            alert.showAndWait();
            return;
        }
        //Missing Song Info
        if (title.equals("") || artist.equals("")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Fill all the boxes");
            alert.setHeaderText("Parameter missing");
            alert.setContentText("Please enter all song details");
            alert.showAndWait();
            return;
        }
        //Confirmation to Add Song
        Alert confirmation = new Alert(AlertType.CONFIRMATION, "CONFIRM", ButtonType.YES, ButtonType.CANCEL);
        confirmation.setTitle("Add Song");
        confirmation.setContentText("Are you sure?");
        confirmation.showAndWait();
        if (confirmation.getResult() == ButtonType.CANCEL) {
            return;
        }
        ObservableList<song> obsList = FXCollections.observableArrayList();
        addToFile(obsList);


        for (song curr : obsList) {
            String a = curr.getTitle();
            String b = curr.getArtist();

            if ((a.equalsIgnoreCase(title)) && (b.equalsIgnoreCase(artist))) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(mainStage);
                alert.setTitle("Error");
                alert.setHeaderText("This song is already present in the playlist");
                alert.setContentText("Please enter songs not included in playlist");
                alert.showAndWait();
                return;
            }
        }

        obsList.add(new song(title, artist, album, year));
        try {
            FileWriter f = new FileWriter("src/songLib.txt", true);
            f.write(title + "\n" + artist + "\n" + album + "\n" + year + "\n");
            f.flush();
            f.close();
        } catch (Exception e) {

        }

        obsList.sort((song1, song2) -> {
            int ret = song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());

            if (ret != 0) {
                return ret;
            } else {
                return song1.getArtist().toLowerCase().compareTo(song2.getArtist().toLowerCase());
            }
        });
        listView.setItems(obsList);

        for (song curr : obsList) {
            String a = curr.getTitle();
            String b = curr.getArtist();
            String c = curr.getAlbum();
            String d = curr.getYear();
            if (a.equals(title) && b.equals(artist) && c.equals(album) && d.equals(year)) {
                index = obsList.indexOf(curr);
                break;
            }
        }
        listView.getSelectionModel().select(index);
    }


    @FXML
    private void editSong(ActionEvent event) {


        song temp = listView.getSelectionModel().getSelectedItem();
        int idx = listView.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            return;
        }
        String title = temp.getTitle();
        String artist = temp.getArtist();
        String album = temp.getAlbum();
        String year = temp.getYear();

        String newTitle = inputSong.getText().trim();
        String newArtist = inputArtist.getText().trim();
        String newAlbum = inputAlbum.getText().trim();
        String newYear = inputYear.getText().trim();

        //Year is not a positive number
        if(!isInteger(year)){
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setHeaderText("Year needs to be a positive integer");
            alert.setContentText("Please re-enter a valid year");
            alert.showAndWait();
            return;
        }
        //Invalid '|' Character
        if(containsVertBar(title,artist,album,year)){
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Error");
            alert.setHeaderText("Character not allowed");
            alert.setContentText("Please re-enter a valid input");
            alert.showAndWait();
            return;
        }
        //Missing Song Info
        if (title.equals("") || artist.equals("")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainStage);
            alert.setTitle("Fill all the boxes");
            alert.setHeaderText("Parameter missing");
            alert.setContentText("Please enter all song details");
            alert.showAndWait();
            return;
        }
        //Confirmation of Edits
        Alert confirmation = new Alert(AlertType.CONFIRMATION, "CONFIRM", ButtonType.YES, ButtonType.CANCEL);
        confirmation.setTitle("Edit Song");
        confirmation.setContentText("Are you sure?");
        confirmation.showAndWait();
        if (confirmation.getResult() == ButtonType.CANCEL) {
            return;
        }
        ObservableList<song> obsList = FXCollections.observableArrayList();
        addToFile(obsList);
        for (song curr : obsList) {
            String a = curr.getTitle();
            String b = curr.getArtist();

            if ((a.equalsIgnoreCase(newTitle)) && (b.equalsIgnoreCase(newArtist))) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(mainStage);
                alert.setTitle("Error");
                alert.setHeaderText("This song is already present in the playlist");
                alert.setContentText("Please enter songs not included in playlist");
                alert.showAndWait();
                return;
            }
        }
        Iterator<song> obit = obsList.iterator();

        while (obit.hasNext()) {
            song curr = obit.next();
            String a = curr.getTitle();
            String b = curr.getArtist();
            String c = curr.getAlbum();
            String d = curr.getYear();

            if (a.equals(title) && b.equals(artist) && c.equals(album) && d.equals(year)) {
                curr.setTitle(newTitle);
                curr.setArtist(newArtist);
                curr.setAlbum(newAlbum);
                curr.setYear(newYear);
                listView.getSelectionModel().select(curr);
                break;
            }
        }

        //Edit the text file for persistence
        try {
            FileWriter f = new FileWriter("src/songLib.txt");
            for (song curr : obsList) {
                String a = curr.getTitle();
                String b = curr.getArtist();
                String c = curr.getAlbum();
                String d = curr.getYear();

                f.write(a + "\n" + b + "\n" + c + "\n" + d + "\n");
            }
            f.flush();
            f.close();
        } catch (Exception e) {

        }

        Collections.sort(obsList, (song1, song2) -> {
            int ret = song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());

            if (ret != 0) {
                return ret;
            } else {
                return song1.getArtist().toLowerCase().compareTo(song2.getArtist().toLowerCase());
            }
        });
        listView.setItems(obsList);
        selectedSong();
    }

    /**
     * Delete Method
     */
    @FXML
    private void delete(ActionEvent event) {
        song placeHolder = listView.getSelectionModel().getSelectedItem();
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            return;
        }
        String title = placeHolder.getTitle();
        String artist = placeHolder.getArtist();
        String album = placeHolder.getAlbum();
        String year = placeHolder.getYear();

        Alert confirmation = new Alert(AlertType.CONFIRMATION, "CONFIRM", ButtonType.YES, ButtonType.CANCEL);
        confirmation.setTitle("Delete Song");
        confirmation.setContentText("Are you sure?");
        confirmation.showAndWait();
        if (confirmation.getResult() == ButtonType.CANCEL) {
            return;
        }

        ObservableList<song> obsList = FXCollections.observableArrayList();
        addToFile(obsList);
        Iterator<song> obit = obsList.iterator();

        //Remove song details from the persistence file
        while (obit.hasNext()) {
            song curr = obit.next();
            String a = curr.getTitle();
            String b = curr.getArtist();
            String c = curr.getAlbum();
            String d = curr.getYear();
            if (a.equals(title) && b.equals(artist) && c.equals(album) && d.equals(year)) {
                obit.remove();
                break;
            }
        }

        try {
            FileWriter f = new FileWriter("src/songLib.txt");
            for (song curr : obsList) {
                String a = curr.getTitle();
                String b = curr.getArtist();
                String c = curr.getAlbum();
                String d = curr.getYear();
                f.write(a + "\n" + b + "\n" + c + "\n" + d + "\n");
            }
            f.flush();
            f.close();
        } catch (Exception e) {

        }
        obsList.sort((song1, song2) -> {
            int ret = song1.getTitle().toLowerCase().compareTo(song2.getTitle().toLowerCase());

            if (ret != 0) {
                return ret;
            } else {
                return song1.getArtist().toLowerCase().compareTo(song2.getArtist().toLowerCase());
            }
        });
        listView.setItems(obsList);

        if (obsList.size() == 0) {
            songDisplay.setText("");
        } else if (index < obsList.size() - 1) {
            listView.getSelectionModel().select(index++);
        } else {
            listView.getSelectionModel().select((obsList.size() - 1));
        }
    }

    /**
     * Helper Methods to Check for '|' and if entry is an integer
     */
    public boolean containsVertBar(String a, String b, String c, String d){
        return a.contains("|") || b.contains("|") || c.contains("|") || d.contains("|");
    }
    public static boolean isInteger(String s) {

        if(s == ""){
            return true;
        }

        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        return Integer.parseInt(s) >= 0;
    }
}