package view;

import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.stream.Stream;

public class MoreCharts {

    private VBox radioGroupVBox;
    private static ToggleGroup radioGroup;
    private static HBox hbox;
    private static HBox chartsHBox;
    private static RadioButton rb1, rb2, rb3;

    public MoreCharts() {

        this.radioGroupVBox = new VBox();
        initViewsSetUp();
    }

    private void initViewsSetUp() {

        initButtons();
        setButtonGroup();
        setUserData();
        initGroupListener();
        addToVBox();
    }

    private void initButtons() {

        rb1 = new RadioButton("Stock Price Distribution By Gain Percentage");
        rb2 = new RadioButton("Total Inflow Volume Distribution By Yearly");
        rb3 = new RadioButton("Inflow Volume Count With More Than $100 Million");
    }

    private void setButtonGroup() {

        radioGroup = new ToggleGroup();
        Stream.of(rb1, rb2, rb3).forEach(radioButton -> {
            radioButton.setStyle("-fx-font-size:14");
            radioButton.setToggleGroup(radioGroup);
        });
    }

    private void setUserData() {

        rb1.setUserData("button1");
        rb2.setUserData("button2");
        rb3.setUserData("button3");
    }

    private void initGroupListener() {

        radioGroup.selectedToggleProperty().addListener( (ov, old_toggle, new_toggle) -> {

            if (radioGroup.getSelectedToggle() != null) {
                chartsHBox.getChildren().clear();
                getSelectedChart();
            }
        });
    }

    private void getSelectedChart() {

        if (radioGroup.getSelectedToggle().getUserData() == "button1")
           chartsHBox.getChildren().add(new GainPercentagePieChart("2018,DECEMBER", "2020,OCTOBER", false).getPieChart());

        else if (radioGroup.getSelectedToggle().getUserData() == "button2")
            chartsHBox.getChildren().add(new YearByVolumeBarChart().getBarChart());

        else if (radioGroup.getSelectedToggle().getUserData() == "button3")
            chartsHBox.getChildren().add(new VolumeCountBarChart().getBarChart());
    }

    private void addToVBox() {

        VBox vbox = new VBox();
        vbox.getChildren().addAll(rb1, rb2, rb3);
        vbox.setSpacing(10);
        addToHBox(vbox);
    }

    private void addToHBox( VBox vbox ) {

        hbox = new HBox();
        hBoxProperties();
        hbox.getChildren().add(vbox);
        chartsHBoxProperties();
    }

    private void chartsHBoxProperties() {

        chartsHBox = new HBox();
        chartsHBox.setMinHeight(500);
        chartsHBox.setPadding(new Insets(40,0,0,100));
        radioGroupVBox.getChildren().addAll(hbox, chartsHBox);
    }

    private void hBoxProperties() {

        hbox.setPadding(new Insets(20, 10, 10, 100));
        hbox.setStyle("-fx-border-color: black; -fx-font-family: Courier");
        hbox.setStyle("-fx-background-color:#D0D0D0");
    }

    public VBox getRadioGroupVBox() {
        return radioGroupVBox;
    }
}
