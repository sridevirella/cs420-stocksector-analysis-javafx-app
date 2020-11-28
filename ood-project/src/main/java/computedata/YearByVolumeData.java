package computedata;

import model.ComputeData;
import model.MonthlyData;
import model.SectorName;
import model.YearName;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static util.SectorDataUtil.getAllSectorMonthlyDataList;

public class YearByVolumeData implements ComputeData {

    private static List<Number> yearByVolumeDataList;
    private static Map<String, Number> yearByVolumeDataMap;
    private static YearName year;

    public YearByVolumeData() {}

    public void getYearByVolumeData(YearName yearName) {
        year = yearName;
        computeDataForChart();
        createMapForChart();
    }

    @Override
    public void computeDataForChart() {

        yearByVolumeDataList = new ArrayList<>();
        getAllSectorMonthlyDataList().forEach( monthlyDataList -> getEachYearTotalVolume(monthlyDataList, year.toString()));
    }

    @Override
    public void createMapForChart() {

        yearByVolumeDataMap = new HashMap<>();

        Arrays.stream(SectorName.values()).forEach(sectorName -> {
            yearByVolumeDataMap.put(sectorName.name(), yearByVolumeDataList.get(sectorName.getOrder()-1));
        });
    }

    private void getEachYearTotalVolume(List<MonthlyData> monthlyDataList, String year) {

        List<Number> yearlyVolumeData = monthlyDataList.stream()
                                                     .filter(monthlyData -> monthlyData.getDate().contains(year))
                                                     .mapToLong(MonthlyData::getInFlowVolume)
                                                     .mapToObj(BigDecimal::valueOf)
                                                     .reduce(BigDecimal::add)
                                                     .stream()
                                                     .map(BigDecimal::longValue)
                                                     .map(value -> value/100000000)
                                                     .collect(Collectors.toList());
        yearByVolumeDataList.addAll(yearlyVolumeData);
    }

    public Map<String, Number> getYearByVolumeDataMap() {
        return yearByVolumeDataMap;
    }
}
