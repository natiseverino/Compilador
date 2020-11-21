package compilador;

import compilador.codigoIntermedio.PolacaInversa;
import compilador.codigoIntermedio.PolacaInversaProcedimientos;
import compilador.codigoSalida.GeneradorCodigo;
import compilador.codigoSalida.GeneradorCodigo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

    public static void guardarArchivo(String txt, String path){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(txt);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        TablaSimbolos.init();
        String filePath = "CasosDePrueba/Test_asm.txt";
        StringBuilder codigoFuente;

        String fileNameAsm = "codigosalida.asm";

//        try {
//            filePath = args[0];
//        } catch (Exception e) {
//            throw new Exception("No se ha ingresado un archivo");
//        }

        try {
            codigoFuente = FileManager.loadCodigoFuente(filePath);
        } catch (Exception e) {
            throw new Exception("No se ha encontrado el archivo " + filePath);
        }

        AnalizadorLexico al = new AnalizadorLexico(codigoFuente, false);
        PolacaInversa polaca = new PolacaInversa();
        PolacaInversaProcedimientos polacaProcedimientos = new PolacaInversaProcedimientos();
        Parser parser = new Parser(al, false, polaca, polacaProcedimientos, true);
        parser.yyparse();
        System.out.println();
        TablaSimbolos.print();
        Errores.printWarnings();
        Errores.printErrores();
        polaca.print();
        polacaProcedimientos.print();

        int errores = al.getErrores() + parser.getErrores();

        if (errores > 0) {
            System.out.println();
            System.out.println("Hay " + errores + " sintacticos y lexicos");
            System.out.println("No se genero el codigo assembler");
        }
        else {

            String code = GeneradorCodigo.getInstance().generarCodigo(polaca);
            int gc_errores = polaca.getErrores();
            if (gc_errores > 0) {
                System.out.println();
                System.out.println("Hay " + gc_errores + " errores en la generacion de codigo");
                System.out.println("No se genero el codigo assembler");
            } else {
                guardarArchivo(code, fileNameAsm);
                System.out.println("Se genero el codigo assembler en " + fileNameAsm);
            }

        }

    }
}
