package view;

import computedata.StockGainLossData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.FilePath.caseFormatter;

class LossPercentageBarGraph {

    private BarChart<String, Number> barChart;
    private static CategoryAxis xAxis;
    private static NumberAxis yAxis;
    private static XYChart.Series<String, Number> seriesName;
    private static String year;
    private static StockGainLossData lossPerChartInstance;

    LossPercentageBarGraph(String fromDate, String toDate) {

        lossPerChartInstance = new StockGainLossData(fromDate, toDate);
        year = fromDate.split(",")[0];
        this.barChart =  initAxisAndChart();
        addDataToBarGraph();
    }

    private void addDataToBarGraph() {

        setChartProperties();
        initChartSeries();
        createBarChartSeries();
    }

    private BarChart<String, Number> initAxisAndChart() {

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        return new BarChart<>(xAxis, yAxis);
    }

    private void setChartProperties() {

        barChart.setTitle("Sector price gain percentage during the " + year + " recession");
        xAxis.setLabel("Sector");
        yAxis.setLabel("Price Loss Percentage");
        barChart.setBarGap(10);
    }

    private void initChartSeries() {

        seriesName = new XYChart.Series<>();
    }

    private void createBarChartSeries() {

        seriesName.setName(year);
        ObservableList<XYChart.Data<String, Number>> countObservableList = FXCollections.observableArrayList( addDataToBarChart(lossPerChartInstance.stockGainPercentageData()) );
        seriesName.setData(countObservableList);
        barChart.getData().add(seriesName);
    }

    private List<XYChart.Data<String, Number>> addDataToBarChart(Map<String, Number> yearByVolumeDataMap) {

        return yearByVolumeDataMap.keySet()
                .stream()
                .map( key -> { return new XYChart.Data<>(caseFormatter(key), lossPerChartInstance.stockGainPercentageData().get(key) ); })
                .collect(Collectors.toList());
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }
}
