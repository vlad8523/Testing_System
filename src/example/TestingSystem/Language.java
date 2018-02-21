package example.TestingSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;


public class Language {
    private boolean canCompiling = false;
    private boolean canInterpreted = false;
    private boolean isExe = false;
    private boolean isJava = false;

    public boolean isJava() {
        return isJava;
    }

    public void setJava(boolean java) {
        isJava = java;
    }

    public boolean isCanCompiling() {
        return canCompiling;
    }

    public void setCanCompiling(boolean canCompiling) {
        this.canCompiling = canCompiling;
    }

    public boolean isCanInterpreted() {
        return canInterpreted;
    }

    public void setCanInterpreted(boolean canInterpreted) {
        this.canInterpreted = canInterpreted;
    }

    public boolean isExe() {
        return isExe;
    }

    public void setExe(boolean exe) {
        isExe = exe;
    }

    public ObservableList<String>  resultProg (DataFile test, DataFile lang, DataFile program) throws Exception{
        ObservableList<String> text = FXCollections.observableArrayList();

        if (canInterpreted){
            text = interpretate(test,lang,program);
        }

        if (isExe){
            text = runExe(test, program.getPath());
        }

        if (canCompiling){
            File checkFile = new File(fileName(program.getPath())+".exe");
            if(!checkFile.exists()) compile(lang, program);
            text = runExe(test,fileName(program.getPath())+".exe");
        }

        if (isJava){
            File checkFile = new File(fileName(program.getPath())+".class");
            if(!checkFile.exists()) compilingJava(lang, program);
            text = runJava(test, lang, program);
        }

        return text;
    }

    public ObservableList<String> runJava(DataFile test, DataFile lang, DataFile program) throws Exception{
        ObservableList<String> text = FXCollections.observableArrayList();
        Process process = new ProcessBuilder("cmd.exe","/c","cd " + fileRepository(program.getPath()) , "type \""+test.getPath()+"\" | \"" + lang.getPath() + "\\bin\\java.exe" + "\" \"" + fileName(program.getPath()) + "\"").start();
        InputStream inputStream = process.getInputStream();

        Scanner scan = new Scanner(inputStream).useDelimiter("\\A");
        while(scan.hasNext()){
            text.add(scan.nextLine());
        }

        return text;
    }
    
    public void compilingJava(DataFile lang, DataFile program) throws Exception{
        Process process = new ProcessBuilder("cmd.exe","/c", "\"" + lang.getPath() + "\\bin\\javac.exe" + "\" \"" + program.getPath() + "\"").start();
        while(process.isAlive())continue;
    }

    public void compile (DataFile lang, DataFile program) throws Exception{
        Process process = new ProcessBuilder("cmd.exe","/c", "\"" + lang.getPath() + "\" \"" + program.getPath() + "\"").start();
        while(process.isAlive())continue;
    }

    public ObservableList<String> interpretate(DataFile test, DataFile lang, DataFile program) throws Exception{
        ObservableList<String> text = FXCollections.observableArrayList();
        Process process = new ProcessBuilder("cmd.exe","/c", "type \""+test.getPath()+"\" | \"" + lang.getPath() + "\" \"" + program.getPath() + "\"").start();
        InputStream inputStream = process.getInputStream();

        Scanner scan = new Scanner(inputStream).useDelimiter("\\A");
        while(scan.hasNext()){
            text.add(scan.nextLine());
        }

        return text;
    }

    public ObservableList<String> runExe(DataFile test,String program) throws Exception{
        ObservableList<String> text = FXCollections.observableArrayList();
        Process process = new ProcessBuilder("cmd.exe","/c", "type \""+test.getPath()+"\" | " + "\"" + program + "\"").start();
        InputStream inputStream = process.getInputStream();

        Scanner scan = new Scanner(inputStream).useDelimiter("\\A");
        while(scan.hasNext()){
            text.add(scan.nextLine());
        }

        return text;
    }

    public String fileName(String string){
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
    
    public String fileRepository(String string){
        String[] fileName = string.split("");
        int slashIndex = fileName.length;
        StringBuilder fileRepository = new StringBuilder();
        for(int i = fileName.length-1;i>0;i--){
            if(!fileName[i].equals("/")){
                continue;
            }
            else{
                slashIndex = i;
                break;
            }
        }
        for(int i = 0;i<slashIndex;i++){
            fileRepository.append(fileName[i]);
        }
        return fileRepository.toString();
        
    }
}
