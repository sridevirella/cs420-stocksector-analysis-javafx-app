package view;

import computedata.YearlyPriceData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.SectorName;
import model.YearName;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SectorStockCharts {

    private VBox allDataVBox;
    private static ComboBox<SectorName> sectorCB;
    private static YearlyPriceData yearlyPriceDataIns;
    private static HBox chartHBox, selectionHBox, radioGroupHBox;
    private static Map<SectorName, Map<Number, Number>> yearlyDataMap;
    private static RadioButton rb1, rb2;
    private static ToggleGroup radioGroup;
    private static ComboBox<YearName> yearCB;
    private static Map<YearName, PieChart> pieChartCollectionMap;

    public SectorStockCharts() throws IOException {

        allDataVBox = initAllDataVBox();
        yearlyPriceDataIns = new YearlyPriceData();
        initAllViews();
    }

    private void initAllViews() throws IOException {

        pieChartCollectionMap = new HashMap<>();
        initSectorCB();
        setupYearCB();
        initRadioGroup();
        initSelectionHBox();
        addAllNodesToVBox();
    }

    private VBox initAllDataVBox() {

        allDataVBox = new VBox();
        chartHBox = new HBox();
        chartHBox.setStyle("-fx-font-size:14; -fx-font-family: Courier");
        chartHBox.setPadding(new Insets(60,0,0,0));
        allDataVBox.setStyle("-fx-font-size:14");
        return allDataVBox;
    }

    private void initRadioGroup() {

        rb1 = new RadioButton("selection1 some question??");
        rb2 = new RadioButton("selection2");
        radioGroup = new ToggleGroup();
        Stream.of(rb1, rb2).forEach(radioButton -> radioButton.setToggleGroup(radioGroup));
        rb1.setUserData("rb1");
        rb2.setUserData("rb2");

        radioGroupHBox = new HBox();
        initRadioGroupListener();
    }

    private void initRadioGroupListener() {

            radioGroup.selectedToggleProperty().addListener( (ov, old_toggle, new_toggle) -> {

                if (radioGroup.getSelectedToggle() != null) {
                    if(radioGroup.getSelectedToggle().getUserData().equals("rb1")){


                        GainPercentagePieChart pieChart2008 = new GainPercentagePieChart("2008,DECEMBER", "2008,JANUARY", true);
                        pieChartCollectionMap.put( YearName.YEAR_2008, pieChart2008.getPieChart());

                        GainPercentagePieChart pieChart2020 = new GainPercentagePieChart("2020,NOVEMBER", "2020,JANUARY", true);
                        pieChartCollectionMap.put( YearName.YEAR_2020, pieChart2020.getPieChart());

                        yearCB.getItems().clear();
                        yearCB.getItems().addAll(FXCollections.observableArrayList(pieChartCollectionMap.keySet())
                                .sorted((o1, o2) -> Integer.compare(o1.getYear(), o2.getYear())));
                    } else {

                    }
                }
            });
    }

    private void setupYearCB() {

        yearCB = new ComboBox<>();
        yearCB.setPromptText("--Select a Year--");
        handleYearlyCbPrompt();
        yearCBListener();
    }

    private void yearCBListener() {

        yearCB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                radioGroupHBox.getChildren().clear();
                radioGroupHBox.getChildren().add(pieChartCollectionMap.get(newValue));
            }
        });
    }

    private VBox getRadioGroupLayout() {

        VBox radioGroupVBox = new VBox();
        radioGroupVBox.getChildren().addAll(rb1, rb2);
        radioGroupVBox.setSpacing(10);
        radioGroupVBox.setPadding(new Insets(0,0,0,250));
        return radioGroupVBox;
    }

    private void initSectorCB() throws IOException {

        sectorCB = new ComboBox<>();
        sectorCB.setPromptText("---Select a Sector---");
        sectorCB.setStyle("-fx-font-size:14; -fx-font-family: Courier");
        yearlyDataMap = yearlyPriceDataIns.getYearlyPriceData();
        sectorCB.getItems().addAll(FXCollections.observableArrayList( yearlyDataMap.keySet()).sorted((o1, o2) -> o1.getOrder() - o2.getOrder()));

        sectorCBListener();
    }

    private void addAllNodesToVBox() {

        HBox allChartsHBox = new HBox();
        selectionHBox.getChildren().add(getYearCBHBox());
        radioGroupHBox.setPadding(new Insets(60,10,10,60));
        allChartsHBox.getChildren().addAll(chartHBox, radioGroupHBox);
        allDataVBox.getChildren().addAll(selectionHBox, allChartsHBox);
    }

    private void sectorCBListener() {

        sectorCB.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    chartHBox.getChildren().clear();
                    chartHBox.getChildren().add(new YearlyPriceLineChart().addDataToLineChart(yearlyDataMap.get(newValue), newValue.name()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleYearlyCbPrompt() {

        yearCB.setButtonCell(new ListCell<>(){

            @Override
            protected  void updateItem(YearName yearName, boolean empty){
                super.updateItem(yearName, empty);
                if( empty || yearName == null )
                    setText("--Select a Year--");
                else
                    setText(yearName.toString());
            }
        });
    }

    private HBox getYearCBHBox() {

        HBox yearCBHBox = new HBox();
        yearCBHBox.getChildren().add(yearCB);
        return yearCBHBox;
    }

    private void initSelectionHBox() {

    selectionHBox = new HBox();
    selectionHBox.getChildren().addAll(sectorCB, getRadioGroupLayout());
    selectionHBox.setStyle("-fx-background-color:#D0D0D0; -fx-font-size:14");
    selectionHBox.setPadding(new Insets(20,20,20,60));
    HBox.setMargin(sectorCB, new Insets(0,0,0,80));
    selectionHBox.setSpacing(20);
    }

    public VBox getAllDataVBox() {
        return allDataVBox;
    }
}
