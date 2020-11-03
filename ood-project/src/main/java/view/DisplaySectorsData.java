package view;

import domain.MonthlyData;
import domain.SectorName;
import domain.YearName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static util.SectorDataUtil.getSectorsDataMap;

public class DisplaySectorsData {

    private ComboBox<SectorName> sectorComboBox;
    private ComboBox<YearName> yearlyComboBox;
    private ListView<MonthlyData> listView;
    private HBox comboBoxLayout;
    private Button chartsButton;
    private final Map<SectorName, Map<YearName, List<MonthlyData>>> sectorDataMap;
    private final ObservableList<SectorName> sectorNames;
    private Map<YearName, List<MonthlyData>> yearlyMap;
    private BorderPane comboBoxGroupPane;

    public DisplaySectorsData() throws IOException {

        this.sectorDataMap = getSectorsDataMap();
        this.sectorNames = FXCollections.observableArrayList(sectorDataMap.keySet());
        this.listView = new ListView<>();
        initSetUp();
        createPane();
    }

    private void initSetUp() {

        initChartButton();
        setUpYearComboBox();
        setUpSectorComboBox();
        yearCbListener();
        handleYearlyCbPrompt();
        setStringConverterToListView();
        setHBoxLayout();
    }

    private static class SectorNameStringConverter extends StringConverter<SectorName> {

        private final String separator = ", Symbol: ";

        @Override
        public String toString(SectorName object) {

            if( object == null )
                return null;
            else
                return object.name().substring(0,1).toUpperCase() + object.name().substring(1).toLowerCase() + separator + object.getSector();
        }

        @Override
        public SectorName fromString(String string) {

            String sectorName = string.split(separator)[1];
            List<SectorName> sectorList = Arrays.stream(SectorName.values())
                                                .filter( sn -> sn.getSector().equals(sectorName))
                                                .limit(1)
                                                .collect(Collectors.toList());

            return sectorList.isEmpty() ? null : sectorList.get(0);
        }
    }

    private void setStringConverterToListView() {

        listView.setCellFactory( lv -> {
            TextFieldListCell<MonthlyData> cell = new TextFieldListCell<>();
            cellDataStringConversion(cell);
            return cell ;
        });
    }

    private void cellDataStringConversion(TextFieldListCell<MonthlyData> cell) {

        cell.setConverter(new StringConverter<>() {

            @Override
            public String toString(MonthlyData object) {

                if (object == null)
                    return null;
                else
                    return object.getDate() + "\nOpening price: " + object.getOpenPrice() +
                            "\nClosing price: " + object.getClosePrice() + "\n\n\n";
            }

            @Override
            public MonthlyData fromString(String string) {
                return null;
            }

        });
    }

    private void setUpSectorComboBox() {

        sectorComboBox = new ComboBox<>();
        sectorComboBox.getItems().addAll( sortSectors() );
        sectorComboBox.setPromptText("---Select a Sector---");
        sectorComboBox.setConverter(new SectorNameStringConverter());
        sectorCbListener();
    }

    private ObservableList<SectorName> sortSectors() {
        return sectorNames.sorted((o1, o2) -> o1.getOrder() - o2.getOrder());
    }

    private void sectorCbListener() {

        sectorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            if( newValue != null ) {
                listView.setVisible(false);
                addSelectedSectorData( newValue );
            }
        });
    }

    private void addSelectedSectorData( SectorName selectedSector ) {

        addYearlyDataToComboBox(selectedSector);
        yearlyMap = sectorDataMap.get(selectedSector);
    }

    private void setUpYearComboBox() {

        yearlyComboBox = new ComboBox<>();
        yearlyComboBox.setPromptText("---Select a Year---");
        yearlyComboBox.setVisible(true);
    }

    private void handleYearlyCbPrompt() {

        yearlyComboBox.setButtonCell(new ListCell<>(){

            @Override
            protected  void updateItem(YearName yearName, boolean empty){
                super.updateItem(yearName, empty);
                if( empty || yearName == null )
                    setText("---Select a Year---");
                else
                    setText(yearName.toString());
            }
        });
    }

    private void addYearlyDataToComboBox( SectorName selectedSector ) {

        yearlyComboBox.getItems().clear();
        ObservableList<YearName> yearRanges = FXCollections.observableArrayList( sectorDataMap.get(selectedSector).keySet() );
        yearlyComboBox.getItems().addAll( sortYears(yearRanges));
    }

    private ObservableList<YearName> sortYears(ObservableList<YearName> yearRanges) {
        return yearRanges.sorted((o1, o2) -> Integer.compare(o1.getYear(), o2.getYear()));
    }

    private void  yearCbListener() {

        yearlyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                listView.getItems().clear();
                listView.getItems().addAll(sortMonths(yearlyMap.get(newValue)));
                listView.setVisible(true);
            }
        });
    }

    private List<MonthlyData> sortMonths(List<MonthlyData> monthlyDataList) {

        monthlyDataList.sort(this::compareMonths);
        return monthlyDataList;
    }

    private int compareMonths(MonthlyData o1, MonthlyData o2) {

        SimpleDateFormat format = new SimpleDateFormat("MMM", Locale.US );

        try {
            return format.parse(o1.getDate().split(",")[1]).compareTo(format.parse(o2.getDate().split(",")[1]));

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void initChartButton() {
        chartsButton = new Button();
        chartsButton.setText("Click here to query more data");
    }

    private void setHBoxLayout() {

        comboBoxLayout = new HBox();
        comboBoxLayout.setSpacing(50);
        comboBoxLayout.getChildren().addAll( sectorComboBox,  yearlyComboBox, listView, chartsButton );
        HBox.setMargin( sectorComboBox, new Insets(30, 5, 30, 10) );
        HBox.setMargin( yearlyComboBox, new Insets(30, 5, 30, 10) );
        HBox.setMargin( chartsButton, new Insets(30, 5, 30, 10) );
        comboBoxLayout.setStyle("-fx-background-color:grey");
    }

    private void createPane() {

        comboBoxGroupPane = new BorderPane();
        comboBoxGroupPane.setTop( comboBoxLayout );
        comboBoxGroupPane.setCenter( listView );
    }

    public BorderPane getComboBoxGroupPane() {
        return comboBoxGroupPane;
    }

    public Button getChartsButton() {
        return chartsButton;
    }
}