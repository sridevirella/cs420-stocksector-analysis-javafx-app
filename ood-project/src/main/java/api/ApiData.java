package api;

import model.SectorName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiData {

    public ApiData() {}

    public boolean getDataFromApi() {

        try {
            List<String> sectorNamesList = new ArrayList<>();

            Arrays.asList(SectorName.values()).forEach(sectorName -> sectorNamesList.add(sectorName.getSector()));
            WriteToFile writeFileInstance = new WriteToFile(sectorNamesList);
            writeFileInstance.getApiDataAndWriteToFile();
            return true;

        }catch(Exception e) {
            return false;
        }
    }
}
