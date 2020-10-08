package util;

import domain.MonthlyData;
import domain.SectorName;
import domain.YearName;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class SectorDataUtil {

    private static Map<YearName, List<MonthlyData>> yearlyDataMap;
    private SectorDataUtil() {}

    public static Map<SectorName, Map<YearName, List<MonthlyData>>> getSectorsDataMap() throws IOException, ParseException {

        Map<SectorName, Map<YearName, List<MonthlyData>>> sectorDataMap = new HashMap<>();
        getAllSectorsData( sectorDataMap );
        return sectorDataMap;
    }

    private static void getAllSectorsData( Map<SectorName, Map<YearName, List<MonthlyData>>> sdm ) throws IOException, ParseException {

        for (SectorName sectorName : SectorName.values())
            sdm.put( sectorName, YearlyDataUtil.getYearlyDataMap(sectorName.getSector() + ".txt") );
    }

    public static void setSelectedSectorYearlyData( Map<YearName, List<MonthlyData>> yearlyMap ) {
        yearlyDataMap = yearlyMap;
    }

    public static Map<YearName, List<MonthlyData>> getSelectedSectorYearlyData(){
        return yearlyDataMap;
    }

}
