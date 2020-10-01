package compilador;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[1;31m";
    public static final String ANSI_GREEN = "\u001B[1;32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GRAY = "\u001B[90m";

    public static void main(String[] args) {
        TablaSimbolos.init();

        AnalizadorLexico al = new AnalizadorLexico(FileManager.loadCodigoFuente("CasosDePrueba/Test_IF.txt"));
        Parser parser = new Parser(al);
        parser.yyparse();
        TablaSimbolos.print();
    }

    //TODO: El analizador sintáctico reconoce que falta literal ';' en la siguiente línea de código
    //TODO: No se permite "." como valor de float 0.0 ni el "_l" como 0_l
    //TODO: hay que propagar el error a las reglas de arriba???
    //TODO: cuando se cambia el signo a una constante negativa, se preserva la constante positiva???

}
