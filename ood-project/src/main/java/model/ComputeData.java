package model;

import java.util.List;

import static util.SectorDataUtil.getAllSectorMonthlyDataList;

public interface ComputeData {

    void computeDataForChart();

    void createMapForChart();

    static List<List<MonthlyData>> getAllSectorMonthlyData() {
        return getAllSectorMonthlyDataList();
    };

}
