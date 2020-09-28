package javafx;

import domain.MonthlyData;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import util.YearlyDataUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SectorDataApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, ParseException {

        final ComboBox<String> cb = new ComboBox<>();
        List<MonthlyData> monthlyDataList = initReadFile();
        ListView<MonthlyData> listView = new ListView<>();

        callListener( cb, listView,  addToComboBox( cb, monthlyDataList) );
        CreateBorderPane( cb, stage, listView );
    }

    private List<MonthlyData> initReadFile() throws IOException, ParseException {

        String fileName = "XLB.txt";
        return YearlyDataUtil.getDataFromFile(fileName);
    }

    private  Map<String, List<MonthlyData>> addToComboBox( ComboBox<String> cb, List<MonthlyData> monthlyDataList){

        cb.setPromptText("---Select a Year---");
        Map<String, List<MonthlyData>> categories = YearlyDataUtil.getYearlyDataMap( (monthlyDataList) );
        ObservableList<String> yearRanges = FXCollections.observableArrayList( categories.keySet() );
        cb.getItems().addAll( yearRanges.sorted() );
        return categories;
    }

    private void  callListener( ComboBox<String> cb, ListView<MonthlyData> listView,  Map<String,List<MonthlyData>> categories) {

        cb.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                listView.getItems().clear();
                listView.getItems().addAll(categories.get(newValue));
            }
        });
    }

    private void CreateBorderPane(ComboBox<String> cb, Stage stage, ListView<MonthlyData> listView){

        BorderPane pane = new BorderPane();
        pane.setTop(cb);
        pane.setCenter(listView);
        setStage( pane, stage);
    }

    private void setStage( BorderPane pane, Stage stage ){

        Scene scene = new Scene(pane, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Stock Sectors Yearly Data");
        stage.show();
    }
}
