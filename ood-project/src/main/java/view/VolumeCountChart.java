package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.VolumeCountDataUtil.getTargetVolumeCountData;
import static util.VolumeCountDataUtil.getTargetVolumeCountDataMap;

class VolumeCountChart {

    private BarChart<String, Number> barChart;
    private static CategoryAxis xAxis;
    private static NumberAxis yAxis;
    private static XYChart.Series<String, Number> series_2018_2020;

    VolumeCountChart() {

        this.barChart =  initAxisAndChart();
        addDataToBarGraph();
    }

    private void addDataToBarGraph() {

        setChartProperties();
        initChartSeries();
        getTargetVolumeCountData();
        createBarChartSeries();
    }

    private BarChart<String, Number> initAxisAndChart() {

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        return new BarChart<>(xAxis, yAxis);
    }

    private void setChartProperties() {

        barChart.setTitle("Total occurrences of inflow volume with more than $100 Million.");
        xAxis.setLabel("Sector");
        yAxis.setLabel("Count");
        barChart.setBarGap(10);
    }

    private void initChartSeries() {

        series_2018_2020 = new XYChart.Series<>();
    }

    private List<XYChart.Data<String, Number>> addDataToBarChart(Map<String, Number> yearByVolumeDataMap) {

        return yearByVolumeDataMap.keySet()
                .stream()
                .map( key -> { return new XYChart.Data<String, Number>( key, getTargetVolumeCountDataMap().get(key) ); })
                .collect(Collectors.toList());
    }

    private void createBarChartSeries() {

        series_2018_2020.setName("2018 To 2020");
        ObservableList<XYChart.Data<String, Number>> countObservableList = FXCollections.observableArrayList( addDataToBarChart(getTargetVolumeCountDataMap()) );
        series_2018_2020.setData(countObservableList);
        barChart.getData().add(series_2018_2020);
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }
}
