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
import java.util.List;


public class Controller{
    private File file;
    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    @FXML
    private TextField programPath,compilatorPath;
    @FXML
    private ListView results,tests,results1,resultsFiles,testText,resultText,resultsProgramText,results1Text;
    @FXML
    private ScrollPane scTests,scResults;
    @FXML
    private ComboBox language;
    @FXML
    private Label mark;
    @FXML
    private ProgressBar progressMark;
    @FXML
    private Button buttonForCompilator,buttonForDirectory;
    @FXML
    private Label errProg,errComp;
    private Thread mThread;

    private int complete;

    private DataFile[] result,test, resultsProgram;

    public void openFilePath(ActionEvent actionEvent) { //
        file = fileChooser.showOpenDialog(new Stage());
        if(file!=null){
            Button button = (Button) actionEvent.getSource();
            switch (button.getId()){
                case "buttonForProgram":
                    programPath.setText(file.getAbsolutePath());
                    break;
                case "buttonForCompilator":
                    compilatorPath.setText(file.getAbsolutePath());
                    break;
            }

        }
    }

    public void addItems(ActionEvent actionEvent) throws Exception { //Метод для добавления элементов в ListView
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if(files!=null){
            javafx.scene.control.Button button = (javafx.scene.control.Button) actionEvent.getSource();
            switch (button.getId()){
                case "addTests": //Тесты
                    mThread = new Thread(() -> {
                        try {
                            test = DataFile.listToArrayDF(files, tests);
                        }
                        catch(Exception ex){
                            System.err.println(ex.getMessage());
                        }
                    });
                    mThread.start();
                    break;
                case "addResults": //Эталон
                    mThread = new Thread(()-> {
                        try {
                            result = DataFile.listToArrayDF(files, results);
                            results1.setItems(results.getItems());
                        }
                        catch (Exception ex){
                            System.err.println(ex.getMessage());
                        }
                    });
                    mThread.start();
                    break;
            }
        }
    }

    public void start(ActionEvent actionEvent) throws Exception { /*Проверяет на пустоту все нужные объекты,
        через foreach запускает метод для компиляции кода и сохраняет все в массив ответов, сверяет все с эталоном*/
        ObservableList<String> nameList = FXCollections.observableArrayList();
        int iter = 0;
        if(compilatorPath.getText()!=null&&
                programPath.getText()!=null&&
                tests!=null&&
                results!=null&&
                result.length==test.length){
        switch (language.getValue().toString()){
            case "Python":
                    for (DataFile testFile:test
                         ) {
                        resultsProgram = new DataFile[test.length];
                        resultsProgram[iter] = new DataFile();
                        resultsProgram[iter].setName(testFile.getName());
                        nameList.add(testFile.getName());
                        resultsProgram[iter].setText(CompilerForLanguages.compileForPython(testFile.getPath(),
                                compilatorPath.getText(),
                                programPath.getText()));
                        iter++;
                    }
                    for (int iterator = 0; iterator< resultsProgram.length; iterator++){
//                        if(resultsProgram[iterator].getText().equals(result[iterator].getText())) {
//                            resultsProgram[iterator].setName(resultsProgram[iterator].getName()+":Правильно");
//                            complete++;
//                        }
//
//                        else  {
//                            resultsProgram[iterator].setName(resultsProgram[iterator].getName()+":Неправильно");
//                        }
                        System.out.println(resultsProgram[iterator].getText());
                        System.out.println(result[iterator].getText());
                    }
                    resultsFiles.setItems(nameList);
                    mark.setText(String.format("%.1f",(double)complete/ resultsProgram.length*10));
                    progressMark.setProgress((double) complete/ resultsProgram.length*100);
                    nameList.removeAll();
                    break;
                case "Java":
                    break;
            }
        }
        else{
            if (programPath.getText()!=null) {
                file = new File(programPath.getText());
                if (!file.exists()) {
                    errProg.setText("Такого файла не существует.");
                }
            }
            else{
                errProg.setText("Вы не прописали путь.");
            }
            if (compilatorPath.getText()!=null){
                file = new File(compilatorPath.getText());
                file = new File(programPath.getText());
                if (!file.exists()) {
                    errProg.setText("Такого файла не существует.");
                }
            }
            else{
                errProg.setText("Вы не прописали путь.");
            }
        }
    }

    public void onSwitchLanguage(ActionEvent actionEvent) {
        ComboBox cb = (ComboBox) actionEvent.getSource();
        switch (cb.getValue().toString()){
            case "Java":
                buttonForDirectory.setVisible(true);
                buttonForCompilator.setVisible(false);
                break;
            default:
                buttonForCompilator.setVisible(true);
                buttonForDirectory.setVisible(false);
                break;
        }
    }

    public void onSwitchFile(MouseEvent mouseEvent) { //Метод для показа содержимого выбранного файла
        ListView listView =(ListView) mouseEvent.getSource();
        switch(listView.getId()){
            case "tests":
                if (test!=null) testText.setItems(test[listView.getSelectionModel().getSelectedIndex()].getText());
                break;
            case "results":
                if (result!=null)resultText.setItems(result[listView.getSelectionModel().getSelectedIndex()].getText());
                break;
            case "results1" :
                if (results1!=null)results1Text.setItems(result[listView.getSelectionModel().getSelectedIndex()].getText());
                break;
            case "resultsProgram":
                if (resultsFiles!=null)resultsProgramText.setItems(resultsProgram[listView.getSelectionModel().getSelectedIndex()].getText());
                break;

        }
    }

    public void openPackage(ActionEvent actionEvent) {
        file = directoryChooser.showDialog(new Stage());
        if (file!=null){
            compilatorPath.setText(file.getAbsolutePath());
        }
    }
}
