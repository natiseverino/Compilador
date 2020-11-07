package compilador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {

    public static StringBuilder loadCodigoFuente(String filePath) throws IOException {

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuffer = new StringBuilder();
            String line = reader.readLine();
            while(line != null) {
                stringBuffer.append(line + '\n');
                line = reader.readLine();
            }
            reader.close();
            return stringBuffer;

    }
}
