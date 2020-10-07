package compilador;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[1;31m";
    public static final String ANSI_GREEN = "\u001B[1;32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GRAY = "\u001B[90m";
    public static final String ANSI_BOLD_WHITE = "\u001B[1;0m";

    public static void main(String[] args) {
        TablaSimbolos.init();

        AnalizadorLexico al = new AnalizadorLexico(FileManager.loadCodigoFuente("CasosDePrueba/Test_sentencias.txt"));
        Parser parser = new Parser(al, false);
        parser.yyparse();
        System.out.println();
        TablaSimbolos.print();
    }
}
