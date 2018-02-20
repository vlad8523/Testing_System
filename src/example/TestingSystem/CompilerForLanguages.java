package example.TestingSystem;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
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

    public static String fileName(File f){
        String[] fileName = f.getName().split("");
        int dotIndex = fileName.length;
        StringBuilder fileNameWithoutExpansion = new StringBuilder();
        for(int i = fileName.length-1;i>0;i--){
            if(!fileName[i].equals(".")){
                continue;
            }
            else{
                dotIndex = i;
                break;
            }
        }
        for(int i = 0;i<dotIndex;i++){
            fileNameWithoutExpansion.append(fileName[i]);
        }
        return fileNameWithoutExpansion.toString();
    }

    public static String fileName(String string){
        String[] fileName = string.split("");
        int dotIndex = fileName.length;
        StringBuilder fileNameWithoutExpansion = new StringBuilder();
        for(int i = fileName.length-1;i>0;i--){
            if(!fileName[i].equals(".")){
                continue;
            }
            else{
                dotIndex = i;
                break;
            }
        }
        for(int i = 0;i<dotIndex;i++){
            fileNameWithoutExpansion.append(fileName[i]);
        }
        return fileNameWithoutExpansion.toString();
    }

}
