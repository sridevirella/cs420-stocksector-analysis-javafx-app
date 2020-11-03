package util;

import domain.MonthlyData;
import domain.SectorName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static util.SectorDataUtil.getAllSectorMonthlyDataList;

public class StockGainDataUtil {

    private static List<Integer> stockGainPercentageList = new ArrayList<>();
    private static Map<String, Integer> stockGainPercentageMap = new HashMap<>();

    private StockGainDataUtil() {}

    public static void stockGainPercentageData() {

        getAllSectorMonthlyDataList().forEach(StockGainDataUtil::computeGainPercentage);
    }

    private static void computeGainPercentage(List<MonthlyData> sector) {

        List<Integer> priceGrowthPercentage = sector.stream()
                                                    .filter(monthlyData -> monthlyData.getDate().contains("2020,OCTOBER") || monthlyData.getDate().equals("2018,DECEMBER"))
                                                    .mapToDouble(MonthlyData::getClosePrice)
                                                    .mapToObj(BigDecimal::valueOf)
                                                    .reduce((t, d) -> t.subtract(d).divide(d, 2, RoundingMode.CEILING).multiply(BigDecimal.valueOf(100)))
                                                    .stream()
                                                    .map(BigDecimal::intValue)
                                                    .limit(1)
                                                    .collect(Collectors.toList());
        stockGainPercentageList.add(priceGrowthPercentage.get(0));
    }

    public static Map<String, Integer> getStockGainPercentageMap() {

        Arrays.stream(SectorName.values()).forEach(sectorName -> {
            stockGainPercentageMap.put(sectorName.name(), stockGainPercentageList.get(sectorName.getOrder()-1));
        });
        return stockGainPercentageMap;
    }
}
