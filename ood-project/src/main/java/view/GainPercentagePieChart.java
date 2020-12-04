package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import computedata.StockGainLossData;
import javafx.scene.text.Text;

import static util.FilePath.caseFormatter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class GainPercentagePieChart {

    private PieChart pieChart;
    private static StockGainLossData gainPerChartInstance;
    private static String fromYear, toYear;

        GainPercentagePieChart(String fromDate, String toDate) {

            gainPerChartInstance = new StockGainLossData(fromDate, toDate);
            fromYear = fromDate;
            toYear = toDate;
            this.pieChart = initPieChart();
    }

    private PieChart initPieChart() {

        ObservableList<PieChart.Data> stockGainPieChart = FXCollections.observableArrayList( addDataToPieChart() );
        pieCustomLabels(stockGainPieChart);
        setPieChartProperties();
        return pieChart;
    }

    private List<PieChart.Data> addDataToPieChart() {

        Map<String, Number>  stockGainPercentageMap = gainPerChartInstance.stockGainPercentageData();
        return stockGainPercentageMap.keySet()
                                          .stream()
                                          .map( key -> { return new PieChart.Data(caseFormatter(key), Double.parseDouble(String.valueOf(stockGainPercentageMap.get(key)))); })
                                          .collect(Collectors.toList());
    }

    private void pieCustomLabels(ObservableList<PieChart.Data> stockGainPieChart) {

        pieChart = new PieChart(stockGainPieChart) {
            @Override
            protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
                changePieLabels(top, left, contentWidth, contentHeight);
            }

            private void changePieLabels(double top, double left, double contentWidth, double contentHeight) {

                if (getLabelsVisible())
                    setForEachPieValue();
                super.layoutChartChildren(top, left, contentWidth, contentHeight);
            }

            private void setForEachPieValue() {

                getData().forEach(data -> {
                    Optional<Node> textLabel = pieChart.lookupAll(".chart-pie-label").stream()
                            .filter(value -> value instanceof Text && ((Text) value).getText().contains(data.getName())).findAny();
                    textLabel.ifPresent(node -> ((Text) node).setText(data.getName()));
                });
            }
        };
    }

    private void setPieChartProperties() {

            pieChart.setLegendSide(Side.BOTTOM);
            if(fromYear.split(",")[0].equals(toYear.split(",")[0]))
                pieChart.setTitle("Sector stock price gain percentage for the year "+fromYear.split(",")[0]);
            else
                pieChart.setTitle("Sector stock price gain percentage for the year "+fromYear.split(",")[0] + "-" +toYear.split(",")[0]);
    }

    public PieChart getPieChart() {
        return pieChart;
    }
}
