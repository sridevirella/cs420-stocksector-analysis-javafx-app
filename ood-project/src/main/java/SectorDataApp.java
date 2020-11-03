import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.DisplaySectorsData;
import view.SectorStockCharts;

import java.io.IOException;

public class SectorDataApp extends Application {

    private DisplaySectorsData sectorDataViews;
    private SectorStockCharts chartViews;
    private Scene scene1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        this.sectorDataViews = new DisplaySectorsData();
        this.chartViews = new SectorStockCharts();
        createBorderPanes(stage);
        radioGroupListener(stage);
    }

    private void radioGroupListener(Stage stage) {

        sectorDataViews.getChartsButton().setOnMouseClicked(event -> stage.setScene(scene1));
    }

    private void createBorderPanes( Stage stage ) {

        setPrimaryStage( sectorDataViews.getComboBoxGroupPane(), stage );
        scene1 = new Scene(chartViews.getRadioGroupPane(), 700, 600);
    }

    private void setPrimaryStage( BorderPane pane, Stage stage ){

        Scene scene = new Scene(pane, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Stock Sectors Yearly Data");
        stage.show();
    }
}
