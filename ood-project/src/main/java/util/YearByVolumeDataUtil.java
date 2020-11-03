package util;

import domain.MonthlyData;
import domain.SectorName;
import domain.YearName;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static util.SectorDataUtil.getAllSectorMonthlyDataList;

public class YearByVolumeDataUtil {

    private static List<Long> yearByVolumeDataList;
    private static Map<String, Long> yearByVolumeDataMap;

    private YearByVolumeDataUtil() {}

    public static void getYearByVolumeData(YearName year) {

        computeYearlyVolume(year);
    }

    private static void computeYearlyVolume(YearName year) {

        yearByVolumeDataList = new ArrayList<>();
        getAllSectorMonthlyDataList().forEach( monthlyDataList -> getEachYearTotalVolume(monthlyDataList, year.toString()));
    }

    private static void getEachYearTotalVolume(List<MonthlyData> monthlyDataList, String year) {

        List<Long> yearlyVolumeData = monthlyDataList.stream()
                                                     .filter(monthlyData -> monthlyData.getDate().contains(year))
                                                     .mapToLong(MonthlyData::getInFlowVolume)
                                                     .mapToObj(BigDecimal::valueOf)
                                                     .reduce(BigDecimal::add)
                                                     .stream()
                                                     .map(BigDecimal::longValue)
                                                     .collect(Collectors.toList());
        yearByVolumeDataList.addAll(yearlyVolumeData);
    }

    public static Map<String, Long> getYearByVolumeDataMap() {

        yearByVolumeDataMap = new HashMap<>();

        Arrays.stream(SectorName.values()).forEach(sectorName -> {
          yearByVolumeDataMap.put(sectorName.name(), yearByVolumeDataList.get(sectorName.getOrder()-1));
        });
        return yearByVolumeDataMap;
    }
}
