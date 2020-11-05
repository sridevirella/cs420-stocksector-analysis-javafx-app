import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.DisplaySectorsData;
import view.SectorStockCharts;

import java.io.IOException;

public class SectorDataApp extends Application {

    private static DisplaySectorsData sectorDataViews;
    private static SectorStockCharts chartViews;
    private static Scene primaryScene;
    private static Scene SecondaryScene;
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        primaryStage = stage;
        sectorDataViews = new DisplaySectorsData();
        chartViews = new SectorStockCharts();
        createBorderPanes();
        radioGroupListener();
        homeButtonListener();
    }

    private void radioGroupListener() {

        sectorDataViews.getChartsButton().setOnMouseClicked(event -> primaryStage.setScene(SecondaryScene));
    }

    private void homeButtonListener() {

        chartViews.getHomeButton().setOnMouseClicked(event -> primaryStage.setScene(primaryScene));
    }

    private void createBorderPanes() {

        setPrimaryStage( sectorDataViews.getComboBoxGroupPane() );
        SecondaryScene = new Scene(chartViews.getRadioGroupPane(), 700, 600);
    }

    private void setPrimaryStage( BorderPane pane){

        primaryScene = new Scene(pane, 700, 500);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Stock Sectors Yearly Data");
        primaryStage.show();
    }
}
