package compilador;

import java.io.FileNotFoundException;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[1;31m";
    public static final String ANSI_GREEN = "\u001B[1;32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GRAY = "\u001B[90m";
    public static final String ANSI_BOLD_WHITE = "\u001B[1;0m";
    public static final long MAX_LONG = 2147483648L;
    public static final float MAX_FLOAT = 3.40282347e+38f;
    public static final float MIN_FLOAT = 1.17549435e-38f;

    public static void main(String[] args) throws Exception {
        TablaSimbolos.init();
        String filePath = "CasosDePrueba/Test_entrega.txt";
        StringBuilder codigoFuente;

//        try {
//            filePath = args[0];
//        } catch (Exception e) {
//            throw new Exception("No se ha ingresado un archivo");
//        }

        try {
            codigoFuente = FileManager.loadCodigoFuente(filePath);
        } catch (Exception e){
            throw new Exception("No se ha encontrado el archivo "+ filePath);
        }

        AnalizadorLexico al = new AnalizadorLexico(codigoFuente);
        Parser parser = new Parser(al, false);
        parser.yyparse();
        System.out.println();
        TablaSimbolos.print();

    }
}
