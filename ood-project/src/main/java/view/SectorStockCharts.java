package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SectorStockCharts {

    private BorderPane radioGroupPane;
    private Button homeButton;
    private static ToggleGroup radioGroup;
    private static HBox hbox;
    private static RadioButton rb1;
    private static RadioButton rb2;
    private static RadioButton rb3;

    public SectorStockCharts() {

        this.radioGroupPane = new BorderPane();
        this.homeButton = initHomeButton();
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

    private Button initHomeButton() {

        homeButton = new Button();
        homeButton.setText("Back to Main Screen");
        homeButton.setPadding(new Insets(5, 10, 5, 10));
        return homeButton;
    }

    private void setButtonGroup() {

        radioGroup = new ToggleGroup();
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
                getSelectedChart();
        });
    }

    private void getSelectedChart() {

        if (radioGroup.getSelectedToggle().getUserData() == "button1")
            getRadioGroupPane().setCenter(new GainPercentageChart().getPieChart());

        else if (radioGroup.getSelectedToggle().getUserData() == "button2")
            getRadioGroupPane().setCenter(new YearByVolumeChart().getBarChart());

        else if (radioGroup.getSelectedToggle().getUserData() == "button3")
            getRadioGroupPane().setCenter(new VolumeCountChart().getBarChart());
    }

    private void addToVBox() {

        VBox vbox = new VBox();
        vbox.getChildren().addAll(rb1, rb2, rb3);
        vbox.setSpacing(10);
        addToHBox(vbox);
    }

    private void addToHBox( VBox vbox ) {

        hbox = new HBox();
        hbox.getChildren().addAll(vbox, homeButton);
        hBoxProperties();
        radioGroupPane.setTop(hbox);
    }

    private void hBoxProperties() {

        hbox.setSpacing(200);
        hbox.setPadding(new Insets(20, 10, 10, 20));
        hbox.setStyle("-fx-border-color: black");
        hbox.setStyle("-fx-background-color:grey");
    }

    public BorderPane getRadioGroupPane() {
        return radioGroupPane;
    }
    public Button getHomeButton() { return homeButton; }
}
