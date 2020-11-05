package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;

import java.util.List;
import java.util.stream.Collectors;

import static util.StockGainDataUtil.getStockGainPercentageMap;
import static util.StockGainDataUtil.stockGainPercentageData;

class GainPercentageChart {

    private PieChart pieChart;

        GainPercentageChart() {

            this.pieChart = initPieChart();
    }

    private PieChart initPieChart() {

        ObservableList<PieChart.Data> stockGainPieChart = FXCollections.observableArrayList( addDataToPieChart() );
        pieChart = new PieChart(stockGainPieChart);
        setPieChartProperties();
        return pieChart;
    }

    private List<PieChart.Data> addDataToPieChart() {

        stockGainPercentageData();
        return getStockGainPercentageMap().keySet()
                                          .stream()
                                          .map( key -> { return new PieChart.Data( key, getStockGainPercentageMap().get(key) ); })
                                          .collect(Collectors.toList());
    }

    private void setPieChartProperties() {

        pieChart.setTitle("Sector stock price gain percentage from the year 2018 - 2020");
        pieChart.setLabelLineLength(10);
        pieChart.setLegendSide(Side.LEFT);
    }

    public PieChart getPieChart() {
        return pieChart;
    }
}
