import controller.ProgressLoader;
import controller.StockDataController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class SectorDataApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        try {
//            new ProgressLoader(primaryStage);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        new StockDataController(primaryStage);
    }
}