package example.TestingSystem;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.InputStream;
import java.util.Scanner;

public class CompilerForLanguages {

    public static ObservableList<String> compileForPython(String testPath, String interPath, String programPath) throws Exception{ //Метод который прогоняет один тест в консоли

        ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c", "type \""+testPath+"\" | \"" + interPath + "\" \"" + programPath + "\"");
        Process process = builder.start();
        InputStream inputStream = process.getInputStream();

        Scanner scan = new Scanner(inputStream).useDelimiter("\\A");
        ObservableList<String> strings = FXCollections.observableArrayList();
        while(scan.hasNext()){
           strings.add(scan.nextLine());
        }

        return strings;
    }

}
