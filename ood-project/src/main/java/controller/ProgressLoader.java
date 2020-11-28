package controller;

import api.ApiData;
import util.FilePath;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class ProgressLoader {

    private Stage primaryStage;
    private static Pane progressLayout;
    private static ProgressBar progressBar;

    public ProgressLoader(Stage stage) throws IOException {

        this.primaryStage = stage;
        initPreLoader();
    }

    public void initPreLoader() throws IOException {

        Task<Boolean> task = defineApiTask();
        getSplashMessageBox();
        runApiTask(task);
    }

    private Task<Boolean> defineApiTask() {

        return new Task<>() {
                @Override public Boolean call() throws IOException, InterruptedException {
                    return new ApiData().getDataFromApi();
                }
            };
    }

    private void runApiTask(Task<Boolean> task) {

        task.setOnRunning((e) -> {
            progressBar.progressProperty().bind(task.progressProperty());
            taskListener(task);
            setSceneAndStage();
        });
        taskOnSuccessORFailure(task);
    }

    private void taskOnSuccessORFailure(Task<Boolean> task) {

        task.setOnSucceeded((e) -> showMainStage());
        task.setOnFailed((e) -> { createNoApiDataStage(); });
        new Thread(task).start();
    }

    private void setSceneAndStage() {

        Scene progressScene = new Scene(progressLayout, 600, 220);
        primaryStage.setScene(progressScene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    private void taskListener(Task<Boolean> task) {

        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                setUnBindProperties();
                createFadeEffect();
            }
        });
    }

    private void createFadeEffect() {

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), progressLayout);
        fadeTransition.setFromValue(1.5);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(actionEvent -> primaryStage.hide());
        fadeTransition.play();
    }

    private void setUnBindProperties() {

        progressBar.progressProperty().unbind();
        progressBar.setProgress(1.0);
        primaryStage.toFront();
    }

    private void createNoApiDataStage() {

        BorderPane pane = new BorderPane();
        pane.setCenter(new Text("There is a problem while fetching data from the api"));
        Stage newStage = new Stage();
        Scene newScene = new Scene(pane, 600, 600);
        newStage.setScene(newScene);
        newStage.show();
    }

    private void showMainStage() {
        try {
            Stage mainStage = new Stage();
            new StockDataController(mainStage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSplashMessageBox() throws IOException {

        HBox hbox = createProgressBarHBox();
        setProgressBarProperties(hbox);
    }

    private void setProgressBarProperties(HBox hbox) {

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(600);
        progressLayout = new VBox();
        progressLayout.getChildren().addAll(hbox, progressBar);
        progressLayout.setEffect(new InnerShadow());
    }

    private HBox createProgressBarHBox() throws IOException {

        ImageView imageIcon = new ImageView(new Image(String.valueOf(FilePath.getFilePath("api-fetch.png").toUri())));
        Label data = new Label("Data fetch from Api is in progress and\n it takes up to 3 minutes....");
        data.setPadding(new Insets(90,10,10,10));
        data.setStyle("-fx-font-size: 15pt;");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(imageIcon, data);
        hbox.setSpacing(10);
        return hbox;
    }
}