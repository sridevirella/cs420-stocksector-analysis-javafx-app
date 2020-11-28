package util;

import model.MonthlyData;
import model.SectorName;
import model.YearName;

import java.io.IOException;
import java.util.*;

import static util.YearlyDataUtil.getMonthlyDataList;
import static util.YearlyDataUtil.getYearlyDataMap;

public class SectorDataUtil {

    private static List<List<MonthlyData>> allSectorMonthlyDataList;
    private SectorDataUtil() {}

    public static Map<SectorName, Map<YearName, List<MonthlyData>>> getSectorsDataMap() throws IOException {

        Map<SectorName, Map<YearName, List<MonthlyData>>> sectorDataMap = new HashMap<>();
        getAllSectorsData(sectorDataMap);
        return sectorDataMap;
    }

    private static void getAllSectorsData(Map<SectorName, Map<YearName, List<MonthlyData>>> sdm) throws IOException {

        allSectorMonthlyDataList = new ArrayList<>(SectorName.values().length);

        for (SectorName sectorName : SectorName.values()) {
            sdm.put(sectorName, getYearlyDataMap(sectorName.getSector() + ".txt"));
            allSectorMonthlyDataList.add(getMonthlyDataList());
        }
    }

    public static List<List<MonthlyData>> getAllSectorMonthlyDataList() {

        return allSectorMonthlyDataList;
    }
}

