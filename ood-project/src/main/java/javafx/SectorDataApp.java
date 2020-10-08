package javafx;

import domain.MonthlyData;
import domain.SectorName;
import domain.YearName;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import util.SectorDataUtil;
import util.SectorNameComparator;
import util.YearlyNameComparator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SectorDataApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, ParseException {

        final ComboBox<SectorName> sectorComboBox = new ComboBox<>();
        final ComboBox<YearName> yearlyComboBox = new ComboBox<>();
        ListView<MonthlyData> listView = new ListView<>();

        initSetup( sectorComboBox, yearlyComboBox, listView, stage );
    }

    private void initSetup( ComboBox<SectorName> sectorComboBox, ComboBox<YearName> yearlyComboBox,
                            ListView<MonthlyData> listView, Stage stage ) throws IOException, ParseException {

        setComboBoxPrompt( sectorComboBox, yearlyComboBox );
        sectorCbListener( sectorComboBox, yearlyComboBox, addSectorData( sectorComboBox ) );
        yearCbListener( yearlyComboBox, listView );
        createBorderPane( yearlyComboBox, sectorComboBox, stage, listView );
    }

    private void sectorCbListener( ComboBox<SectorName> scb, ComboBox<YearName> ycb,
                                   Map<SectorName, Map<YearName,List<MonthlyData>>> smd ) {

        scb.valueProperty().addListener(new ChangeListener<SectorName>() {
            @Override
            public void changed( ObservableValue<? extends SectorName> observable, SectorName oldValue, SectorName newValue ) {

                if( newValue != null ) {
                    addDataToYearlyComboBox( ycb, smd, newValue );
                }
            }
        });
    }

    private void addDataToYearlyComboBox( ComboBox<YearName> ycb, Map<SectorName, Map<YearName,List<MonthlyData>>> smd, SectorName newValue ) {

        ycb.getItems().clear();
        addYearlyData(ycb, smd.get(newValue));
        SectorDataUtil.setSelectedSectorYearlyData(smd.get(newValue));
    }

    private  Map<SectorName, Map<YearName,List<MonthlyData>>> addSectorData( ComboBox<SectorName> scb ) throws IOException, ParseException {

        Map<SectorName, Map<YearName, List<MonthlyData>>> sectors = SectorDataUtil.getSectorsDataMap();
        ObservableList<SectorName> sectorNames = FXCollections.observableArrayList( sectors.keySet() );
        scb.getItems().addAll( sectorNames.sorted(new SectorNameComparator()));
        return sectors;
    }

    private void  yearCbListener( ComboBox<YearName> cb, ListView<MonthlyData> listView ) {

        cb.valueProperty().addListener(new ChangeListener<YearName>() {
            @Override
            public void changed(ObservableValue<? extends YearName> observable, YearName oldValue, YearName newValue) {

                if (newValue != null) {

                    listView.getItems().clear();
                    listView.getItems().addAll(SectorDataUtil.getSelectedSectorYearlyData().get(newValue));
                }
            }
        });
    }

    private  void addYearlyData(ComboBox<YearName> cb, Map<YearName, List<MonthlyData>> mdl ) {

        ObservableList<YearName> yearRanges = FXCollections.observableArrayList( mdl.keySet() );
        cb.getItems().addAll( yearRanges.sorted(new YearlyNameComparator()));
    }


    private void createBorderPane(ComboBox<YearName> ycb, ComboBox<SectorName> scb, Stage stage, ListView<MonthlyData> listView) {

        BorderPane pane = new BorderPane();
        FlowPane flowPane = new FlowPane(30.0, 20.0, scb, ycb );
        pane.setTop( flowPane );
        pane.setCenter( listView );
        setStage( pane, stage );
    }

    private void setStage( BorderPane pane, Stage stage ){

        Scene scene = new Scene(pane, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Stock Sectors Yearly Data");
        stage.show();
    }

    private void setComboBoxPrompt( ComboBox<SectorName> scb, ComboBox<YearName> ycb) {
        scb.setPromptText("---Select a Sector---");
        ycb.setPromptText("---Select a Year---");
    }
}
