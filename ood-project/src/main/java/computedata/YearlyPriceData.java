package computedata;

import model.ComputeData;
import model.MonthlyData;
import model.SectorName;
import model.YearName;
import util.SectorDataUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class YearlyPriceData implements ComputeData{

    private static Map<Number, Number> yearlyPriceDataMap;
    private static Map<SectorName, Map<Number, Number>> sectorYearlyPriceMap;
    private static Map<SectorName, Map<YearName, List<MonthlyData>>> sectorDataMap;
    private static boolean isForAllSectors;

    public YearlyPriceData() {}

    public Map<SectorName, Map<Number, Number>> getYearlyPriceData(boolean forAllSectors) throws IOException {

        sectorDataMap = SectorDataUtil.getSectorsDataMap();
        sectorYearlyPriceMap = new HashMap<>();
        isForAllSectors = forAllSectors;
        computeDataForChart();
        createMapForChart();
        return sectorYearlyPriceMap;
    }

    @Override
    public void computeDataForChart() {

        sectorDataMap.forEach((sector, yearNameListMap) -> {

            yearlyPriceDataMap = new HashMap<>();
            yearNameListMap.forEach(this::getYearlyDataMap);
            sectorYearlyPriceMap.put(sector, yearlyPriceDataMap);
        });
    }

    private void getYearlyDataMap(YearName year, List<MonthlyData> monthlyDataList) {

        if(isForAllSectors)
            getAllSectorsYearlyDataMap(year, monthlyDataList);
        else
            getOneSectorYearlyDataMap(year, monthlyDataList);
    }

    private void getOneSectorYearlyDataMap(YearName year, List<MonthlyData> monthlyDataList) {

        yearlyPriceDataMap.put(year.getYear(), monthlyDataList.stream()
                .filter(monthlyData -> monthlyData.getDate().contains(year.getYear() + ",JANUARY")
                                                           ? monthlyData.getDate().contains(year.getYear() + ",JANUARY")
                                                           : monthlyData.getDate().contains(year.getYear() + ",DECEMBER"))
                .map(MonthlyData::getClosePrice)
                .collect(Collectors.toList()).get(0));
    }

    private void getAllSectorsYearlyDataMap(YearName year, List<MonthlyData> monthlyDataList) {

        yearlyPriceDataMap.put(year.getYear(), monthlyDataList.stream()
                .filter(monthlyData -> (year.getYear() == 2020) ? monthlyData.getDate().contains(year.getYear() + ",NOVEMBER")
                                                                : monthlyData.getDate().contains(year.getYear() + ",DECEMBER"))
                .map(MonthlyData::getClosePrice)
                .collect(Collectors.toList()).get(0));
    }

    @Override
    public void createMapForChart() {}
}
