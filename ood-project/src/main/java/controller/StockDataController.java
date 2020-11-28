package controller;

import util.FilePath;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import view.AllSectorsData;
import view.MoreCharts;
import view.SectorStockCharts;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class StockDataController {

    private static SectorStockCharts sectorStockCharts;
    private static AllSectorsData sectorDataViews;
    private static MoreCharts chartViews;
    private static BorderPane primaryPane;
    private static Button primaryButton, allDataButton, queryButton;
    private static VBox primaryVBox;
    private Stage primaryStage;

    public StockDataController(Stage stage) throws IOException {

        this.primaryStage = stage;
        initViews();
    }

    private void initViews() throws IOException {

        sectorStockCharts = new SectorStockCharts();
        sectorDataViews = new AllSectorsData();
        chartViews = new MoreCharts();
        createPrimaryVBox();
        createPaneAndScenes();
    }

    private void createPaneAndScenes() {

        primaryPane = new BorderPane();
        primaryPane.setLeft(primaryVBox);
        Scene primaryScene = new Scene(primaryPane, 900, 600);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Stock Sectors Data Analysis from Year 2008 - 2020");
        primaryStage.show();
    }

    private void createPrimaryVBox() throws IOException {

        initButtons();
        setButtonProperties();
        initPrimaryVBox();
        buttonActionListener();
    }

    private void buttonActionListener() {

        primaryButtonListener();
        allDataButtonListener();
        queryButtonListener();
    }

    private void queryButtonListener() {

        queryButton.setOnMouseClicked(event -> {
            restoreButtonColors(queryButton);
            queryButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#98ff98"), null, null)));
            primaryPane.setCenter(chartViews.getRadioGroupVBox());
        });
    }

    private void allDataButtonListener() {

        allDataButton.setOnMouseClicked(event -> {
            restoreButtonColors(allDataButton);
            allDataButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#98ff98"), null, null)));
            primaryPane.setCenter(sectorDataViews.getAllSectorDataVBox());
        });
    }

    private void primaryButtonListener() {

        primaryButton.setOnMouseClicked(event -> {
            restoreButtonColors(primaryButton);
            primaryButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#98ff98"), null, null)));
            primaryPane.setCenter(sectorStockCharts.getAllDataVBox());
        });
    }

    private void restoreButtonColors(Button selection) {

        Stream.of(primaryButton, allDataButton, queryButton)
                .filter(button -> !button.equals(selection))
                .forEach(button -> button.setBackground(new Background(new BackgroundFill(Paint.valueOf("#F0F0F0"), null, null))));
    }

    private void initButtons() {

        primaryButton = new Button("Main Questions");
        allDataButton = new Button("Display All data");
        queryButton = new Button("Query More Data");
        Arrays.asList(primaryButton, allDataButton, queryButton).forEach( button -> button.setStyle("-fx-font-size:14"));
    }

    private void setButtonProperties() {

        Arrays.asList(primaryButton, allDataButton, queryButton).forEach(button -> {
            button.setMinWidth(180);
            button.setMinHeight(50);
        });
    }

    private void initPrimaryVBox() throws IOException {

        primaryVBox = new VBox();
        primaryVBox.setStyle("-fx-border-insets: 5; -fx-border-width: 2; -fx-border-style: solid; -fx-font-family: Courier");
        primaryVBox.setEffect(new DropShadow());
        primaryVBox.setPadding(new Insets(10,10,10,20));
        primaryVBox.getChildren().addAll(getSectorIcon(), getButtonVBox());
        primaryVBox.setSpacing(60);
    }

    private HBox getSectorIcon() throws IOException {

        ImageView stockIcon = new ImageView(new Image(String.valueOf(FilePath.getFilePath("stock-icon.jpg").toUri())));
        stockIcon.setFitHeight(100);
        stockIcon.setFitWidth(100);
        HBox imgHBox = new HBox();
        imgHBox.getChildren().add(stockIcon);
        imgHBox.setPadding(new Insets(10,30,40,20));
        stockIcon.setEffect(new DropShadow());
        return imgHBox;
    }

    private VBox getButtonVBox() {

        VBox buttonVBox = new VBox();
        buttonVBox.setSpacing(20);
        buttonVBox.getChildren().addAll(primaryButton, allDataButton, queryButton);
        return buttonVBox;
    }
}
