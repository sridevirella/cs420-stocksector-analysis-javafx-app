package view;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;
import util.FilePath;

import java.io.IOException;
import java.util.Map;

class YearlyPriceLineChart {

    private static NumberAxis xAxis;
    private static NumberAxis yAxis;
    private static XYChart.Series<Number, Number> series;
    private LineChart<Number, Number> lineChart;

    YearlyPriceLineChart() {

        initAxisAndSeries();
        this.lineChart = initLineChart();
    }

    private void initAxisAndSeries() {

        xAxis = new NumberAxis();
        xAxis.setLabel("Year");
        yAxis = new NumberAxis();
        yAxis.setLabel("Stock Price in $");
        series = new XYChart.Series<>();
        xAxisTickFormat();
    }

    private void xAxisTickFormat() {

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(2008);
        xAxis.setUpperBound(2020);
        xAxis.setTickUnit(1);
        setTickStringFormat();
    }

    private void setTickStringFormat() {

        xAxis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number object) {
                return object.toString().substring(0,4);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
    }

    private LineChart<Number, Number> initLineChart() {

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Sector Yearly Stock Price");
        lineChart.getData().add(series);
        return lineChart;
    }

    public LineChart<Number, Number> addDataToLineChart(Map<Number, Number> yearlyPriceDataMap, String sectorName) throws IOException {

        yearlyPriceDataMap.forEach((key, value) -> {
            series.getData().add(new XYChart.Data<>(key, value));
            series.setName( formatter(sectorName));
        });
        return lineChart;
    }

    private String formatter(String sectorName) {

        return FilePath.caseFormatter(sectorName);
    }
}
