package example.TestingSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Controller{
    private File file;
    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    @FXML
    private TextField programPath,compilatorPath;
    @FXML
    private ListView resultsLV,tests, results1LV,resultsFiles,testText,resultText,resultsProgramText,results1Text;
    @FXML
    private ComboBox language;
    @FXML
    private Label mark;
    @FXML
    private ProgressBar progressMark;
    @FXML
    private Button buttonForCompilator,buttonForDirectory;
    @FXML
    private Label errorLog;
    List<File> files;

    DataFile userProgram = new DataFile(),languageProgram = new DataFile();

    private Map<String,Language> languageMap = new HashMap<>();

    private int complete;

    private DataFile[] result,test, resultsProgram;

    public void openFilePath(ActionEvent actionEvent) { //
        file = fileChooser.showOpenDialog(new Stage());
        if(file!=null){
            Button button = (Button) actionEvent.getSource();
            switch (button.getId()){
                case "buttonForProgram":
                    programPath.setText(file.getAbsolutePath());
                    userProgram.setPath(file.getPath());
                    break;
                case "buttonForCompilator":
                    compilatorPath.setText(file.getAbsolutePath());
                    languageProgram.setPath(file.getPath());
                    break;
            }
        }
    }

    public void addItems(ActionEvent actionEvent) throws Exception { //Метод для добавления элементов в ListView
         files = fileChooser.showOpenMultipleDialog(new Stage());
        if(files!=null){
            javafx.scene.control.Button button = (javafx.scene.control.Button) actionEvent.getSource();
            switch (button.getId()){
                case "addTests": //Тесты
                    try {
                        test = DataFile.listToArrayDF(files, tests);
                    }
                    catch(Exception ex){
                        errorLog.setText("Ошибка");
                    }
                    break;
                case "addResults": //Эталон
                    try {
                        result = DataFile.listToArrayDF(files, resultsLV);
                        results1LV.setItems(resultsLV.getItems());
                    }
                    catch (Exception ex){
                        errorLog.setText("Ошибка");
                    }
                    break;
            }
        }
    }

    public void start(ActionEvent actionEvent) throws Exception { /*Проверяет на пустоту все нужные объекты,
        через foreach запускает метод для компиляции кода и сохраняет все в массив ответов, сверяет все с эталоном*/
        ObservableList<String> nameList = FXCollections.observableArrayList();

        if(compilatorPath.getText()!=null&&
                programPath.getText()!=null&&
                tests!=null&&
                resultsLV !=null&&
                result!=null&&
                test!=null&&
                language.getValue()!=null) {
            if (result.length == test.length) {
                complete = 0;
                for (int iterator = 0; iterator < test.length; iterator++) {
                    resultsProgram = new DataFile[test.length];
                    resultsProgram[iterator] = new DataFile();
                    resultsProgram[iterator].setText(languageMap.get(language.getValue().toString()).resultProg(test[iterator],
                            languageProgram,
                            userProgram));
                    if (resultsProgram[iterator].getText().
                            equals(result[iterator].getText())) {

                        resultsProgram[iterator].setName(test[iterator].getName() + ":Правильно");
                        nameList.add(resultsProgram[iterator].getName());
                        complete++;

                    } else {

                        resultsProgram[iterator].setName(test[iterator].getName() + ":Неправильно");
                        nameList.add(resultsProgram[iterator].getName());
                    }
                }
                resultsFiles.setItems(nameList);
                mark.setText(String.format("%.1f", (double) complete / resultsProgram.length * 10));
                progressMark.setProgress((double) complete / resultsProgram.length);
                nameList.removeAll();
                errorLog.setText("");
            } else {
                errorLog.setText("Ошибка");
            }
        }
            else{
            errorLog.setText("Ошибка");
        }
    }

    public void onSwitchLanguage(ActionEvent actionEvent) {
        ComboBox cb = (ComboBox) actionEvent.getSource();

        switch (cb.getValue().toString()){
            case "Java":
                buttonForDirectory.setVisible(true);
                buttonForCompilator.setVisible(false);
                compilatorPath.setPromptText("Путь до jdk");
                break;
            default:
                buttonForCompilator.setVisible(true);
                buttonForDirectory.setVisible(false);
                compilatorPath.setPromptText("Путь до компилятора/интерпретатора");
                break;
        }
    }

    public void onSwitchFile(MouseEvent mouseEvent) { //Метод для показа содержимого выбранного файла
        ListView listView =(ListView) mouseEvent.getSource();
        switch(listView.getId()){
            case "tests":
                if (test!=null) testText.setItems(test[listView.getSelectionModel().getSelectedIndex()].getText());
                break;
            case "resultsLV":
                if (result!=null)resultText.setItems(result[listView.getSelectionModel().getSelectedIndex()].getText());
                break;
            case "results1LV" :
                if (results1LV !=null)results1Text.setItems(result[listView.getSelectionModel().getSelectedIndex()].getText());
                break;
            case "resultsFiles":
                if (resultsFiles!=null){
                    if(resultsProgram[listView.getSelectionModel().getSelectedIndex()]!=null)resultsProgramText.setItems(resultsProgram[listView.getSelectionModel().getSelectedIndex()].getText());
                    else resultsProgramText.setItems(result[listView.getSelectionModel().getSelectedIndex()].getText());
                }
                break;

        }
    }

    public void initialize(){
        ObservableList<String> languageList = FXCollections.observableArrayList();
        languageMap.clear();

        languageMap.put("Python",new Language());
        languageMap.get("Python").setCanInterpreted(true);
//        languageMap.put("Pascal",new Language());
//        languageMap.get("Pascal").setCanCompiling(true);
//        languageMap.put("Java",new Language());
//        languageMap.get("Java").setJava(true);
//        languageMap.put("Exe",new Language());
//        languageMap.get("Exe").setExe(true);
        for (String language: languageMap.keySet()
             ) {
            languageList.add(language);
        }

        language.setItems(languageList);
    }

    public void openPackage(ActionEvent actionEvent) {
        file = directoryChooser.showDialog(new Stage());
        if (file!=null){
            compilatorPath.setText(file.getAbsolutePath());
        }
    }
}
