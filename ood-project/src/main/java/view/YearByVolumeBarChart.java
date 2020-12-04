package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.YearName;
import computedata.YearByVolumeData;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.FilePath.caseFormatter;


class YearByVolumeBarChart {

    private BarChart<String, Number> barChart;
    private static List<YearName> yearList;
    private static CategoryAxis xAxis;
    private static NumberAxis yAxis;
    private static XYChart.Series<String, Number> series_2018;
    private static XYChart.Series<String, Number> series_2019;
    private static XYChart.Series<String, Number> series_2020;
    private static YearByVolumeData yearByVolumeInst;

        YearByVolumeBarChart() {

            this.barChart = initAxisAndChart();
            yearByVolumeInst = new YearByVolumeData();
            addDataToBarGraph();
    }

    private void addDataToBarGraph() {

        initAxisAndChart();
        setChartProperties();
        initChartSeries();
        yearList = getRequiredYearList();
        createBarChartSeries();
    }

    private BarChart<String, Number> initAxisAndChart() {

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        return new BarChart<>(xAxis, yAxis);
    }

    private void setChartProperties() {

        xAxis.setLabel("Sector");
        yAxis.setLabel("Volume In Millions $");
        barChart.setTitle("Each sector total inflow cash for the years 2018, 2019 and 2020");
    }

    private void initChartSeries() {

        series_2018 = new XYChart.Series<>();
        series_2019 = new XYChart.Series<>();
        series_2020 = new XYChart.Series<>();
    }

    private List<YearName> getRequiredYearList() {

        return Arrays.stream(YearName.values())
                .filter(yearName -> yearName.equals(YearName.YEAR_2018)
                        || yearName.equals(YearName.YEAR_2019)
                        || yearName.equals(YearName.YEAR_2020))
                .collect(Collectors.toList());
    }

    private List<XYChart.Data<String, Number>> addDataToBarChart(Map<String, Number> yearByVolumeDataMap) {

        return yearByVolumeDataMap.keySet()
                .stream()
                .map( key -> { return new XYChart.Data<>( caseFormatter(key), yearByVolumeDataMap.get(key) ); })
                .collect(Collectors.toList());
    }

    private void createObservableList(YearName year, XYChart.Series<String, Number> series_name) {

        series_name.setName(year.toString());
        ObservableList<XYChart.Data<String, Number>> volumeObservableList =  FXCollections.observableArrayList(
                                                                             addDataToBarChart(yearByVolumeInst.getYearByVolumeDataMap()) );
        series_name.setData(volumeObservableList);
        barChart.getData().add(series_name);
    }

    private void createBarChartSeries() {

        yearList.forEach(year -> {
            yearByVolumeInst.getYearByVolumeData(year);
            getSeriesAndCreateList(year);
        });
    }

    private void getSeriesAndCreateList(YearName year) {

        if(year.getYear() == YearName.YEAR_2018.getYear())
            createObservableList(year, series_2018);

        else if (year.getYear() == YearName.YEAR_2019.getYear())
            createObservableList(year, series_2019);

        else if(year.getYear() == YearName.YEAR_2020.getYear())
            createObservableList(year, series_2020);
    }

    public BarChart<String, Number> getBarChart() {
            return barChart;
    }
}
