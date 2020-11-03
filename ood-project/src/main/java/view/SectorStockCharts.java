package view;

import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SectorStockCharts {

    private HBox hbox;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private ToggleGroup radioGroup;

    private PieChart gainPercentageChart;
    private BarChart yearByVolumeChart;
    private BarChart<String, Number> targetVolumeCountChart;
    private BorderPane radioGroupPane;

    public SectorStockCharts() {

        initViewsSetUp();
        getGainPercentageInstance();
        getYearByVolumeInstance();
        getTargetVolumeCountInstance();
        createPane();
    }

    private void initViewsSetUp() {

        initButtons();
        setButtonGroup();
        setUserData();
        initGroupListener();
        addToVBox();
    }

    private void getGainPercentageInstance() {

        GainPercentageChart PercentageChartInstance = new GainPercentageChart();
        this.gainPercentageChart = PercentageChartInstance.getPieChart();
    }

    private void getYearByVolumeInstance() {

        YearByVolumeChart yearByVolumeChartInstance = new YearByVolumeChart();
        this.yearByVolumeChart = yearByVolumeChartInstance.getBarChart();
    }

    private void getTargetVolumeCountInstance() {

        VolumeCountChart targetVolumeChartInstance = new VolumeCountChart();
        this.targetVolumeCountChart = targetVolumeChartInstance.getBarChart();
    }

    private void initButtons() {

        rb1 = new RadioButton("Stock Price Distribution By Gain Percentage");
        rb2 = new RadioButton("Total Inflow Volume Distribution By Yearly");
        rb3 = new RadioButton("Count of Inflow Volume With More Than $100 Million.");
        radioGroup = new ToggleGroup();
    }

    private void setButtonGroup() {

        rb1.setToggleGroup(radioGroup);
        rb2.setToggleGroup(radioGroup);
        rb3.setToggleGroup(radioGroup);
    }

    private void setUserData() {

        rb1.setUserData("button1");
        rb2.setUserData("button2");
        rb3.setUserData("button3");
    }

    private void initGroupListener() {

        radioGroup.selectedToggleProperty().addListener( (ov, old_toggle, new_toggle) -> {

            if (radioGroup.getSelectedToggle() != null)

                if(radioGroup.getSelectedToggle().getUserData() == "button1")
                    handleGainPercentageSelection();

                else if(radioGroup.getSelectedToggle().getUserData() == "button2")
                    handleYearByVolumeSelection();

                else if(radioGroup.getSelectedToggle().getUserData() == "button3")
                    handleSelection3();
        });
    }

    private void handleSelection3() {

        targetVolumeCountChart.setVisible(true);
        getRadioGroupPane().setCenter(targetVolumeCountChart);
    }

    private void handleYearByVolumeSelection() {

        yearByVolumeChart.setVisible(true);
        getRadioGroupPane().setCenter(yearByVolumeChart);
    }

    private void handleGainPercentageSelection() {

        gainPercentageChart.setVisible(true);
        getRadioGroupPane().setCenter(gainPercentageChart);
    }

    private void addToVBox() {

        VBox vbox = new VBox();
        vbox.getChildren().addAll(rb1, rb2, rb3);
        vbox.setSpacing(10);
        addToHBox(vbox);
    }

    private void addToHBox( VBox vbox ) {

        hbox = new HBox();
        hbox.getChildren().add(vbox);
        hbox.setSpacing(50);
        hbox.setPadding(new Insets(20, 10, 10, 20));
        hbox.setStyle("-fx-border-color: black");
        hbox.setStyle("-fx-background-color:grey");
    }

    private HBox getRadioGroupLayout() {

        return hbox;
    }

    private void createPane() {

        radioGroupPane = new BorderPane();
        radioGroupPane.setTop(getRadioGroupLayout());
    }

    public BorderPane getRadioGroupPane() {
        return radioGroupPane;
    }
}
