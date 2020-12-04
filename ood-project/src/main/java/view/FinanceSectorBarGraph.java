package view;

import computedata.YearlyPriceData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.SectorName;
import model.YearName;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class FinanceSectorBarGraph {

    private BarChart<String, Number> barChart;
    private static CategoryAxis xAxis;
    private static NumberAxis yAxis;
    private static XYChart.Series<String, Number> seriesName;
    private static YearlyPriceData yearlyPriceDataIns;
    private static Map<Number, Number> financeDataMap;

    FinanceSectorBarGraph() throws IOException {

        yearlyPriceDataIns = new YearlyPriceData();
        this.barChart =  initAxisAndChart();
        addDataToBarGraph();
    }

    private void addDataToBarGraph() throws IOException {

        setChartProperties();
        initChartSeries();
        financeDataMap = getFinanceSectorDataMap();
        createBarChartSeries();
    }

    private BarChart<String, Number> initAxisAndChart() {

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        return new BarChart<>(xAxis, yAxis);
    }

    private void setChartProperties() {

        barChart.setTitle("Finance sector year start stock price distribution");
        xAxis.setLabel("year");
        yAxis.setLabel("Price Value in $");
        barChart.setBarGap(10);
    }

    private void initChartSeries() {

        seriesName = new XYChart.Series<>();
    }

    private void createBarChartSeries() throws IOException {

        seriesName.setName(YearName.YEAR_2008.toString());
        ObservableList<XYChart.Data<String, Number>> countObservableList = FXCollections.observableArrayList( addDataToBarChart());
        seriesName.setData(countObservableList);
        barChart.getData().add(seriesName);
    }

    private Map<Number, Number> getFinanceSectorDataMap() throws IOException {

        return yearlyPriceDataIns.getYearlyPriceData(false).get(SectorName.FINANCIAL);
    }

    private List<XYChart.Data<String, Number>> addDataToBarChart() {


        return Arrays.stream(YearName.values())
                .map( key -> { return new XYChart.Data<>(String.valueOf(key.getYear()), financeDataMap.get(key.getYear()) ); })
                .collect(Collectors.toList());
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }
}
