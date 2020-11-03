import api.WriteFile;
import domain.SectorName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main( String[] args ) {

        List<String> sectorNamesList = new ArrayList<>();
       Arrays.asList(SectorName.values()).forEach( sectorName -> {
           sectorNamesList.add(sectorName.getSector());
       });

        try {
              WriteFile writeFileInstance = new WriteFile( sectorNamesList );
              writeFileInstance.getApiDataAndWriteToFile();

        } catch ( IOException | InterruptedException use ) {
            System.out.println( use.toString() );
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
