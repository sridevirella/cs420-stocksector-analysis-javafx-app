package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.MonthlyData;
import model.SectorName;
import model.YearName;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static util.SectorDataUtil.getSectorsDataMap;

public class AllSectorsData {

    private HBox comboBoxLayout;
    private VBox allSectorDataVBox;

    private static ComboBox<SectorName> sectorComboBox;
    private static ComboBox<YearName> yearlyComboBox;
    private static ListView<MonthlyData> listView;
    private final Map<SectorName, Map<YearName, List<MonthlyData>>> sectorDataMap;
    private final ObservableList<SectorName> sectorNames;
    private static Map<YearName, List<MonthlyData>> yearlyMap;


    public AllSectorsData() throws IOException {

        this.sectorDataMap = getSectorsDataMap();
        this.sectorNames = FXCollections.observableArrayList(sectorDataMap.keySet());
        initSetUp();
        this.allSectorDataVBox = createVBox();
    }

    private void initSetUp() {

        setUpYearComboBox();
        setUpSectorComboBox();
        setupListView();
        handleViewNodes();
    }

    private void handleViewNodes() {

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

    private void setupListView() {
        listView = new ListView<>();
    }

    private void cellDataStringConversion(TextFieldListCell<MonthlyData> cell) {

        cell.setConverter(new StringConverter<>() {

            @Override
            public String toString(MonthlyData object) {

                if (object == null)
                    return null;
                else
                    return object.getDate() + "\n\nOpening Price: " + object.getOpenPrice() +
                            "\nClosing pPrice: " + object.getClosePrice() +
                            "\nTotal Volume: " + object.getInFlowVolume() + "\n\n\n";
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

    private void setHBoxLayout() {

        comboBoxLayout = new HBox();
        comboBoxLayout.setSpacing(20);
        comboBoxLayout.getChildren().addAll( sectorComboBox,  yearlyComboBox, listView );
        comboBoxLayout.setStyle("-fx-background-color:#D0D0D0; -fx-font-size:14");
        hBoxLayoutMargin();
    }

    private void hBoxLayoutMargin() {

        HBox.setMargin( sectorComboBox, new Insets(30, 5, 30, 100) );
        HBox.setMargin( yearlyComboBox, new Insets(30, 5, 30, 10) );
        listView.setMinHeight(500);
        listView.setMinWidth(600);
    }

    private VBox createVBox() {

        allSectorDataVBox = new VBox();

        allSectorDataVBox.getChildren().addAll( comboBoxLayout, listViewHBox() );
        allSectorDataVBox.setSpacing(50);
        return allSectorDataVBox;
    }

    private HBox listViewHBox() {

        HBox listViewHBox = new HBox();
        listViewHBox.setPadding(new Insets(0, 0, 30, 100));
        listViewHBox.getChildren().add(listView);
        return listViewHBox;
    }

    public VBox getAllSectorDataVBox() {
        return allSectorDataVBox;
    }
}