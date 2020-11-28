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
    private static Map<SectorName, Map<Number, Number>> sectorYearlyPriceMap = new HashMap<>();
    Map<SectorName, Map<YearName, List<MonthlyData>>> sectorDataMap;

    public YearlyPriceData() {}

    public Map<SectorName, Map<Number, Number>> getYearlyPriceData() throws IOException {

        sectorDataMap = SectorDataUtil.getSectorsDataMap();
        computeDataForChart();
        createMapForChart();
        return sectorYearlyPriceMap;
    }

    @Override
    public void computeDataForChart() {

        sectorDataMap.forEach((sector, yearNameListMap) -> {

            yearlyPriceDataMap = new HashMap<>();
            yearNameListMap.forEach((year, monthlyDataList) -> {

                yearlyPriceDataMap.put(year.getYear(), monthlyDataList.stream()
                        .filter(monthlyData -> (year.getYear() == 2020) ? monthlyData.getDate().contains(year.getYear()+",NOVEMBER")
                                : monthlyData.getDate().contains(year.getYear()+",DECEMBER"))
                        .map(MonthlyData::getClosePrice)
                        .collect(Collectors.toList()).get(0));

            });
            sectorYearlyPriceMap.put(sector, yearlyPriceDataMap);
        });
    }

    @Override
    public void createMapForChart() {}
}
