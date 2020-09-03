import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {

    public static String loadCodigoFuente(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String stringBuffer = "";
            String line = reader.readLine();
            while(line != null) {
                stringBuffer += line + '\n';
                line = reader.readLine();
            }
            reader.close();
            return stringBuffer;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
