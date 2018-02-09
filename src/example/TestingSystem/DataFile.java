package example.TestingSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class DataFile {

    private String name;
    private String path;
    private ObservableList<String> text = FXCollections.observableArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ObservableList<String> getText() {
        return text;
    }

    public void setText(ObservableList<String> text) {
        this.text = text;
    }

    public static DataFile[] listToArrayDF(List<File> list, ListView listView) throws Exception{ //Переводит List в массив DataFile
        DataFile[] dataFiles = new DataFile[list.size()];
        int i = 0;
        ObservableList<String> text = FXCollections.observableArrayList();
        ObservableList<String> listNames = FXCollections.observableArrayList();
        Scanner scan;
        for (File file: list
                ) {
            dataFiles[i] = new DataFile();
            scan = new Scanner(new File(file.getAbsolutePath()));
            while(scan.hasNextLine()){
                dataFiles[i].text.add(scan.nextLine());
            }
           dataFiles[i].name = (file.getName());
           dataFiles[i].path = (file.getAbsolutePath());
           listNames.add(file.getName());
           i++;
        }
        listView.setItems(listNames);
        return dataFiles;
    }

}

