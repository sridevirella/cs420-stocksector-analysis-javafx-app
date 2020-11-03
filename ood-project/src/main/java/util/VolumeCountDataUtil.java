package util;

import domain.MonthlyData;
import domain.SectorName;
import domain.YearName;

import java.math.BigInteger;
import java.util.*;

import static util.SectorDataUtil.getAllSectorMonthlyDataList;

public class VolumeCountDataUtil {

    private static List<Number> targetVolumeCountDataList = new ArrayList<>();
    private static Map<String, Number> targetVolumeCountDataMap = new HashMap<>();

    private VolumeCountDataUtil() {}

    public static void getTargetVolumeCountData() {

        getAllSectorMonthlyDataList().forEach(VolumeCountDataUtil::getTargetVolumeCount);
    }

    private static void getTargetVolumeCount(List<MonthlyData> monthlyDataList) {

      long count =
                monthlyDataList.stream()
                .filter(monthlyData -> monthlyData.getDate().contains(YearName.YEAR_2018.toString())
                                       || monthlyData.getDate().contains(YearName.YEAR_2019.toString())
                                       || monthlyData.getDate().contains(YearName.YEAR_2020.toString()))
                .mapToLong(MonthlyData::getInFlowVolume)
                .mapToObj(BigInteger::valueOf)
                .filter( volume -> volume.compareTo( BigInteger.valueOf( 100000000 ) )> 0)
                .count();
       targetVolumeCountDataList.add(count);
    }

    public static Map<String, Number> getTargetVolumeCountDataMap() {

        Arrays.stream(SectorName.values()).forEach(sectorName -> {
            targetVolumeCountDataMap.put(sectorName.name(), targetVolumeCountDataList.get(sectorName.getOrder()-1));
        });
        return targetVolumeCountDataMap;
    }
}
