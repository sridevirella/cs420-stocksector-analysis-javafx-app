package computedata;

import model.ComputeData;
import model.MonthlyData;
import model.SectorName;
import model.YearName;

import java.math.BigInteger;
import java.util.*;

public class VolumeCountData implements ComputeData {

    private static List<Number> targetVolumeCountDataList = new ArrayList<>();
    private static Map<String, Number> targetVolumeCountDataMap = new HashMap<>();

    public VolumeCountData() {}

    public Map<String, Number> getTargetVolumeCountData() {

        computeDataForChart();
        createMapForChart();
        return targetVolumeCountDataMap;
    }

    @Override
    public void computeDataForChart() {

        ComputeData.getAllSectorMonthlyData().forEach(monthlyDataList -> {

            long count = monthlyDataList.stream()
                            .filter(monthlyData -> monthlyData.getDate().contains(YearName.YEAR_2018.toString())
                                    || monthlyData.getDate().contains(YearName.YEAR_2019.toString())
                                    || monthlyData.getDate().contains(YearName.YEAR_2020.toString()))
                            .mapToLong(MonthlyData::getInFlowVolume)
                            .mapToObj(BigInteger::valueOf)
                            .filter( volume -> volume.compareTo( BigInteger.valueOf( 100000000 ) )> 0)
                            .count();
            targetVolumeCountDataList.add(count);
        });
    }

    @Override
    public void createMapForChart() {

        Arrays.stream(SectorName.values()).forEach(sectorName -> {
            targetVolumeCountDataMap.put(sectorName.name(), targetVolumeCountDataList.get(sectorName.getOrder()-1));
        });
    }
}
