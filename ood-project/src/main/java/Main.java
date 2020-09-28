import api.ReadFile;
import api.WriteFile;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main( String[] args ) {

        final String[] fileNames = {"XLB.txt","XLC.txt","XLE.txt", "XLF.txt", "XLI.txt","XLK.txt",
                                    "XLP.txt","XLRE.txt","XLU.txt","XLV.txt","XLY.txt"
                                   };
        try {
              WriteFile writeFileInstance = new WriteFile( fileNames );
              writeFileInstance.getApiDataAndWriteToFile();

        } catch ( URISyntaxException | IOException | ParseException | InterruptedException use ) {
            System.out.println( use.toString() );
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
