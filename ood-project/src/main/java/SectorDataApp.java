
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import view.DisplayData;

import java.io.IOException;

public class SectorDataApp extends Application {

    private DisplayData views;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, ParseException {

        views = new DisplayData();
        createBorderPane( stage );
    }

    private void createBorderPane( Stage stage ) {

        BorderPane pane = new BorderPane();
        HBox hbox = new HBox();
        setHBox( hbox );
        pane.setTop( hbox );
        pane.setCenter( views.getListView() );
        setStage( pane, stage );
    }

    private void setHBox( HBox hbox ) {

        hbox.setSpacing(50);
        hbox.getChildren().addAll(views.getSectorComboBox(),  views.getYearlyComboBox(), views.getListView());
        HBox.setMargin(views.getSectorComboBox(), new Insets(30, 5, 10, 10));
        HBox.setMargin(views.getYearlyComboBox(), new Insets(30, 5, 5, 10));
    }

    private void setStage( BorderPane pane, Stage stage ){

        Scene scene = new Scene(pane, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Stock Sectors Yearly Data");
        stage.show();
    }
}
